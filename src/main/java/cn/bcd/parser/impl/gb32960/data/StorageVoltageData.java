package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

/**
 * 每个可充电储能子系统电压数据格式
 */
public class StorageVoltageData {
    //可充电储能子系统号
    @F_num(type = NumType.uint8)
    public short no;

    //可充电储能装置电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public float voltage;

    //可充电储能状态电流
    @F_num(type = NumType.uint16, valExpr = "x/10-1000")
    public float current;

    //单体电池总数
    @F_num(type = NumType.uint16)
    public int total;

    //本帧起始电池序号
    @F_num(type = NumType.uint16)
    public int frameNo;

    //本帧单体电池总数
    @F_num(type = NumType.uint8, var = 'm')
    public short frameTotal;

    //单体电池电压
    @F_num_array(singleType = NumType.uint16, lenExpr = "m", singleValExpr = "x/1000")
    public float[] singleVoltage;
}
