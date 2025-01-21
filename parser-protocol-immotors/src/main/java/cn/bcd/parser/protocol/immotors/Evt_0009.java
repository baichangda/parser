package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_0009 extends Evt_2_6 {
    @F_num(type = NumType.uint16)
    public int cellLAC;
    @F_num(type = NumType.uint32)
    public long CellID;
}
