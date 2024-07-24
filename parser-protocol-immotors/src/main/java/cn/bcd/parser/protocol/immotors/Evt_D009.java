package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_D009 extends Evt_4_x {
    @F_bit_num(len = 2)
    public byte BMSCMUFlt;
    @F_bit_num(len = 2)
    public byte BMSCellVoltFlt;
    @F_bit_num(len = 2)
    public byte BMSPackTemFlt;
    @F_bit_num(len = 2)
    public byte BMSPackVoltFlt;
    @F_bit_num(len = 6)
    public byte BMSWrnngInfo;
    @F_bit_num(len = 6)
    public byte BMSWrnngInfoPV;
    @F_bit_num(len = 4)
    public byte BMSWrnngInfoRC;
    @F_bit_num(len = 1,skipAfter = 5)
    public byte BMSPreThrmFltInd;
    @F_bit_num(len = 4)
    public byte BMSKeepSysAwkScene;
    @F_bit_num(len = 3)
    public byte BMSTemOverDifAlrm;
    @F_bit_num(len = 3)
    public byte BMSOverTemAlrm;
    @F_bit_num(len = 3)
    public byte BMSOverPackVolAlrm;
    @F_bit_num(len = 3)
    public byte BMSUnderPackVolAlrm;
    @F_bit_num(len = 3)
    public byte BMSHVILAlrm;
    @F_bit_num(len = 3)
    public byte BMSOverCellVolAlrm;
    @F_bit_num(len = 3)
    public byte BMSUnderCellVolAlrm;
    @F_bit_num(len = 3)
    public byte BMSLowSOCAlrm;
    @F_bit_num(len = 3)
    public byte BMSJumpngSOCAlrm;
    @F_bit_num(len = 3)
    public byte BMSHiSOCAlrm;
    @F_bit_num(len = 3)
    public byte BMSPackVolMsmchAlrm;
    @F_bit_num(len = 3)
    public byte BMSPoorCellCnstncyAlrm;
    @F_bit_num(len = 3)
    public byte BMSCellOverChrgdAlrm;
    @F_bit_num(len = 3)
    public byte BMSLowPtIsltnRstcAlrm;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short TMRtrTem;
    @F_bit_num(len = 3)
    public byte TMStrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte TMInvtrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte ISCStrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte ISCInvtrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte SAMStrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte SAMInvtrOvTempAlrm;
    @F_bit_num(len = 3)
    public byte EPTHVDCDCMdReq;
    @F_bit_num(len = 6)
    public byte VCUSecyWrnngInfo;
    @F_bit_num(len = 6)
    public byte VCUSecyWrnngInfoPV;
    @F_bit_num(len = 4)
    public byte VCUSecyWrnngInfoRC;
    @F_bit_num(len = 8)
    public short VCUSecyWrnngInfoCRC;
    @F_bit_num(len = 8)
    public short BMSOnbdChrgSpRsn;
}
