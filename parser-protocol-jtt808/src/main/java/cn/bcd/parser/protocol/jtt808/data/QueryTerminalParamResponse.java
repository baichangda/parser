package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_bean_list;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class QueryTerminalParamResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //应答参数个数
    @F_num(type = NumType.uint8, var = 'n')
    public short num;
    //参数项列表
    @F_bean_list(listLenExpr = "n")
    public TerminalParamItem[] items;
}
