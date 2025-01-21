package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_skip;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_0801 extends Evt_2_6 {
    @F_skip(lenBefore = 5)
    @F_num(type = NumType.uint8,  valExpr = "x*0.392157")
    public float BrkPdlPos;
}
