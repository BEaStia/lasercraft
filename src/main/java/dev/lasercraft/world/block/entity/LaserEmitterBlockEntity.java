package dev.lasercraft.world.block.entity;

import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.world.block.LaserEmitterBlock;
import dev.lasercraft.world.laser.LaserPath;
import dev.lasercraft.world.laser.LaserType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public final class LaserEmitterBlockEntity extends BlockEntity {
    private LaserType laserType = LaserType.RED;
    private boolean pulseMode;

    public LaserEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LASER_EMITTER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LaserEmitterBlockEntity emitter) {
        if (!level.isClientSide && state.getValue(LaserEmitterBlock.POWERED) && emitter.isEmitting()
                && level.getGameTime() % 2L == 0L) {
            LaserPath.trace(level, pos, state.getValue(LaserEmitterBlock.FACING), emitter.laserType, true);
        }
    }

    public LaserType getLaserType() {
        return laserType;
    }

    public boolean isEmitting() {
        return level != null && (!pulseMode || level.getGameTime() % 10L < 4L);
    }

    public boolean togglePulseMode() {
        pulseMode = !pulseMode;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
        return pulseMode;
    }

    @Override
    public AABB getRenderBoundingBox() {
        // LevelRenderer performs this frustum test before it calls the block
        // entity renderer. The default one-block box would cull the entire
        // beam as soon as the emitter itself left the camera frustum.
        return new AABB(worldPosition).inflate(LaserPath.MAX_RENDER_RANGE + 1.0D);
    }

    public void setLaserType(LaserType laserType) {
        if (this.laserType == laserType) {
            return;
        }
        this.laserType = laserType;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("LaserType", laserType.name());
        tag.putBoolean("PulseMode", pulseMode);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        laserType = LaserType.fromName(tag.getString("LaserType"));
        pulseMode = tag.getBoolean("PulseMode");
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
}
