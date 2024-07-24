package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class Evt_000D extends Evt_2_6 {
    @F_num(type = NumType.uint32)
    public long CellFrequency;
}
