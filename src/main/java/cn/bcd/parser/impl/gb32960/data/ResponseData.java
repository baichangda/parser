package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.C_impl;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

@C_impl(value = Integer.MAX_VALUE)
public class ResponseData implements PacketData {
    @F_num_array(singleType = NumType.uint8,lenExpr = "A")
    public byte[] content;
}
