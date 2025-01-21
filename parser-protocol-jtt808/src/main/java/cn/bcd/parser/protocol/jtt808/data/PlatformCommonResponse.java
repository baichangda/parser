package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class PlatformCommonResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //应答id
    @F_num(type = NumType.uint16)
    public int id;
    @F_num(type = NumType.uint8)
    public byte res;
}
