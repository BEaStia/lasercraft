package dev.lasercraft.world.laser;

import dev.lasercraft.config.ServerConfig;
import dev.lasercraft.world.block.LaserCombinerBlock;
import dev.lasercraft.world.block.ColorDetectorBlock;
import dev.lasercraft.world.block.LaserReceiverBlock;
import dev.lasercraft.world.block.entity.LaserCombinerBlockEntity;
import dev.lasercraft.world.block.entity.ColorDetectorBlockEntity;
import dev.lasercraft.world.block.entity.LaserReceiverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class LaserPath {
    public static final int MAX_RANGE = 32;
    public static final int MAX_RENDER_RANGE = 96;

    public static List<LaserSegment> trace(Level level, BlockPos emitterPos, Direction initialDirection,
                                           LaserType initialType, boolean applyEffects) {
        TraceContext context = new TraceContext(level, applyEffects);
        Vec3 start = Vec3.atCenterOf(emitterPos)
                .add(Vec3.atLowerCornerOf(initialDirection.getNormal()).scale(0.51D));
        int configuredRange = Math.min(ServerConfig.BASE_RANGE.get(), ServerConfig.MAX_RANGE.get());
        traceBranch(context, emitterPos, initialDirection, LaserBeam.initial(initialType, configuredRange), start);
        return List.copyOf(context.segments);
    }

    private static void traceBranch(TraceContext context, BlockPos startPos, Direction initialDirection,
                                    LaserBeam beam, Vec3 initialStart) {
        if (++context.branchCount > ServerConfig.MAX_BRANCHES.get() || beam.remainingRange() <= 0) {
            return;
        }
        Direction direction = initialDirection;
        BlockPos cursor = startPos;
        Vec3 segmentStart = initialStart;

        for (int travelled = 0; travelled < beam.remainingRange(); travelled++) {
            cursor = cursor.relative(direction);
            if (!context.visited.add(new PathStep(cursor, direction, beam.type()))) {
                context.segments.add(new LaserSegment(segmentStart, Vec3.atCenterOf(cursor), beam.type()));
                return;
            }

            Vec3 cellEnd = Vec3.atCenterOf(cursor);
            if (context.applyEffects && context.level instanceof ServerLevel serverLevel) {
                affectEntities(serverLevel, segmentStart, cellEnd, beam.type(), beam.strength(), context.affected);
            }

            BlockState state = context.level.getBlockState(cursor);
            if (context.applyEffects && context.level instanceof ServerLevel serverLevel) {
                state = interactWithWorld(serverLevel, cursor, state, beam.type());
            }
            if (state.getBlock() instanceof ColorDetectorBlock) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
                if (context.applyEffects && direction == state.getValue(ColorDetectorBlock.FACING).getOpposite()
                        && context.level.getBlockEntity(cursor) instanceof ColorDetectorBlockEntity detector) {
                    detector.markHit(beam.type());
                }
                return;
            }

            if (state.getBlock() instanceof LaserReceiverBlock) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
                if (context.applyEffects && direction == state.getValue(LaserReceiverBlock.FACING).getOpposite()
                        && context.level.getBlockEntity(cursor) instanceof LaserReceiverBlockEntity receiver) {
                    receiver.markHit();
                }
                return;
            }

            if (state.getBlock() instanceof LaserCombinerBlock) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
                boolean shouldEmit = false;
                LaserType outputType = LaserType.WHITE;
                if (context.level.getBlockEntity(cursor) instanceof LaserCombinerBlockEntity combiner) {
                    shouldEmit = context.applyEffects
                            ? combiner.markHit(direction, beam.type())
                            : combiner.shouldEmit(direction);
                    outputType = combiner.getOutputType();
                }
                if (shouldEmit) {
                    Direction output = state.getValue(LaserCombinerBlock.FACING);
                    int outputRange = beam.remainingRange() - travelled - 1;
                    traceBranch(context, cursor, output, beam.withColor(outputType).branch(outputRange), cellEnd);
                }
                return;
            }

            var opticalResult = LaserOptics.transform(state, direction, beam,
                    beam.remainingRange() - travelled - 1);
            if (opticalResult.isPresent()) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
                for (BeamBranch branch : opticalResult.get()) {
                    traceBranch(context, cursor, branch.direction(), branch.beam(), cellEnd);
                }
                return;
            }

            if (!state.getCollisionShape(context.level, cursor).isEmpty()) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
                return;
            }

            if (travelled == beam.remainingRange() - 1) {
                context.segments.add(new LaserSegment(segmentStart, cellEnd, beam.type()));
            }
        }
    }

    private static void affectEntities(ServerLevel level, Vec3 start, Vec3 end, LaserType type, int strength,
                                       Set<DamageHit> affected) {
        if (!ServerConfig.ENTITY_EFFECTS.get()) {
            return;
        }
        AABB beamBounds = new AABB(start, end).inflate(0.18D);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, beamBounds, LivingEntity::isAlive)) {
            if (affected.add(new DamageHit(entity.getUUID(), type))) {
                type.applyEffect(entity, strength);
            }
        }
    }

    private static BlockState interactWithWorld(ServerLevel level, BlockPos pos, BlockState state, LaserType type) {
        if (!ServerConfig.WORLD_INTERACTIONS.get()) {
            return state;
        }
        if ((type == LaserType.RED || type == LaserType.ORANGE)
                && (state.is(Blocks.ICE) || state.is(Blocks.FROSTED_ICE)
                || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE))) {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            return level.getBlockState(pos);
        }

        if (ServerConfig.TNT_INTERACTION.get()
                && (type == LaserType.RED || type == LaserType.ORANGE) && state.is(Blocks.TNT)) {
            level.removeBlock(pos, false);
            level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    2.5F, Level.ExplosionInteraction.BLOCK);
            return level.getBlockState(pos);
        }

        if ((type == LaserType.BLUE || type == LaserType.LIGHT_BLUE) && state.is(BlockTags.FIRE)) {
            level.removeBlock(pos, false);
            return level.getBlockState(pos);
        }

        if ((type == LaserType.BLUE || type == LaserType.LIGHT_BLUE) && state.is(Blocks.LAVA)) {
            level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
            return level.getBlockState(pos);
        }

        if (ServerConfig.CROP_GROWTH.get()
                && (type == LaserType.GREEN || type == LaserType.LIME || type == LaserType.PINK)
                && state.getBlock() instanceof BonemealableBlock growable
                && level.getRandom().nextInt(8) == 0
                && growable.isValidBonemealTarget(level, pos, state, false)
                && growable.isBonemealSuccess(level, level.getRandom(), pos, state)) {
            growable.performBonemeal(level, level.getRandom(), pos, state);
            return level.getBlockState(pos);
        }

        if (ServerConfig.BLOCK_BREAKING.get() && type == LaserType.BLACK && (state.is(BlockTags.LEAVES)
                || state.is(Blocks.GLASS) || state.is(Blocks.GLASS_PANE)
                || state.is(Blocks.TINTED_GLASS))) {
            level.destroyBlock(pos, true);
            return level.getBlockState(pos);
        }

        return state;
    }

    private record PathStep(BlockPos pos, Direction direction, LaserType type) {
    }

    private record DamageHit(UUID entityId, LaserType type) {
    }

    private static final class TraceContext {
        private final Level level;
        private final boolean applyEffects;
        private final List<LaserSegment> segments = new ArrayList<>();
        private final Set<PathStep> visited = new HashSet<>();
        private final Set<DamageHit> affected = new HashSet<>();
        private int branchCount;

        private TraceContext(Level level, boolean applyEffects) {
            this.level = level;
            this.applyEffects = applyEffects;
        }
    }

    private LaserPath() {
    }
}
