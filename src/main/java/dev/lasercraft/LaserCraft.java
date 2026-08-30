package dev.lasercraft;

import dev.lasercraft.config.ServerConfig;
import dev.lasercraft.registry.CreativeTabPopulator;
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
        if (event.getTabKey() != CreativeModeTabs.REDSTONE_BLOCKS) {
            return;
        }
        CreativeTabPopulator.acceptAll(event,
                ModItems.LASER_EMITTER,
                ModItems.LASER_RECEIVER,
                ModItems.LASER_MIRROR,
                ModItems.LASER_PRISM,
                ModItems.LASER_COMBINER,
                ModItems.LASER_TURRET,
                ModItems.LASER_DIVIDER,
                ModItems.LASER_AMPLIFIER,
                ModItems.LASER_POLARIZER,
                ModItems.FOCUSING_LENS,
                ModItems.RANGE_LENS,
                ModItems.COLOR_DETECTOR,
                ModItems.COLOR_CRYSTAL);
    }
}
