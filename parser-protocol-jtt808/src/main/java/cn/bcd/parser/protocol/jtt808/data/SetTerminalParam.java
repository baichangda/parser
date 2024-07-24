package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_bean_list;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class SetTerminalParam implements PacketBody {
    //参数总数
    @F_num(type = NumType.uint8, var = 'n')
    public short total;
    //参数项列表
    @F_bean_list(listLenExpr = "n")
    public TerminalParamItem[] items;
}
