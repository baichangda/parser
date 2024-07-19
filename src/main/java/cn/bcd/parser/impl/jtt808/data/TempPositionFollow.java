package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.NumType;

public class TempPositionFollow implements PacketBody {
    //时间间隔
    @F_num(type = NumType.uint16)
    public int interval;
    //位置跟踪有效期
    @F_num(type = NumType.uint32)
    public long valid;
}
