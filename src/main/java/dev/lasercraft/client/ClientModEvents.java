package dev.lasercraft.client;

import dev.lasercraft.LaserCraft;
import dev.lasercraft.client.render.LaserEmitterRenderer;
import dev.lasercraft.client.render.LaserTurretRenderer;
import dev.lasercraft.registry.ModBlockEntities;
import dev.lasercraft.registry.ModBlocks;
import dev.lasercraft.registry.ModItems;
import dev.lasercraft.world.block.ColorCrystalBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LaserCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.LASER_EMITTER.get(), LaserEmitterRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.LASER_TURRET.get(), LaserTurretRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> tintIndex != 0
                ? 0xFFFFFF
                : ColorCrystalBlock.laserType(state).rgb(), ModBlocks.COLOR_CRYSTAL.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex != 0
                ? 0xFFFFFF
                : ColorCrystalBlock.colorFromItem(stack).rgb(), ModItems.COLOR_CRYSTAL.get());
    }

    private ClientModEvents() {
    }
}
