package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;

public class Evt_D006 extends Evt_4_x {
    @F_bit_num(len = 1)
    public byte EPTRdy;
    @F_bit_num(len = 5)
    public byte BMSBscSta;
    @F_bit_num(len = 16,  valExpr = "x*0.05-1000")
    public float BMSPackCrnt;
    @F_bit_num(len = 1)
    public byte BMSPackCrntV;
    @F_bit_num(len = 10,  valExpr = "x*0.1")
    public float BMSPackSOC;
    @F_bit_num(len = 1)
    public byte BMSPackSOCV;
    @F_bit_num(len = 10,  valExpr = "x*0.1")
    public float BMSPackSOCDsp;
    @F_bit_num(len = 1)
    public byte BMSPackSOCDspV;
    @F_bit_num(len = 4)
    public byte ElecVehSysMd;
    @F_bit_num(len = 12,  valExpr = "x*0.25")
    public float BMSPackVol;
    @F_bit_num(len = 1)
    public byte BMSPackVolV;
    @F_bit_num(len = 3)
    public byte HVDCDCSta;
    @F_bit_num(len = 12,  valExpr = "x*0.5-848")
    public float EPTTrInptShaftToq;
    @F_bit_num(len = 1)
    public byte EPTTrInptShaftToqV;
    @F_bit_num(len = 12,  valExpr = "x*2-3392")
    public short EPTTrOtptShaftToq;
    @F_bit_num(len = 1)
    public byte EPTTrOtptShaftToqV;
    @F_bit_num(len = 1)
    public byte EPTBrkPdlDscrtInptSts;
    @F_bit_num(len = 1)
    public byte EPTBrkPdlDscrtInptStsV;
    @F_bit_num(len = 1)
    public byte BrkSysBrkLghtsReqd;
    @F_bit_num(len = 1)
    public byte EPBSysBrkLghtsReqd;
    @F_bit_num(len = 1)
    public byte EPBSysBrkLghtsReqdA;
    @F_bit_num(len = 14,  valExpr = "x*0.5")
    public float BMSPtIsltnRstc;
    @F_bit_num(len = 8,  valExpr = "x*0.392157")
    public float EPTAccelActuPos;
    @F_bit_num(len = 1)
    public byte EPTAccelActuPosV;
    @F_bit_num(len = 1)
    public byte TMInvtrCrntV;
    @F_bit_num(len = 11,  valExpr = "x-1024")
    public short TMInvtrCrnt;
    @F_bit_num(len = 1)
    public byte ISGInvtrCrntV;
    @F_bit_num(len = 11,  valExpr = "x-1024")
    public short ISGInvtrCrnt;
    @F_bit_num(len = 11,  valExpr = "x-1024")
    public short SAMInvtrCrnt;
    @F_bit_num(len = 1)
    public byte SAMInvtrCrntV;
    @F_bit_num(len = 4)
    public byte TMSta;
    @F_bit_num(len = 4)
    public byte ISGSta;
    @F_bit_num(len = 4)
    public byte SAMSta;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short TMInvtrTem;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short ISGInvtrTem;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short SAMInvtrTem;
    @F_bit_num(len = 16,  valExpr = "x-32768")
    public int TMSpd;
    @F_bit_num(len = 1)
    public byte TMSpdV;
    @F_bit_num(len = 16,  valExpr = "x-32768")
    public int ISGSpd;
    @F_bit_num(len = 1)
    public byte ISGSpdV;
    @F_bit_num(len = 1)
    public byte SAMSpdV;
    @F_bit_num(len = 16,  valExpr = "x-32768")
    public int SAMSpd;
    @F_bit_num(len = 11,  valExpr = "x*0.5-512")
    public float TMActuToq;
    @F_bit_num(len = 1)
    public byte TMActuToqV;
    @F_bit_num(len = 11,  valExpr = "x*0.5-512")
    public float ISGActuToq;
    @F_bit_num(len = 1)
    public byte ISGActuToqV;
    @F_bit_num(len = 1)
    public byte SAMActuToqV;
    @F_bit_num(len = 11,  valExpr = "x*0.5-512")
    public float SAMActuToq;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short TMSttrTem;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short ISGSttrTem;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short SAMSttrTem;
    @F_bit_num(len = 10)
    public short HVDCDCHVSideVol;
    @F_bit_num(len = 1)
    public byte HVDCDCHVSideVolV;
    @F_bit_num(len = 8,  valExpr = "x*0.1")
    public float AvgFuelCsump;
    @F_bit_num(len = 1)
    public byte TMInvtrVolV;

    @F_bit_num(len = 10)
    public short TMInvtrVol;
    @F_bit_num(len = 1)
    public byte ISGInvtrVolV;
    @F_bit_num(len = 10)
    public short ISGInvtrVol;
    @F_bit_num(len = 1)
    public byte SAMInvtrVolV;
    @F_bit_num(len = 10)
    public short SAMInvtrVol;
    @F_bit_num(len = 8)
    public short BMSCellMaxTemIndx;
    @F_bit_num(len = 8,  valExpr = "x*0.5-40")
    public float BMSCellMaxTem;
    @F_bit_num(len = 1)
    public byte BMSCellMaxTemV;
    @F_bit_num(len = 8)
    public short BMSCellMinTemIndx;

    @F_bit_num(len = 8,  valExpr = "x*0.5-40")
    public float BMSCellMinTem;
    @F_bit_num(len = 1)
    public byte BMSCellMinTemV;
    @F_bit_num(len = 8)
    public short BMSCellMaxVolIndx;
    @F_bit_num(len = 13,  valExpr = "x*0.001")
    public float BMSCellMaxVol;
    @F_bit_num(len = 1)
    public byte BMSCellMaxVolV;
    @F_bit_num(len = 8)
    public short BMSCellMinVolIndx;
    @F_bit_num(len = 13,  valExpr = "x*0.001")
    public float BMSCellMinVol;
    @F_bit_num(len = 1)
    public byte BMSCellMinVolV;
    @F_bit_num(len = 1)
    public byte BMSPtIsltnRstcV;
    @F_bit_num(len = 8,  valExpr = "x-40")
    public short HVDCDCTem;
    @F_bit_num(len = 1)
    public byte BrkFludLvlLow;
    @F_bit_num(len = 1)
    public byte BrkSysRedBrkTlltReq;
    @F_bit_num(len = 1)
    public byte ABSF;
    @F_bit_num(len = 3)
    public byte VSESts;
    @F_bit_num(len = 1)
    public byte IbstrWrnngIO;
    @F_bit_num(len = 1)
    public byte BMSHVILClsd;
    @F_bit_num(len = 12,  valExpr = "x*0.5-848")
    public float EPTTrOtptShaftTotToq;
    @F_bit_num(len = 1)
    public byte EPTTrOtptShaftTotToqV;
    @F_bit_num(len = 1)
    public byte BrkFludLvlLowV;
    @F_bit_num(len = 16,  valExpr = "x*0.25")
    public float EnSpd;
    @F_bit_num(len = 2)
    public byte EnSpdSts;
    @F_bit_num(len = 12, valExpr = "x*16")
    public int FuelCsump;

}
