package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_D01F extends Evt_4_x {
    @F_num(type = NumType.uint8)
    public short NetRecvRsn;
    @F_num(type = NumType.uint8)
    public short NetRecvActn;
    @F_bit_num(len = 48)
    public long NetRecvActnTimstmp;
    @F_num(type = NumType.uint8)
    public short NetRecvActnCnt;
    @F_num(type = NumType.uint8)
    public short NetRecvActnRst;
    @F_bit_num(len = 48)
    public long NetRecvtime;
}
