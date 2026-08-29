package dev.lasercraft.world.laser;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;

public enum LaserType {
    WHITE(0xFFFDF5), ORANGE(0xFF8A18), MAGENTA(0xE83EE8), LIGHT_BLUE(0x55CFFF),
    YELLOW(0xFFF13B), LIME(0x75FF3B), PINK(0xFF74B9), GRAY(0x666666),
    LIGHT_GRAY(0xBDBDBD), CYAN(0x20D7D7), PURPLE(0xA44DFF), BLUE(0x245BFF),
    BROWN(0x8A512A), GREEN(0x25A83B), RED(0xFF1818), BLACK(0x2B183B);

    private final int rgb;

    LaserType(int rgb) {
        this.rgb = rgb;
    }

    public int rgb() {
        return rgb;
    }

    public static LaserType fromDye(DyeColor dyeColor) {
        return valueOf(dyeColor.name());
    }

    public static LaserType fromName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return RED;
        }
    }

    public void applyEffect(LivingEntity entity) {
        applyEffect(entity, 1);
    }

    public void applyEffect(LivingEntity entity, int strength) {
        int effectAmplifier = Math.max(0, strength - 1);
        switch (this) {
            case RED -> {
                entity.hurt(entity.damageSources().magic(), 2.0F * strength);
                entity.setSecondsOnFire(2 * strength);
            }
            case ORANGE -> {
                entity.hurt(entity.damageSources().magic(), 1.0F * strength);
                entity.setSecondsOnFire(4 * strength);
            }
            case YELLOW, WHITE -> add(entity, MobEffects.GLOWING, 40 * strength, effectAmplifier);
            case GREEN -> add(entity, MobEffects.POISON, 60, effectAmplifier);
            case LIME, PINK -> add(entity, MobEffects.REGENERATION, 40, effectAmplifier);
            case BLUE, LIGHT_BLUE -> add(entity, MobEffects.MOVEMENT_SLOWDOWN, 40, strength);
            case PURPLE -> add(entity, MobEffects.LEVITATION, 20, effectAmplifier);
            case MAGENTA -> add(entity, MobEffects.CONFUSION, 80 * strength, effectAmplifier);
            case CYAN -> add(entity, MobEffects.DIG_SPEED, 40, effectAmplifier);
            case BROWN -> add(entity, MobEffects.HUNGER, 60, effectAmplifier);
            case GRAY, LIGHT_GRAY -> add(entity, MobEffects.WEAKNESS, 60, effectAmplifier);
            case BLACK -> add(entity, MobEffects.WITHER, 40, effectAmplifier);
        }
    }

    private static void add(LivingEntity entity, net.minecraft.world.effect.MobEffect effect, int duration, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, true));
    }
}
