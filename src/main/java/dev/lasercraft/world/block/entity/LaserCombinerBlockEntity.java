package dev.lasercraft.world.block.entity;

import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.world.block.LaserCombinerBlock;
import dev.lasercraft.world.laser.LaserType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public final class LaserCombinerBlockEntity extends BlockEntity {
    private static final long INPUT_TIMEOUT = 4L;

    private final Map<Direction, InputHit> inputs = new EnumMap<>(Direction.class);
    private LaserType outputType = LaserType.WHITE;
    private Direction leaderDirection = Direction.NORTH;

    public LaserCombinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LASER_COMBINER.get(), pos, state);
    }

    public boolean markHit(Direction direction, LaserType type) {
        if (level == null || level.isClientSide) {
            return shouldEmit(direction);
        }
        inputs.put(direction, new InputHit(type, level.getGameTime()));
        refreshCombination(level.getGameTime());
        return shouldEmit(direction);
    }

    public boolean shouldEmit(Direction incomingDirection) {
        return getBlockState().getValue(LaserCombinerBlock.POWERED)
                && incomingDirection == leaderDirection;
    }

    public LaserType getOutputType() {
        return outputType;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LaserCombinerBlockEntity combiner) {
        if (!level.isClientSide) {
            combiner.refreshCombination(level.getGameTime());
        }
    }

    private void refreshCombination(long now) {
        inputs.entrySet().removeIf(entry -> now - entry.getValue().time() > INPUT_TIMEOUT);
        boolean powered = inputs.size() >= 2;
        LaserType mixedType = powered ? mix(inputs.values()) : outputType;
        Direction leader = powered ? inputs.keySet().stream()
                .min((first, second) -> Integer.compare(first.ordinal(), second.ordinal()))
                .orElse(Direction.NORTH) : leaderDirection;

        BlockState state = getBlockState();
        boolean changed = state.getValue(LaserCombinerBlock.POWERED) != powered
                || outputType != mixedType || leaderDirection != leader;
        outputType = mixedType;
        leaderDirection = leader;
        if (changed && level != null) {
            setChanged();
            level.setBlock(worldPosition, state.setValue(LaserCombinerBlock.POWERED, powered), Block.UPDATE_ALL);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static LaserType mix(Iterable<InputHit> hits) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (InputHit hit : hits) {
            int rgb = hit.type().rgb();
            red += (rgb >> 16) & 0xFF;
            green += (rgb >> 8) & 0xFF;
            blue += rgb & 0xFF;
        }

        int maximum = Math.max(red, Math.max(green, blue));
        if (maximum > 0) {
            red = red * 255 / maximum;
            green = green * 255 / maximum;
            blue = blue * 255 / maximum;
        }

        LaserType closest = LaserType.WHITE;
        long closestDistance = Long.MAX_VALUE;
        for (LaserType candidate : LaserType.values()) {
            int rgb = candidate.rgb();
            long redDelta = red - ((rgb >> 16) & 0xFF);
            long greenDelta = green - ((rgb >> 8) & 0xFF);
            long blueDelta = blue - (rgb & 0xFF);
            long distance = redDelta * redDelta + greenDelta * greenDelta + blueDelta * blueDelta;
            if (distance < closestDistance) {
                closest = candidate;
                closestDistance = distance;
            }
        }
        return closest;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("OutputType", outputType.name());
        tag.putString("LeaderDirection", leaderDirection.getName());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        outputType = LaserType.fromName(tag.getString("OutputType"));
        Direction savedDirection = Direction.byName(tag.getString("LeaderDirection"));
        leaderDirection = savedDirection == null ? Direction.NORTH : savedDirection;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    private record InputHit(LaserType type, long time) {
    }
}
