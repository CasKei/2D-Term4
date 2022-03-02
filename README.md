# 2D-Term4

---

# 50.002 Part1

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

`O(√n)`

Where top 16bits driven by Cout of the bottom 16bits.

Can split more and form some massive tree to make it `O(log n)`.
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

```haskell
G = A⋅B
P = A ⊕ B
```

So

```haskell
Cout = G + P⋅Cin
```

For the lowest bit, substituting, we get

```haskell
Cout = A⋅B + (A ⊕ B)⋅Cin
```

The second bit onwards is the mad bit.
It will have `Cout=1` if \
`G=1` OR \
`P=1` and lowest bit `G=1` OR\
`P=1` and lowest bit `P=1` and `Cin=1`.

```haskell
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

```haskell
S = A ⊕ B ⊕ Cin
  = P ⊕ Cin
```

So the sum for any column is just a `XOR` of the `Cin` and the `P` that we already computed for our `Cout`.

## KS

Another pair of geniuses found that you can combine `G` and `P` before they are used.

If you combine 2 columns, then as a whole they may generate or propagate a carry.\
If

- left `G` OR
- left `P` and right `G`

then the combined 2 column unit will have `G` (generate a carry).

The combined unit will `P` if both columns are `P`.

```haskell
Gunit = G1 + P1⋅G0
Punit = P1⋅P0
```

So we have this thing to do this combining operation

```haskell
.subckt KSPG ph gh pl gl pout gout
Xpo ph pl pout and
Xgo1 ph gl phgl and
Xgo2 phgl gh gout or
.ends
```

Ay we in loggy territory~

But there's a lot of wires because we need to compute the combined `P` and `G` for each column, not just the final one.\
These combined `P` and `G` represent the combined value for each set of columns all the way to the right so they can be used to compute the `Cout` for each column from the original `Cin`.

```haskell
Cn = G_{n - combined} + P_{n - combined}⋅Cin
```

Sum bit can still be computed with a `XOR` using the original `P` and the carry bit to its right.

```haskell
Sn = Pn ⊕ C_{n-1}
```

aight.

---

# 50.002 Part2

1. Select one output bit `> 8`
2. From gate netlist, derive the Boolean expression in CNF

## CEC

> Combinational Equivalence Checking: a technique used to chck if 2 combinational circuit designs implement the same Boolean functions.

## SAT

One technique of CEC.

> Given 2 outputs `ya` and `yb` of circuits `A` and `B`,\
> `ya = yb` if `ya ⊕ yb` is UNSAT.

To use SAT solver, convert circuit netlist into CNF.\
Translation procss from boolean to CNF:

1. Write boolean eqns in terms of 2 input gates
2. From these eqns, write implications
3. Replace implications by disjunctions.

So we need to make .bc with the boolean eqns.

[.bc doc of-sorts](http://users.ics.aalto.fi/tjunttil/circuits/)
