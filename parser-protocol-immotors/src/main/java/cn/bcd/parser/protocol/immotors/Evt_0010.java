package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_0010 extends Evt_2_6 {
    @F_num(type = NumType.uint16, valExpr = "x*0.1-2000")
    public float SAMActuToqHiPre;
    @F_bit_num(len = 15, valExpr = "x*0.1-1000")
    public float SAMInvtrCrntHiPre;
    @F_bit_num(len = 1)
    public byte SAMInvtrOvTemAlrm;
    @F_bit_num(len = 1)
    public byte SAMOvCrntAlrm;
    @F_bit_num(len = 1)
    public byte SAMOvSpdAlrm;
    @F_bit_num(len = 1)
    public byte SAMStrOvTemAlrm;
    @F_bit_num(len = 1)
    public byte TMInvtrOvTemAlrm;
    @F_bit_num(len = 1)
    public byte TMOvCrntAlrm;
    @F_bit_num(len = 1)
    public byte TMOvSpdAlrm;
    @F_bit_num(len = 1)
    public byte TMStrOvTemAlrm;
    @F_bit_num(len = 4)
    public byte UsgMd;
    @F_bit_num(len = 1)
    public byte UsgMdV;
}
