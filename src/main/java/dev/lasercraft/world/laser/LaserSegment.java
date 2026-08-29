package dev.lasercraft.world.laser;

import net.minecraft.world.phys.Vec3;

public record LaserSegment(Vec3 start, Vec3 end, LaserType type) {
}
