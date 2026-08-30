package dev.lasercraft.world.laser;

import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorCrystalOpticsTest {
    @Test
    void recastChangesColorAndKeepsDirectionAndStrength() {
        LaserBeam incoming = new LaserBeam(LaserType.RED, 2, 20, 0);

        BeamBranch result = ColorCrystalOptics.recast(Direction.NORTH, incoming, LaserType.YELLOW, 19);

        assertEquals(Direction.NORTH, result.direction());
        assertEquals(LaserType.YELLOW, result.beam().type());
        assertEquals(2, result.beam().strength());
        assertEquals(19, result.beam().remainingRange());
        assertEquals(1, result.beam().branchDepth());
    }
}
