package dev.lasercraft.world.laser;

import java.util.Objects;

public record LaserBeam(LaserType type, int strength, int remainingRange, int branchDepth) {
    public LaserBeam {
        Objects.requireNonNull(type, "type");
        if (strength < 1) {
            throw new IllegalArgumentException("strength must be positive");
        }
        if (remainingRange < 0) {
            throw new IllegalArgumentException("remainingRange must not be negative");
        }
        if (branchDepth < 0) {
            throw new IllegalArgumentException("branchDepth must not be negative");
        }
    }

    public static LaserBeam initial(LaserType type, int range) {
        return new LaserBeam(type, 1, range, 0);
    }

    public LaserBeam withColor(LaserType newType) {
        return new LaserBeam(newType, strength, remainingRange, branchDepth);
    }

    public LaserBeam withRemainingRange(int range) {
        return new LaserBeam(type, strength, Math.max(0, range), branchDepth);
    }

    public LaserBeam branch(int range) {
        return new LaserBeam(type, strength, Math.max(0, range), branchDepth + 1);
    }

    public LaserBeam amplify(int maximumStrength) {
        return new LaserBeam(type, Math.min(maximumStrength, strength + 1), remainingRange, branchDepth);
    }

    public LaserBeam focus(int range, int maximumStrength) {
        return new LaserBeam(type, Math.min(maximumStrength, strength + 1), Math.max(1, range / 2), branchDepth);
    }

    public LaserBeam extend(int range, int addedRange, int maximumRange) {
        return new LaserBeam(type, strength, Math.min(maximumRange, range + addedRange), branchDepth);
    }
}
