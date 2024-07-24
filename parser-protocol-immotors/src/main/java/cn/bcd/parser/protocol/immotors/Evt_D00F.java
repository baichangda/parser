package cn.bcd.parser.protocol.immotors;


import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class Evt_D00F extends Evt_4_x {
    @F_num(type = NumType.uint8)
    public short BMSWrnngInfoCRC;
    @F_num(type = NumType.uint8,  valExpr = "x*0.5-40")
    public float BMSBusbarTempMax;
    @F_bit_num(len = 1)
    public byte BMSPreThrmFltIndBkup;
    @F_bit_num(len = 4)
    public byte BMSWrnngInfoRCBkup;
    @F_bit_num(len = 3)
    public byte BMSBatPrsFlt;
    @F_bit_num(len = 6)
    public byte BMSWrnngInfoBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrm;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmV;
    @F_bit_num(len = 1)
    public byte BMSBatPrsSnsrV;
    @F_bit_num(len = 15,  valExpr = "x*0.05")
    public float BMSBatPrsSnsrValBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsSnsrVBkup;
    @F_bit_num(len = 15,  valExpr = "x*0.05")
    public float BMSBatPrsSnsrVal;
    @F_bit_num(len = 8,  valExpr = "x*0.4")
    public float BMSClntPumpPWMReq;
    @F_bit_num(len = 1)
    public byte BMSPumpPwrOnReq;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmVBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmBkup;
    @F_bit_num(len = 4)
    public byte BMSWrnngInfoCRCBkup;
    @F_bit_num(len = 1)
    public byte VCUBatPrsAlrm;
    @F_bit_num(len = 8,  valExpr = "x*0.5-40")
    public float OtsdAirTemCrVal;
    @F_bit_num(len = 1)
    public byte VCUBatPrsAlrmV;
    @F_bit_num(len = 1)
    public byte OtsdAirTemCrValV;
}
