package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_000A extends Evt_2_6 {
    @F_num(type = NumType.int8)
    public byte cellSignalStrength;
    @F_bit_num(len = 3)
    public byte cellRAT;
    @F_bit_num(len = 3)
    public byte cellRATadd;
    @F_bit_num(len = 9)
    public short cellChanID;
    @F_bit_num(len = 8)
    public short GNSSSATS;
}
