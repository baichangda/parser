package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class Evt_D015 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationICC;
}
