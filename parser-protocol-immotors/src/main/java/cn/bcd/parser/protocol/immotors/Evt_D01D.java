package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class Evt_D01D extends Evt_4_x {
    @F_num(type = NumType.uint32)
    public long cellLAC5G;
    @F_num(type = NumType.uint64)
    public long CellID5G;
}
