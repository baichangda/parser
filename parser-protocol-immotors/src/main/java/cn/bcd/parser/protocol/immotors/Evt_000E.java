package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_000E extends Evt_2_6 {
    @F_bit_num(len = 5)
    public byte BMSChrgSts;
    @F_bit_num(len = 10,valExpr = "x*0.1")
    public float BMSPackSOCBkup;
    @F_bit_num(len = 1)
    public byte BMSPackSOCVBkup;
    @F_num(type = NumType.uint8)
    public byte BMSOfbdChrgSpRsn;
    @F_num(type = NumType.uint8)
    public byte BMSWrlsChrgSpRsn;
}
