# LaserCraft

LaserCraft 0.5.0 is a Forge mod for Minecraft Java Edition 1.20.1.

## Requirements

- Minecraft 1.20.1
- Forge 47.4.10 or another compatible Forge 47.x build
- Java 17

## Blocks

- **Laser Emitter** points away from the player when placed. Supply redstone power to activate its 32-block beam.
- **Laser Mirror** redirects a beam by 90 degrees toward the face marked as its output. A beam on the same axis stops.
- **Laser Receiver** accepts a beam through its marked front face and outputs redstone power level 15.
- **Splitting Prism** splits any incoming beam into red, green, and blue branches pointing forward, left, and right.
- **Combining Prism** mixes any two or more simultaneous colors arriving from different sides. It emits the nearest of the 16 laser colors through its marked output face, so the mixed beam also receives that color's effect.
- **Laser Turret** automatically attacks the nearest visible hostile mob within 32 blocks, including diagonal targets. Use a dye to select its color.
- **Beam Divider** creates two copies of the incoming color.
- **Laser Amplifier** increases damage and status-effect strength, up to three levels.
- **Polarizer** only passes a beam travelling in its marked direction.
- **Focusing Lens** increases strength but halves the remaining range.
- **Range Lens** adds 32 blocks to the remaining range.
- **Color Detector** emits redstone only for its selected dye color.
- **Color Crystal** recasts any passing beam to its dyed color and continues in the same direction. Crafted crystals start white. See [docs/color-crystal.md](docs/color-crystal.md).

Use any vanilla dye on an emitter, turret, detector, or color crystal to change its laser color. Colors have distinct combat and utility effects.
Use a clock on an emitter to switch between continuous and pulsed operation.

Hot colors melt ice and ignite TNT. Blue colors extinguish fire and freeze lava into obsidian.
Green, lime, and pink beams grow plants. Black beams break leaves and glass.

The beam stops at solid blocks and receivers. Living entities intersecting it take damage and briefly catch fire.

## Build

```sh
./gradlew build
```

The distributable mod is created at `build/libs/lasercraft-0.5.0.jar`.
