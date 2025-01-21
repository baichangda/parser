package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_string;
import cn.bcd.parser.base.anno.data.NumType;

public class Evt_D01C extends Evt_4_x {
    @F_string(len = 18)
    public String IPAddress4;
    @F_string(len = 17)
    public String CurIMSI;
    @F_num(type = NumType.uint8)
    public short NetType;
    @F_num(type = NumType.uint16)
    public int rssi;
    @F_num(type = NumType.uint16)
    public int rsrp;
    @F_num(type = NumType.uint16)
    public int rscp;
    @F_num(type = NumType.uint16)
    public int sinr;
    @F_num(type = NumType.uint16)
    public int ecio;
}
