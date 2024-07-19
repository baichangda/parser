package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

public class ServerSubPacketRequest implements PacketBody {
    //原始消息流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //重传包总数
    @F_num(type = NumType.uint16, var = 'n')
    public int total;
    //重传包id列表
    @F_num_array(singleType = NumType.uint16, lenExpr = "n")
    public int[] ids;
}
