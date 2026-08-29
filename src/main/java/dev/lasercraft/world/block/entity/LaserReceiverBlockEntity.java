package dev.lasercraft.world.block.entity;

import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.world.block.LaserReceiverBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class LaserReceiverBlockEntity extends BlockEntity {
    private long lastHitTime = -1000L;

    public LaserReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LASER_RECEIVER.get(), pos, state);
    }

    public void markHit() {
        if (level == null || level.isClientSide) {
            return;
        }
        lastHitTime = level.getGameTime();
        BlockState state = getBlockState();
        if (!state.getValue(LaserReceiverBlock.POWERED)) {
            level.setBlock(worldPosition, state.setValue(LaserReceiverBlock.POWERED, true), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, state.getBlock());
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LaserReceiverBlockEntity receiver) {
        if (!level.isClientSide && state.getValue(LaserReceiverBlock.POWERED)
                && level.getGameTime() - receiver.lastHitTime > 3L) {
            level.setBlock(pos, state.setValue(LaserReceiverBlock.POWERED, false), Block.UPDATE_ALL);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }
}
