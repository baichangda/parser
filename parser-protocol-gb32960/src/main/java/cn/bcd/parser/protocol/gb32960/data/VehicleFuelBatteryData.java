package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.*;

/**
 * 燃料电池数据
 */
public class VehicleFuelBatteryData {
    //燃料电池电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float voltage;

    //燃料电池电流
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float current;

    //燃料消耗率
    @F_num(type = NumType.uint16, valExpr = "x/100")
    public NumVal_float consumptionRate;

    //燃料电池温度探针总数
    @F_num(type = NumType.uint16, var = 'a')
    public NumVal_int num;

    //探针温度值
    @F_num_array(lenExpr = "a", singleValExpr = "x-40", singleType = NumType.uint8)
    public short[] temperatures;

    //氢系统中最高温度
    @F_num(type = NumType.uint16,  valExpr = "x/10-40")
    public NumVal_float maxTemperature;

    //氢系统中最高温度探针代号
    @F_num(type = NumType.uint8)
    public NumVal_short maxTemperatureCode;

    //氢气最高浓度
    @F_num(type = NumType.uint16,  valExpr = "x-10000")
    public NumVal_int maxConcentration;

    //氢气最高浓度传感器代号
    @F_num(type = NumType.uint8)
    public NumVal_short maxConcentrationCode;

    //氢气最高压力
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public float maxPressure;

    //氢气最高压力传感器代号
    @F_num(type = NumType.uint8)
    public NumVal_short maxPressureCode;

    //高压DC/DC状态
    @F_num(type = NumType.uint8)
    public NumVal_byte dcStatus;

}
