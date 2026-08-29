package dev.lasercraft.world.laser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LaserBeamTest {
    @Test
    void amplifierStopsAtConfiguredMaximum() {
        LaserBeam beam = LaserBeam.initial(LaserType.RED, 32)
                .amplify(3)
                .amplify(3)
                .amplify(3);

        assertEquals(3, beam.strength());
        assertEquals(32, beam.remainingRange());
    }

    @Test
    void focusingLensTradesRangeForStrength() {
        LaserBeam beam = LaserBeam.initial(LaserType.BLUE, 32).focus(24, 3);

        assertEquals(2, beam.strength());
        assertEquals(12, beam.remainingRange());
    }

    @Test
    void rangeLensCannotExceedHardLimit() {
        LaserBeam beam = LaserBeam.initial(LaserType.GREEN, 32).extend(80, 32, 96);

        assertEquals(96, beam.remainingRange());
    }

    @Test
    void branchesRetainPropertiesAndIncrementDepth() {
        LaserBeam beam = new LaserBeam(LaserType.PURPLE, 2, 20, 0).branch(15);

        assertEquals(LaserType.PURPLE, beam.type());
        assertEquals(2, beam.strength());
        assertEquals(15, beam.remainingRange());
        assertEquals(1, beam.branchDepth());
    }

    @Test
    void invalidBeamStateIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new LaserBeam(LaserType.RED, 0, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new LaserBeam(LaserType.RED, 1, -1, 0));
    }
}
