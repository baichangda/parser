package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.C_impl;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.NumType;

@C_impl(value = C_impl.Default)
public class ResponseData implements PacketData {
    @F_num_array(singleType = NumType.uint8,lenExpr = "A")
    public byte[] content;
}
