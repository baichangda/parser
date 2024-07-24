package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.NumType;

public class Evt_4_x_unknown extends Evt_4_x {
    @F_num_array(lenExpr = "z", singleType = NumType.uint8)
    public byte[] data;
}
