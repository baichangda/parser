package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.NumType;

public class QueryAreaOrPathRequest implements PacketBody {
    //查询类型
    @F_num(type = NumType.uint8)
    public byte type;
    //要查询的区域或线路的id数量
    @F_num(type = NumType.uint32, var = 'n')
    public long num;
    //id
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
