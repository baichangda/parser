package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

public class QueryTerminalParamRequest implements PacketBody {
    //参数总数
    @F_num(type = NumType.uint8, var = 'n')
    public short total;
    //参数id列表
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
