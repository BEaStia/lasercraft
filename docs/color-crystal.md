# Color Crystal

The Color Crystal is a pass-through optic that recasts a laser to a stored dye color.

## Placement and dyeing

Crafted crystals are white. Right-click a placed crystal with any vanilla dye to set its color. The dye is consumed unless the player is in creative mode.

Breaking a crystal keeps its color on the dropped item (`copy_state` on `color`). Middle-click pick-block also copies the color.

## Beam behavior

Any incoming direction is accepted. The beam continues the same way with:

- the crystal's stored color
- the same strength
- the remaining range after this block

White crystals bleach the beam to white until they are dyed. This is the mid-path counterpart to dyeing an emitter or turret: once a beam is in the network, a crystal can still change its color and therefore its combat or world effect.

## Appearance

The block uses vanilla textures only (white stained glass plus an amethyst base). Client tinting uses the same RGB values as the laser beam, so the crystal color matches the outgoing beam.

## Recipe

```
 A
GAG
 A
```

`A` = amethyst shard, `G` = glass.
