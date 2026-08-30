package dev.lasercraft.world.laser;

import net.minecraft.world.phys.Vec3;

/**
 * Targeting helpers for the laser turret. Sight rays must start outside the
 * turret's own 1x1x1 collision cube; a ray that begins at the block center
 * hits the turret first and never acquires a target.
 */
public final class TurretTargeting {
    private static final double CUBE_HALF = 0.5D;
    private static final double EXIT_EPSILON = 0.02D;

    /**
     * Point just outside the turret cube, on the line from {@code center} to {@code targetEye}.
     */
    public static Vec3 sightStart(Vec3 center, Vec3 targetEye) {
        Vec3 delta = targetEye.subtract(center);
        double length = delta.length();
        if (length < 1.0E-6D) {
            return center;
        }
        Vec3 direction = delta.scale(1.0D / length);
        double maxAxis = Math.max(Math.abs(direction.x), Math.max(Math.abs(direction.y), Math.abs(direction.z)));
        double exitDistance = CUBE_HALF / maxAxis + EXIT_EPSILON;
        if (length <= exitDistance) {
            return targetEye;
        }
        return center.add(direction.scale(exitDistance));
    }

    public static boolean isInsideUnitCube(Vec3 pointRelativeToCubeMin) {
        return pointRelativeToCubeMin.x > 0.0D && pointRelativeToCubeMin.x < 1.0D
                && pointRelativeToCubeMin.y > 0.0D && pointRelativeToCubeMin.y < 1.0D
                && pointRelativeToCubeMin.z > 0.0D && pointRelativeToCubeMin.z < 1.0D;
    }

    private TurretTargeting() {
    }
}
