package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.NumType;

public class DeletePolygonArea implements PacketBody{
    //区域数
    @F_num(type = NumType.uint8, var = 'n')
    public short num;
    //区域id
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
