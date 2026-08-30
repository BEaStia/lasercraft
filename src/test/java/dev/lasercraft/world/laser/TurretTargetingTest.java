package dev.lasercraft.world.laser;

import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurretTargetingTest {
    private static final Vec3 CENTER = new Vec3(0.5D, 0.5D, 0.5D);

    @Test
    void sightStartLeavesTheCubeOnAxis() {
        Vec3 start = TurretTargeting.sightStart(CENTER, new Vec3(10.0D, 0.5D, 0.5D));

        assertFalse(TurretTargeting.isInsideUnitCube(start));
        assertTrue(start.x > 1.0D);
    }

    @Test
    void sightStartLeavesTheCubeOnDiagonal() {
        Vec3 start = TurretTargeting.sightStart(CENTER, new Vec3(10.0D, 10.0D, 10.0D));

        assertFalse(TurretTargeting.isInsideUnitCube(start),
                "diagonal offset of 0.51 stays inside the cube and blocks line of sight");
    }

    @Test
    void sightStartLeavesTheCubeForATypicalZombieEye() {
        Vec3 zombieEye = new Vec3(3.5D, 1.74D, 2.5D);
        Vec3 start = TurretTargeting.sightStart(CENTER, zombieEye);

        assertFalse(TurretTargeting.isInsideUnitCube(start));
    }
}
