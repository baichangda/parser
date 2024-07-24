package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_0802 extends Evt_2_6 {
    @F_bit_num(len = 15,  valExpr = "x*0.015625")
    public float VehSpdAvgDrvn;
    @F_bit_num(len = 1)
    public byte VehSpdAvgDrvnV;
}
