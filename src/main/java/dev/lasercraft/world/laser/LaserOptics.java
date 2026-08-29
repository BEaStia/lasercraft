package dev.lasercraft.world.laser;

import dev.lasercraft.config.ServerConfig;
import dev.lasercraft.registry.ModBlocks;
import dev.lasercraft.world.block.LaserMirrorBlock;
import dev.lasercraft.world.block.LaserPrismBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class LaserOptics {
    private static final List<OpticalComponent> COMPONENTS = List.of(
            component(state -> state.getBlock() instanceof LaserMirrorBlock,
                    (state, incoming, beam, range) -> {
                        Direction reflected = state.getValue(LaserMirrorBlock.FACING);
                        return reflected.getAxis() == incoming.getAxis()
                                ? List.of()
                                : List.of(new BeamBranch(reflected, beam.branch(range)));
                    }),
            component(state -> state.is(ModBlocks.LASER_PRISM.get()),
                    (state, incoming, beam, range) -> {
                        Direction forward = state.getValue(LaserPrismBlock.FACING);
                        return List.of(
                                new BeamBranch(forward, beam.withColor(LaserType.RED).branch(range)),
                                new BeamBranch(forward.getCounterClockWise(),
                                        beam.withColor(LaserType.GREEN).branch(range)),
                                new BeamBranch(forward.getClockWise(),
                                        beam.withColor(LaserType.BLUE).branch(range))
                        );
                    }),
            component(state -> state.is(ModBlocks.LASER_DIVIDER.get()),
                    (state, incoming, beam, range) -> {
                        Direction forward = state.getValue(LaserPrismBlock.FACING);
                        return List.of(
                                new BeamBranch(forward, beam.branch(range)),
                                new BeamBranch(forward.getClockWise(), beam.branch(range))
                        );
                    }),
            component(state -> state.is(ModBlocks.LASER_AMPLIFIER.get()),
                    (state, incoming, beam, range) -> List.of(new BeamBranch(
                            state.getValue(LaserPrismBlock.FACING),
                            beam.withRemainingRange(range).amplify(ServerConfig.MAX_STRENGTH.get()).branch(range)))),
            component(state -> state.is(ModBlocks.LASER_POLARIZER.get()),
                    (state, incoming, beam, range) -> {
                        Direction allowed = state.getValue(LaserPrismBlock.FACING);
                        return incoming == allowed
                                ? List.of(new BeamBranch(allowed, beam.branch(range)))
                                : List.of();
                    }),
            component(state -> state.is(ModBlocks.FOCUSING_LENS.get()),
                    (state, incoming, beam, range) -> {
                        int focusedRange = Math.max(1, range / 2);
                        return List.of(new BeamBranch(state.getValue(LaserPrismBlock.FACING),
                                beam.focus(range, ServerConfig.MAX_STRENGTH.get()).branch(focusedRange)));
                    }),
            component(state -> state.is(ModBlocks.RANGE_LENS.get()),
                    (state, incoming, beam, range) -> {
                        int extendedRange = Math.min(ServerConfig.MAX_RANGE.get(), range + 32);
                        return List.of(new BeamBranch(state.getValue(LaserPrismBlock.FACING),
                                beam.extend(range, 32, ServerConfig.MAX_RANGE.get()).branch(extendedRange)));
                    })
    );

    public static Optional<List<BeamBranch>> transform(BlockState state, Direction incomingDirection,
                                                       LaserBeam beam, int remainingRange) {
        return COMPONENTS.stream()
                .filter(component -> component.matches(state))
                .findFirst()
                .map(component -> component.transform(state, incomingDirection, beam, remainingRange));
    }

    private static OpticalComponent component(Predicate<BlockState> matcher, Transformer transformer) {
        return new OpticalComponent() {
            @Override
            public boolean matches(BlockState state) {
                return matcher.test(state);
            }

            @Override
            public List<BeamBranch> transform(BlockState state, Direction incomingDirection,
                                              LaserBeam beam, int remainingRange) {
                return transformer.transform(state, incomingDirection, beam, remainingRange);
            }
        };
    }

    @FunctionalInterface
    private interface Transformer {
        List<BeamBranch> transform(BlockState state, Direction incomingDirection,
                                   LaserBeam beam, int remainingRange);
    }

    private LaserOptics() {
    }
}
