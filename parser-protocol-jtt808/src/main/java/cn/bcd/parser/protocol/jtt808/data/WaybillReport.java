package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.NumType;

public class WaybillReport implements PacketBody{
    @F_num(type = NumType.uint32, var = 'n')
    public long len;
    @F_num_array(singleType = NumType.uint8, lenExpr = "n")
    public byte[] content;
}
