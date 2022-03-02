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

Each FA needs to take in the lower-order Cin.\
We can mitigate this with replacing Cin by alternative information that can be used to deduce higher-order Cins, hence eliminating Cout signals.

Generate this alternate information with only the inputs at that bit position, and not outputs computed from other bit positions.

This works with info from adjacent sequences of bit positions and can be aggregated to characterise the carry behaviour of their concatenation.

$$
S = A \oplus B \oplus C_{in}\\
C_{out} =  (A + B)\cdot C_{in}+ A \cdot B
$$

See $C_{out}$:\
We choose these 2 signals:

> **_Carry generate_**: $G_i = A_i \cdot B_i$ 1 if carry is 1.\
> **_Carry propagate_**: $P_i = (A_i \oplus B_i)$ 1 if carry = Cin.

If neither is asserted, carry is 0 independently of Cin.

Hence
$$C_{i+1} = P_i \cdot C_i + G_i$$

$$
S = P \oplus C_{in}
$$

### OK lezgo 1 bit

Inputs: a, b, cin\
Outputs: g, p, s\
g = ab = (a nand b) inv \
p = a xor b

```
.subckt CLA1 a b cin g p s
Xs1 a b p xor2
Xs2 cin p s xor2
Xg_inv a b ginv nand2
Xg ginv g inverter
.ends
```

### 2 bit
