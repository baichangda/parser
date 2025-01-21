package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class TerminalUpgradeResResponse implements PacketBody {
    //升级类型
    @F_num(type = NumType.uint8)
    public byte type;
    //升级结果
    @F_num(type = NumType.uint8)
    public byte res;
}
