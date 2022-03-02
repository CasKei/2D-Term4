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

For a 1 bit adder: use `G` to represent if it will generate a carry by itself, and `P` if it propagates a carry if the carry in is `1`.

$$
G=A \cdot B\\
P = A \oplus B
$$

So

$$
C_{out} = G + P \cdot C_{in}
$$

For the lowest bit, substituting, we get

$$
C_{out} = A \cdot B + (A \oplus B) \cdot C_{in}
$$

The second bit onwards is the mad bit.
It will have `Cout=1` if \
`G=1` OR \
`P=1` and lowest bit `G=1` OR\
`P=1` and lowest bit `P=1` and `Cin=1`.

```math
C0 = G0 + P0⋅Cin
C1 = G1 + P1⋅G0 + P1⋅P0⋅Cin
C2 = G2 + P2⋅G1 + P2⋅P1⋅G0 + P2⋅P1⋅P0⋅Cin
...
```

## Parallel

The series of `Cout` can go on.\
If we compute a `G` amd `P` for each column, then we can compute the carry bit for column `N` by making an `OR` gate with `N+2` inputs, each is a `G` and some `P`s, with the last `AND` gate having `N+1` inputs.\
Can compute each carry bit in 3 gate delays. (2 bit adder)

Sum bit is

```
S = A ⊕ B ⊕ Cin
  = P ⊕ Cin
```

So the sum for any column is just a `XOR` of the `Cin` and the `P` that we already computed for our `Cout`.

## KS

Another
