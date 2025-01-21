package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_D016 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationLHCMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRHCMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRLSM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRRSM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationPMA;
    @F_num(type = NumType.uint56)
    public long DTCInfomationLVBM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIMU;
}
