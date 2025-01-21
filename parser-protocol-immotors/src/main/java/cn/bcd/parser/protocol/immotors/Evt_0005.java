package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_0005 extends Evt_2_6 {
    @F_bit_num(len = 28, unsigned = false, valExpr = "x*0.000001")
    public double Latitude;
    @F_bit_num(skipBefore = 2,len = 2)
    public byte VehTyp;
    @F_num(type = NumType.uint16, valExpr = "x*0.01")
    public float GNSSDirection;
}
