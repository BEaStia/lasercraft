package dev.lasercraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ServerConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue BASE_RANGE;
    public static final ForgeConfigSpec.IntValue MAX_RANGE;
    public static final ForgeConfigSpec.IntValue MAX_BRANCHES;
    public static final ForgeConfigSpec.IntValue MAX_STRENGTH;
    public static final ForgeConfigSpec.DoubleValue TURRET_RANGE;
    public static final ForgeConfigSpec.BooleanValue ENTITY_EFFECTS;
    public static final ForgeConfigSpec.BooleanValue WORLD_INTERACTIONS;
    public static final ForgeConfigSpec.BooleanValue TNT_INTERACTION;
    public static final ForgeConfigSpec.BooleanValue BLOCK_BREAKING;
    public static final ForgeConfigSpec.BooleanValue CROP_GROWTH;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("laser");
        BASE_RANGE = builder.comment("Base emitter range in blocks.")
                .defineInRange("baseRange", 32, 4, 128);
        MAX_RANGE = builder.comment("Hard range limit after lenses.")
                .defineInRange("maxRange", 96, 8, 256);
        MAX_BRANCHES = builder.comment("Maximum recursive beam branches per source trace.")
                .defineInRange("maxBranches", 64, 1, 512);
        MAX_STRENGTH = builder.comment("Maximum amplifier strength.")
                .defineInRange("maxStrength", 3, 1, 10);
        ENTITY_EFFECTS = builder.comment("Allow laser damage and status effects.")
                .define("entityEffects", true);
        builder.pop();

        builder.push("turret");
        TURRET_RANGE = builder.comment("Turret target acquisition range.")
                .defineInRange("range", 32.0D, 4.0D, 128.0D);
        builder.pop();

        builder.push("worldInteractions");
        WORLD_INTERACTIONS = builder.define("enabled", true);
        TNT_INTERACTION = builder.define("igniteTnt", true);
        BLOCK_BREAKING = builder.define("breakFragileBlocks", true);
        CROP_GROWTH = builder.define("growCrops", true);
        builder.pop();

        SPEC = builder.build();
    }

    private ServerConfig() {
    }
}
