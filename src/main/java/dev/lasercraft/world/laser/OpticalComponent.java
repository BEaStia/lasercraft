package dev.lasercraft.world.laser;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface OpticalComponent {
    boolean matches(BlockState state);

    List<BeamBranch> transform(BlockState state, Direction incomingDirection,
                               LaserBeam beam, int remainingRange);
}
