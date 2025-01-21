package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;
import cn.bcd.parser.base.anno.data.NumVal_float;
import cn.bcd.parser.base.anno.data.NumVal_short;

/**
 * 极值数据
 */
public class VehicleLimitValueData {
    //最高电压电池子系统号
    @F_num(type = NumType.uint8)
    public NumVal_short maxVoltageSystemNo;

    //最高电压电池单体代号
    @F_num(type = NumType.uint8)
    public NumVal_short maxVoltageCode;

    //电池单体电压最高值
    @F_num(type = NumType.uint16,  valExpr = "x/1000")
    public NumVal_float maxVoltage;

    //最低电压电池子系统号
    @F_num(type = NumType.uint8)
    public NumVal_short minVoltageSystemNo;

    //最低电压电池单体代号
    @F_num(type = NumType.uint8)
    public NumVal_short minVoltageCode;

    //电池单体电压最低值
    @F_num(type = NumType.uint16,  valExpr = "x/1000")
    public NumVal_float minVoltage;

    //最高温度子系统号
    @F_num(type = NumType.uint8)
    public NumVal_short maxTemperatureSystemNo;

    //最高温度探针序号
    @F_num(type = NumType.uint8)
    public NumVal_short maxTemperatureNo;

    //最高温度值
    @F_num(type = NumType.uint8,  valExpr = "x-40")
    public NumVal_short maxTemperature;

    //最低温度子系统号
    @F_num(type = NumType.uint8)
    public NumVal_short minTemperatureSystemNo;

    //最低温度探针序号
    @F_num(type = NumType.uint8)
    public NumVal_short minTemperatureNo;

    //最低温度值
    @F_num(type = NumType.uint8,  valExpr = "x-40")
    public NumVal_short minTemperature;
}
