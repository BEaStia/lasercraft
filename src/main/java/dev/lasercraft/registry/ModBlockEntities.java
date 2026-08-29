package dev.lasercraft.registry;

import dev.lasercraft.LaserCraft;
import dev.lasercraft.world.block.entity.LaserEmitterBlockEntity;
import dev.lasercraft.world.block.entity.ColorDetectorBlockEntity;
import dev.lasercraft.world.block.entity.LaserCombinerBlockEntity;
import dev.lasercraft.world.block.entity.LaserReceiverBlockEntity;
import dev.lasercraft.world.block.entity.LaserTurretBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LaserCraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<LaserEmitterBlockEntity>> LASER_EMITTER =
            BLOCK_ENTITIES.register("laser_emitter", () -> BlockEntityType.Builder
                    .of(LaserEmitterBlockEntity::new, ModBlocks.LASER_EMITTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserReceiverBlockEntity>> LASER_RECEIVER =
            BLOCK_ENTITIES.register("laser_receiver", () -> BlockEntityType.Builder
                    .of(LaserReceiverBlockEntity::new, ModBlocks.LASER_RECEIVER.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserCombinerBlockEntity>> LASER_COMBINER =
            BLOCK_ENTITIES.register("laser_combiner", () -> BlockEntityType.Builder
                    .of(LaserCombinerBlockEntity::new, ModBlocks.LASER_COMBINER.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserTurretBlockEntity>> LASER_TURRET =
            BLOCK_ENTITIES.register("laser_turret", () -> BlockEntityType.Builder
                    .of(LaserTurretBlockEntity::new, ModBlocks.LASER_TURRET.get()).build(null));
    public static final RegistryObject<BlockEntityType<ColorDetectorBlockEntity>> COLOR_DETECTOR =
            BLOCK_ENTITIES.register("color_detector", () -> BlockEntityType.Builder
                    .of(ColorDetectorBlockEntity::new, ModBlocks.COLOR_DETECTOR.get()).build(null));

    private ModBlockEntities() {
    }
}
