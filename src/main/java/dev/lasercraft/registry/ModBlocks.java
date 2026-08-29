package dev.lasercraft.registry;

import dev.lasercraft.LaserCraft;
import dev.lasercraft.world.block.LaserEmitterBlock;
import dev.lasercraft.world.block.ColorCrystalBlock;
import dev.lasercraft.world.block.ColorDetectorBlock;
import dev.lasercraft.world.block.LaserCombinerBlock;
import dev.lasercraft.world.block.LaserMirrorBlock;
import dev.lasercraft.world.block.LaserPrismBlock;
import dev.lasercraft.world.block.LaserReceiverBlock;
import dev.lasercraft.world.block.LaserTurretBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LaserCraft.MOD_ID);

    public static final RegistryObject<Block> LASER_EMITTER = BLOCKS.register("laser_emitter",
            () -> new LaserEmitterBlock(machineProperties()));
    public static final RegistryObject<Block> LASER_RECEIVER = BLOCKS.register("laser_receiver",
            () -> new LaserReceiverBlock(machineProperties()));
    public static final RegistryObject<Block> LASER_MIRROR = BLOCKS.register("laser_mirror",
            () -> new LaserMirrorBlock(machineProperties()));
    public static final RegistryObject<Block> LASER_PRISM = BLOCKS.register("laser_prism",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> LASER_COMBINER = BLOCKS.register("laser_combiner",
            () -> new LaserCombinerBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> LASER_TURRET = BLOCKS.register("laser_turret",
            () -> new LaserTurretBlock(machineProperties()));
    public static final RegistryObject<Block> LASER_DIVIDER = BLOCKS.register("laser_divider",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> LASER_AMPLIFIER = BLOCKS.register("laser_amplifier",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> LASER_POLARIZER = BLOCKS.register("laser_polarizer",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> FOCUSING_LENS = BLOCKS.register("focusing_lens",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> RANGE_LENS = BLOCKS.register("range_lens",
            () -> new LaserPrismBlock(machineProperties().noOcclusion()));
    public static final RegistryObject<Block> COLOR_DETECTOR = BLOCKS.register("color_detector",
            () -> new ColorDetectorBlock(machineProperties()));
    public static final RegistryObject<Block> COLOR_CRYSTAL = BLOCKS.register("color_crystal",
            () -> new ColorCrystalBlock(machineProperties().noOcclusion()));

    private static BlockBehaviour.Properties machineProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F).requiresCorrectToolForDrops();
    }

    private ModBlocks() {
    }
}
