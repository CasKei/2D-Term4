# 2D-50.002

## Problem

Ripple carry adders are slow.
They cascade full adders.
So for n-bits, we need n full adders.
To optimise this, there are many possible designs.

## Carry select

If we split and try to do parallel: say split 32 bit to 2 16bit adders,

**Problem**: the carry input to the top 16bits is unknow until the bottom 16bits adder has completed its operation.

**Solution**:
Do 2 additions:

- Assume Cin = 0
- Assume Cin = 1

Then use MUX to select when the Cin is known

$$O(\sqrt{n})$$

Where top 16bits driven by Cout of the bottom 16bits.

Can split more and form some massive tree to make it $O(\log{n})$.
But we can do better.

## G and P

Some geniuses saw something about the carry.\
Even if you don't know what a column's carry-in will be yet, you could make some assumptions about what will happen:

| `A` | `B` | `Cout` |
| --- | --- | ------ |
| `0` | `0` | `0`    |
| `0` | `1` | `Cin`  |
| `1` | `0` | `Cin`  |
| `1` | `1` | `1`    |

- **_Kill_**: if both inputs are `0`, carry is sure `0`
- **_Generated_**: if both inputs are `1`, carry is sure `1`
- **_Propagated_**: if only one input is `1`, carry out is `1` iff carry in is `1`
