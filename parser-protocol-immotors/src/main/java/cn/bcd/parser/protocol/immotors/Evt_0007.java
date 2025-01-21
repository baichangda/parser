package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.data.BitRemainingMode;
import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_0007 extends Evt_2_6 {
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceX;
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceY;
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceZ;
}
