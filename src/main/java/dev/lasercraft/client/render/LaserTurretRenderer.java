package dev.lasercraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lasercraft.world.block.LaserTurretBlock;
import dev.lasercraft.world.block.entity.LaserTurretBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public final class LaserTurretRenderer implements BlockEntityRenderer<LaserTurretBlockEntity> {
    public LaserTurretRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LaserTurretBlockEntity turret, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (turret.getLevel() == null || !turret.getBlockState().getValue(LaserTurretBlock.POWERED)) {
            return;
        }
        LaserEmitterRenderer.renderDirectBeam(turret.getBlockPos(), turret.getBeamEnd(),
                turret.getLaserType(), poseStack, buffers);
    }

    @Override
    public boolean shouldRenderOffScreen(LaserTurretBlockEntity turret) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}
