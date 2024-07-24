package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_D019 extends Evt_4_x {
    @F_bit_num(len = 1)
    public byte BCMAvlbly;
    @F_bit_num(len = 1)
    public byte CCUAvlbly;
    @F_bit_num(len = 1)
    public byte EnrgSplReqEPTRdy;
    @F_bit_num(len = 8, valExpr = "x*0.125")
    public float HVDCDCLVSideVol;
    @F_bit_num(len = 16, valExpr = "x*0.03125-1024")
    public float BatCrnt;
    @F_bit_num(len = 8, valExpr = "x*0.4")
    public float BatSOC;
    @F_bit_num(len = 2)
    public byte BatSOCSts;
    @F_bit_num(len = 14, valExpr = "x*0.00097656+3")
    public float BatVol;
    @F_bit_num(len = 1)
    public byte EnrgSplReq;
    @F_bit_num(len = 64)
    public long EnrgSplReqScene;
    @F_bit_num(len = 3)
    public byte VehEnrgRdyLvl;
    @F_bit_num(len = 1)
    public byte VehEnrgRdyLvlV;
    @F_bit_num(len = 2)
    public byte HVEstbCond;
}
