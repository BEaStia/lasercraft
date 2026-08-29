package dev.lasercraft.client;

import dev.lasercraft.LaserCraft;
import dev.lasercraft.client.render.LaserEmitterRenderer;
import dev.lasercraft.client.render.LaserTurretRenderer;
import dev.lasercraft.registry.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LaserCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.LASER_EMITTER.get(), LaserEmitterRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.LASER_TURRET.get(), LaserTurretRenderer::new);
    }

    private ClientModEvents() {
    }
}
