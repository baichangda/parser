package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class ConfirmAlarmMsg implements PacketBody {
    //报警消息流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //人工确认报警类型
    @F_num(type = NumType.uint32)
    public int type;
}
