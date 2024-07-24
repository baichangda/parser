package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.NumType;

/**
 * 整车数据
 */
public class VehicleBaseData {
    //车辆状态
    @F_num(type = NumType.uint8)
    public short vehicleStatus;

    //充电状态
    @F_num(type = NumType.uint8)
    public short chargeStatus;

    //运行模式
    @F_num(type = NumType.uint8)
    public short runMode;

    //车速
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public float vehicleSpeed;

    //累计里程
    @F_num(type = NumType.uint32, valExpr = "x/10")
    public double totalMileage;

    //总电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public float totalVoltage;

    //总电流
    @F_num(type = NumType.uint16, valExpr = "x/10-1000")
    public float totalCurrent;

    //soc
    @F_num(type = NumType.uint8)
    public short soc;

    //DC-DC状态
    @F_num(type = NumType.uint8)
    public short dcStatus;

    //档位
    @F_num(type = NumType.uint8)
    public short gearPosition;

    //绝缘电阻
    @F_num(type = NumType.uint16)
    public int resistance;

    //加速踏板行程值
    @F_num(type = NumType.uint8)
    public short pedalVal;

    //制动踏板状态
    @F_num(type = NumType.uint8)
    public short pedalStatus;
}
