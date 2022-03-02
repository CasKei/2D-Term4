# 2D-50.002

## Problem

Ripple carry adders are slow.
They cascade full adders.
So for n-bits, we need n full adders.
To optimise this, there are many possible designs.

## Carry select

Do 2 additions:

- Assume Cin = 0
- Assume Cin = 1

Then use MUX to select when the Cin is known

$$O(\sqrt{n})$$

Where top 16bits driven by Cout of the bottom 16bits.

## Carry lookahead

Recall a FA:

```
**** FA circuit ******************
.subckt FA a b cin s co
Xs1 a b s1 xor2
Xs2 s1 cin s xor2
Xc1 a b co1 nand2
Xc2 a cin co2 nand2
Xc3 b cin co3 nand2
Xc4 co1 co2 co3 co nand3
.ends
**********************************
```

This works with

$$
S = A \oplus B \oplus C_{in}\\
C_{out} =  (A + B)\cdot C_{in}+ A \cdot B
$$

See $C_{out}$:

$$

C_{i+1} = (A_i + B_i)C_{in} + A_i\cdot B_i = P_i \cdot C_i + G_i\\


$$

> **_Carry generate_**: $G_i = A_i \cdot B_i$ is true if a carry is generated.

> **_Carry propagate_**: $P_i = (A_i + B_i)$ is true if the carry is propagated by FA from $C_{in}$ to $C_{out}$.

NOTE sometimes $P_{i} = (A_i \oplus B_i)$ to allow us to express $S$ as a simpler function

$$
S = P \oplus C_{in}
$$
