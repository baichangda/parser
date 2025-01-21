package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.NumType;

public class TerminalParamItem {
    //参数id
    @F_num(type = NumType.uint32)
    public long id;
    //参数长度
    @F_num(type = NumType.uint8, var = 'n')
    public short len;
    //参数值
    @F_num_array(singleType = NumType.uint8, lenExpr = "n")
    public byte[] val;
}
