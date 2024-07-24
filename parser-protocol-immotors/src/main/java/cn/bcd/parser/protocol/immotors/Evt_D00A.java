package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_string;

public class Evt_D00A extends Evt_4_x {
    @F_string(len = 17)
    public String VIN;
    @F_string(len = 16)
    public String IAMSN;
    @F_string(len = 20)
    public String EsimIccid;
    @F_string(len = 32)
    public String EsimID;
}
