package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.NumType;
import cn.bcd.parser.base.anno.data.NumVal_float;
import cn.bcd.parser.base.anno.data.NumVal_int;

/**
 * 每个可充电储能子系统电压数据格式
 */
public class StorageVoltageData {
    //可充电储能子系统号
    @F_num(type = NumType.uint8)
    public short no;

    //可充电储能装置电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float voltage;

    //可充电储能状态电流
    @F_num(type = NumType.uint16, valExpr = "x/10-1000")
    public NumVal_float current;

    //单体电池总数
    @F_num(type = NumType.uint16)
    public NumVal_int total;

    //本帧起始电池序号
    @F_num(type = NumType.uint16)
    public int frameNo;

    //本帧单体电池总数
    @F_num(type = NumType.uint8, var = 'm')
    public short frameTotal;

    //单体电池电压
    @F_num_array(singleType = NumType.uint16, lenExpr = "m", singleValExpr = "x/1000")
    public NumVal_float[] singleVoltage;
}
