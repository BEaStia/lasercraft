package dev.lasercraft.registry;

import dev.lasercraft.LaserCraft;
import dev.lasercraft.world.item.LaserBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LaserCraft.MOD_ID);

    public static final RegistryObject<Item> LASER_EMITTER = ITEMS.register("laser_emitter",
            () -> item(ModBlocks.LASER_EMITTER.get(), "laser_emitter"));
    public static final RegistryObject<Item> LASER_RECEIVER = ITEMS.register("laser_receiver",
            () -> item(ModBlocks.LASER_RECEIVER.get(), "laser_receiver"));
    public static final RegistryObject<Item> LASER_MIRROR = ITEMS.register("laser_mirror",
            () -> item(ModBlocks.LASER_MIRROR.get(), "laser_mirror"));
    public static final RegistryObject<Item> LASER_PRISM = ITEMS.register("laser_prism",
            () -> item(ModBlocks.LASER_PRISM.get(), "laser_prism"));
    public static final RegistryObject<Item> LASER_COMBINER = ITEMS.register("laser_combiner",
            () -> item(ModBlocks.LASER_COMBINER.get(), "laser_combiner"));
    public static final RegistryObject<Item> LASER_TURRET = ITEMS.register("laser_turret",
            () -> item(ModBlocks.LASER_TURRET.get(), "laser_turret"));
    public static final RegistryObject<Item> LASER_DIVIDER = ITEMS.register("laser_divider",
            () -> item(ModBlocks.LASER_DIVIDER.get(), "laser_divider"));
    public static final RegistryObject<Item> LASER_AMPLIFIER = ITEMS.register("laser_amplifier",
            () -> item(ModBlocks.LASER_AMPLIFIER.get(), "laser_amplifier"));
    public static final RegistryObject<Item> LASER_POLARIZER = ITEMS.register("laser_polarizer",
            () -> item(ModBlocks.LASER_POLARIZER.get(), "laser_polarizer"));
    public static final RegistryObject<Item> FOCUSING_LENS = ITEMS.register("focusing_lens",
            () -> item(ModBlocks.FOCUSING_LENS.get(), "focusing_lens"));
    public static final RegistryObject<Item> RANGE_LENS = ITEMS.register("range_lens",
            () -> item(ModBlocks.RANGE_LENS.get(), "range_lens"));
    public static final RegistryObject<Item> COLOR_DETECTOR = ITEMS.register("color_detector",
            () -> item(ModBlocks.COLOR_DETECTOR.get(), "color_detector"));

    private static LaserBlockItem item(net.minecraft.world.level.block.Block block, String id) {
        return new LaserBlockItem(block, new Item.Properties(), id);
    }

    private ModItems() {
    }
}
