package dev.lasercraft.world.laser;

import net.minecraft.core.Direction;

/**
 * Recasts a beam through a color crystal: same path, stored color, preserved strength.
 */
public final class ColorCrystalOptics {
    public static BeamBranch recast(Direction incoming, LaserBeam beam, LaserType crystalType, int remainingRange) {
        return new BeamBranch(incoming, beam.withColor(crystalType).branch(remainingRange));
    }

    private ColorCrystalOptics() {
    }
}
