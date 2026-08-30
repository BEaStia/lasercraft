package dev.lasercraft;

import dev.lasercraft.config.ServerConfig;
import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.registry.ModBlocks;
import dev.lasercraft.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LaserCraft.MOD_ID)
public final class LaserCraft {
    public static final String MOD_ID = "lasercraft";

    public LaserCraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        modBus.addListener(this::addCreativeTabItems);
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModItems.LASER_EMITTER);
            event.accept(ModItems.LASER_RECEIVER);
            event.accept(ModItems.LASER_MIRROR);
            event.accept(ModItems.LASER_PRISM);
            event.accept(ModItems.LASER_COMBINER);
            event.accept(ModItems.LASER_TURRET);
            event.accept(ModItems.LASER_DIVIDER);
            event.accept(ModItems.LASER_AMPLIFIER);
            event.accept(ModItems.LASER_POLARIZER);
            event.accept(ModItems.FOCUSING_LENS);
            event.accept(ModItems.RANGE_LENS);
            event.accept(ModItems.COLOR_DETECTOR);
            event.accept(ModItems.COLOR_CRYSTAL);
        }
    }
}
