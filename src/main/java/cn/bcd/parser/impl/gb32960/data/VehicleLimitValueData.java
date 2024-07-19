package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.NumType;

/**
 * 极值数据
 */
public class VehicleLimitValueData {
    //最高电压电池子系统号
    @F_num(type = NumType.uint8)
    public short maxVoltageSystemNo;

    //最高电压电池单体代号
    @F_num(type = NumType.uint8)
    public short maxVoltageCode;

    //电池单体电压最高值
    @F_num(type = NumType.uint16,  valExpr = "x/1000")
    public float maxVoltage;

    //最低电压电池子系统号
    @F_num(type = NumType.uint8)
    public short minVoltageSystemNo;

    //最低电压电池单体代号
    @F_num(type = NumType.uint8)
    public short minVoltageCode;

    //电池单体电压最低值
    @F_num(type = NumType.uint16,  valExpr = "x/1000")
    public float minVoltage;

    //最高温度子系统号
    @F_num(type = NumType.uint8)
    public short maxTemperatureSystemNo;

    //最高温度探针序号
    @F_num(type = NumType.uint8)
    public short maxTemperatureNo;

    //最高温度值
    @F_num(type = NumType.uint8,  valExpr = "x-40")
    public short maxTemperature;

    //最低温度子系统号
    @F_num(type = NumType.uint8)
    public short minTemperatureSystemNo;

    //最低温度探针序号
    @F_num(type = NumType.uint8)
    public short minTemperatureNo;

    //最低温度值
    @F_num(type = NumType.uint8,  valExpr = "x-40")
    public short minTemperature;
}
