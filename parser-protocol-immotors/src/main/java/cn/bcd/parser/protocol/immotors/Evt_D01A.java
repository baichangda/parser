package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_string;
import cn.bcd.parser.base.anno.NumType;

public class Evt_D01A extends Evt_4_x {
    @F_num(type = NumType.uint16)
    public int iEcuSts;
    @F_num(type = NumType.uint8)
    public short iIAMInterSts;
    @F_num(type = NumType.uint8)
    public short iMpuIPTableRuleSts;
    @F_num(type = NumType.uint8)
    public short iModemIPTableRuleSts;
    @F_num(type = NumType.uint8)
    public short iARPRuleSts;
    @F_num(type = NumType.uint16)
    public int iICC2PHYSGMIISts;
    @F_num(type = NumType.uint16)
    public int iMpuRGMIISts;
    @F_num(type = NumType.uint16)
    public int iModemRGMIISts;
    @F_num(type = NumType.uint16)
    public int iSwitchSGMIISts;
    @F_num(type = NumType.uint8)
    public short iUSBConnSts;
    @F_num(type = NumType.uint8)
    public short iIPASts;
    @F_num(type = NumType.uint8)
    public short iAPSts;
    @F_string(len = 28)
    public String networkbackupinfo;
}
