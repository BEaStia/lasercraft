package dev.lasercraft.world.block.entity;

import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.world.block.ColorDetectorBlock;
import dev.lasercraft.world.laser.LaserType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class ColorDetectorBlockEntity extends BlockEntity {
    private LaserType filter = LaserType.RED;
    private long lastHitTime = -1000L;

    public ColorDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COLOR_DETECTOR.get(), pos, state);
    }

    public void markHit(LaserType type) {
        if (level == null || level.isClientSide || type != filter) {
            return;
        }
        lastHitTime = level.getGameTime();
        BlockState state = getBlockState();
        if (!state.getValue(ColorDetectorBlock.POWERED)) {
            level.setBlock(worldPosition, state.setValue(ColorDetectorBlock.POWERED, true), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, state.getBlock());
        }
    }

    public void setFilter(LaserType filter) {
        this.filter = filter;
        setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ColorDetectorBlockEntity detector) {
        if (!level.isClientSide && state.getValue(ColorDetectorBlock.POWERED)
                && level.getGameTime() - detector.lastHitTime > 3L) {
            level.setBlock(pos, state.setValue(ColorDetectorBlock.POWERED, false), Block.UPDATE_ALL);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Filter", filter.name());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        filter = LaserType.fromName(tag.getString("Filter"));
    }
}
