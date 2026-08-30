# Creative tabs

LaserCraft items appear in the vanilla Redstone Blocks tab.

On a dedicated server, Forge rebuilds those tabs when you join and when you open the creative inventory. That can unbind `RegistryObject` holders. Calling `RegistryObject.get()` in that moment crashed 0.5.1 (`LaserCraft.addCreativeTabItems`).

0.5.2 skips missing items and resolves from the live item registry instead of assuming every holder is bound.
