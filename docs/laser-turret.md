# Laser Turret

The turret does not use redstone. It arms itself when a living hostile mob is in range.

## How to start it

1. Place the turret in the open. Dye is optional and only changes the beam color.
2. Put a hostile mob in front of it (zombie, skeleton, creeper, spider, and so on) within 32 blocks.
3. Keep a clear air path from the turret to the mob's eyes. Solid blocks block the shot.

It will **not** fire at players, animals, villagers, or armor stands. Peaceful difficulty removes hostiles, so the turret stays idle.

Quick test:

```
/summon minecraft:zombie ~ ~ ~3
```

## Notes

The sight ray starts just outside the turret cube. A ray that began at the block center hit the turret itself and never acquired a target.
