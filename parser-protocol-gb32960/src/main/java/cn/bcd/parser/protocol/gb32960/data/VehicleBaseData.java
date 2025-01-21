package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.*;

/**
 * 整车数据
 */
public class VehicleBaseData {
    //车辆状态
    @F_num(type = NumType.uint8)
    public NumVal_byte vehicleStatus;

    //充电状态
    @F_num(type = NumType.uint8)
    public NumVal_byte chargeStatus;

    //运行模式
    @F_num(type = NumType.uint8)
    public NumVal_byte runMode;

    //车速
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float vehicleSpeed;

    //累计里程
    @F_num(type = NumType.uint32, valExpr = "x/10")
    public NumVal_double totalMileage;

    //总电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float totalVoltage;

    //总电流
    @F_num(type = NumType.uint16, valExpr = "x/10-1000")
    public NumVal_float totalCurrent;

    //soc
    @F_num(type = NumType.uint8)
    public NumVal_byte soc;

    //DC-DC状态
    @F_num(type = NumType.uint8)
    public NumVal_byte dcStatus;

    //档位
    @F_num(type = NumType.uint8)
    public short gearPosition;

    //绝缘电阻
    @F_num(type = NumType.uint16)
    public NumVal_int resistance;

    //加速踏板行程值
    @F_num(type = NumType.uint8)
    public NumVal_byte pedalVal;

    //制动踏板状态
    @F_num(type = NumType.uint8)
    public NumVal_byte pedalStatus;
}
