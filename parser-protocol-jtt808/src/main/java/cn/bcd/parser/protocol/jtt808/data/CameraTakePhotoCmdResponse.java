package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.NumType;

public class CameraTakePhotoCmdResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //拍摄命令
    @F_num(type = NumType.uint8)
    public byte res;
    //id个数
    @F_num(type = NumType.uint16, var = 'n')
    public int num;
    //id列表
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
