package dev.lasercraft.world.block.entity;

import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.config.ServerConfig;
import dev.lasercraft.world.block.LaserTurretBlock;
import dev.lasercraft.world.laser.LaserPath;
import dev.lasercraft.world.laser.LaserType;
import dev.lasercraft.world.laser.TurretTargeting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public final class LaserTurretBlockEntity extends BlockEntity {
    private LaserType laserType = LaserType.RED;
    private Vec3 beamEnd = Vec3.ZERO;

    public LaserTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LASER_TURRET.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LaserTurretBlockEntity turret) {
        if (level.isClientSide || level.getGameTime() % 2L != 0L) {
            return;
        }

        Monster target = turret.findTarget();
        boolean powered = target != null;
        Direction facing = powered
                ? Direction.getNearest((float) (target.getX() - pos.getX() - 0.5D),
                (float) (target.getEyeY() - pos.getY() - 0.5D),
                (float) (target.getZ() - pos.getZ() - 0.5D))
                : state.getValue(LaserTurretBlock.FACING);

        if (powered) {
            turret.beamEnd = target.getEyePosition();
            turret.laserType.applyEffect(target, 1);
            turret.syncToClient();
        }

        if (state.getValue(LaserTurretBlock.POWERED) != powered
                || state.getValue(LaserTurretBlock.FACING) != facing) {
            level.setBlock(pos, state.setValue(LaserTurretBlock.POWERED, powered)
                    .setValue(LaserTurretBlock.FACING, facing), Block.UPDATE_ALL);
        }
    }

    @Nullable
    private Monster findTarget() {
        if (level == null) {
            return null;
        }
        Vec3 start = Vec3.atCenterOf(worldPosition);
        double targetRange = ServerConfig.TURRET_RANGE.get();
        AABB searchArea = new AABB(worldPosition).inflate(targetRange);
        return level.getEntitiesOfClass(Monster.class, searchArea, Monster::isAlive).stream()
                .filter(monster -> monster.distanceToSqr(start) <= targetRange * targetRange)
                .filter(monster -> hasLineOfSight(start, monster))
                .min(Comparator.comparingDouble(monster -> monster.distanceToSqr(start)))
                .orElse(null);
    }

    private boolean hasLineOfSight(Vec3 start, Monster target) {
        if (level == null) {
            return false;
        }
        Vec3 end = target.getEyePosition();
        Vec3 clipStart = TurretTargeting.sightStart(start, end);
        HitResult hit = level.clip(new ClipContext(clipStart, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target));
        return hit.getType() == HitResult.Type.MISS;
    }

    public LaserType getLaserType() {
        return laserType;
    }

    public Vec3 getBeamEnd() {
        return beamEnd;
    }

    public void setLaserType(LaserType laserType) {
        this.laserType = laserType;
        setChanged();
        syncToClient();
    }

    private void syncToClient() {
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).inflate(LaserPath.MAX_RENDER_RANGE + 1.0D);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("LaserType", laserType.name());
        tag.putDouble("BeamEndX", beamEnd.x);
        tag.putDouble("BeamEndY", beamEnd.y);
        tag.putDouble("BeamEndZ", beamEnd.z);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        laserType = LaserType.fromName(tag.getString("LaserType"));
        beamEnd = new Vec3(tag.getDouble("BeamEndX"), tag.getDouble("BeamEndY"), tag.getDouble("BeamEndZ"));
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
        if (packet.getTag() != null) {
            load(packet.getTag());
        }
    }
}
