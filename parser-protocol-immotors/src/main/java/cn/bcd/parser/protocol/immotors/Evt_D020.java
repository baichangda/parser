package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_D020 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCinfomationRWSL;
    @F_num(type = NumType.uint56)
    public long DTCInfomationESS;
}
