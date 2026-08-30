package dev.lasercraft.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Adds items to creative tabs without crashing when a {@link RegistryObject}
 * is unbound. That happens on the client after joining a dedicated server,
 * when Forge rebuilds tabs and {@code RegistryObject.get()} would throw.
 */
public final class CreativeTabPopulator {
    public static ItemStack stackOrEmpty(Item item) {
        if (item == null || item == Items.AIR) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(item);
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    public static void accept(BuildCreativeModeTabContentsEvent event, RegistryObject<? extends ItemLike> item) {
        Item resolved = item.isPresent()
                ? item.get().asItem()
                : ForgeRegistries.ITEMS.getValue(item.getId());
        ItemStack stack = stackOrEmpty(resolved);
        if (!stack.isEmpty()) {
            event.accept(stack);
        }
    }

    @SafeVarargs
    public static void acceptAll(BuildCreativeModeTabContentsEvent event, RegistryObject<? extends ItemLike>... items) {
        for (RegistryObject<? extends ItemLike> item : items) {
            accept(event, item);
        }
    }

    private CreativeTabPopulator() {
    }
}
