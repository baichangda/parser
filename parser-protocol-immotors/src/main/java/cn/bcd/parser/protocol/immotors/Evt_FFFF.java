package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_FFFF extends Evt_2_6 {
    @F_bit_num(len = 48)
    public long EvtCRC;
}
