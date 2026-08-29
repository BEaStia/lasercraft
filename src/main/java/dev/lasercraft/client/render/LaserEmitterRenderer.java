package dev.lasercraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.lasercraft.world.block.LaserEmitterBlock;
import dev.lasercraft.world.block.entity.LaserEmitterBlockEntity;
import dev.lasercraft.world.laser.LaserPath;
import dev.lasercraft.world.laser.LaserSegment;
import dev.lasercraft.world.laser.LaserType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class LaserEmitterRenderer implements BlockEntityRenderer<LaserEmitterBlockEntity> {
    private static final float RADIUS = 0.05F;

    public LaserEmitterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LaserEmitterBlockEntity emitter, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (emitter.getLevel() == null || !emitter.getBlockState().getValue(LaserEmitterBlock.POWERED)
                || !emitter.isEmitting()) {
            return;
        }

        BlockPos origin = emitter.getBlockPos();
        renderBeam(emitter.getLevel(), origin, emitter.getBlockState().getValue(LaserEmitterBlock.FACING),
                emitter.getLaserType(), poseStack, buffers);
    }

    public static void renderBeam(Level level, BlockPos origin, Direction direction, LaserType type,
                                  PoseStack poseStack, MultiBufferSource buffers) {
        VertexConsumer vertices = buffers.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();
        for (LaserSegment segment : LaserPath.trace(level, origin, direction, type, false)) {
            Vec3 start = segment.start().subtract(origin.getX(), origin.getY(), origin.getZ());
            Vec3 end = segment.end().subtract(origin.getX(), origin.getY(), origin.getZ());
            drawBeam(vertices, matrix, start, end, segment.type().rgb());
        }
    }

    public static void renderDirectBeam(BlockPos origin, Vec3 absoluteEnd, LaserType type,
                                        PoseStack poseStack, MultiBufferSource buffers) {
        Vec3 center = Vec3.atCenterOf(origin);
        Vec3 delta = absoluteEnd.subtract(center);
        if (delta.lengthSqr() < 0.01D) {
            return;
        }
        Vec3 absoluteStart = center.add(delta.normalize().scale(0.51D));
        Vec3 start = absoluteStart.subtract(origin.getX(), origin.getY(), origin.getZ());
        Vec3 end = absoluteEnd.subtract(origin.getX(), origin.getY(), origin.getZ());
        VertexConsumer vertices = buffers.getBuffer(RenderType.lightning());
        drawBeam(vertices, poseStack.last().pose(), start, end, type.rgb());
    }

    private static void drawBeam(VertexConsumer vertices, Matrix4f matrix, Vec3 start, Vec3 end, int rgb) {
        float minX = (float) Math.min(start.x, end.x) - RADIUS;
        float minY = (float) Math.min(start.y, end.y) - RADIUS;
        float minZ = (float) Math.min(start.z, end.z) - RADIUS;
        float maxX = (float) Math.max(start.x, end.x) + RADIUS;
        float maxY = (float) Math.max(start.y, end.y) + RADIUS;
        float maxZ = (float) Math.max(start.z, end.z) + RADIUS;

        quad(vertices, matrix, rgb, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ);
        quad(vertices, matrix, rgb, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ);
        quad(vertices, matrix, rgb, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ);
        quad(vertices, matrix, rgb, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ);
        quad(vertices, matrix, rgb, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ);
        quad(vertices, matrix, rgb, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ);
    }

    private static void quad(VertexConsumer vertices, Matrix4f matrix, int rgb,
                             float x1, float y1, float z1, float x2, float y2, float z2,
                             float x3, float y3, float z3, float x4, float y4, float z4) {
        // Render both windings. RenderType.lightning() culls back faces, so a
        // single winding makes the beam disappear from some camera angles.
        vertex(vertices, matrix, rgb, x1, y1, z1);
        vertex(vertices, matrix, rgb, x2, y2, z2);
        vertex(vertices, matrix, rgb, x3, y3, z3);
        vertex(vertices, matrix, rgb, x4, y4, z4);
        vertex(vertices, matrix, rgb, x4, y4, z4);
        vertex(vertices, matrix, rgb, x3, y3, z3);
        vertex(vertices, matrix, rgb, x2, y2, z2);
        vertex(vertices, matrix, rgb, x1, y1, z1);
    }

    private static void vertex(VertexConsumer vertices, Matrix4f matrix, int rgb, float x, float y, float z) {
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        vertices.vertex(matrix, x, y, z).color(red, green, blue, 220).endVertex();
    }

    @Override
    public boolean shouldRenderOffScreen(LaserEmitterBlockEntity emitter) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}
