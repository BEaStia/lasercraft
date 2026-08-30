package dev.lasercraft.registry;

import net.minecraft.world.item.Items;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreativeTabPopulatorTest {
    @Test
    void missingItemDoesNotProduceAStack() {
        assertTrue(CreativeTabPopulator.stackOrEmpty(null).isEmpty());
    }

    @Test
    void airDoesNotProduceAStack() {
        assertTrue(CreativeTabPopulator.stackOrEmpty(Items.AIR).isEmpty());
    }
}
