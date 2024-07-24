package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

/**
 * 报警数据
 */
public class VehicleAlarmData {
    //最高报警等级
    @F_num(type = NumType.uint8)
    public short maxAlarmLevel;

    //最高电压电池单体代号
    @F_num(type = NumType.int32)
    public int alarmFlag;

    //可充电储能装置故障总数
    @F_num(type = NumType.uint8, var = 'a')
    public short chargeBadNum;

    //可充电储能装置故障代码列表
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] chargeBadCodes;

    //驱动电机故障总数
    @F_num(type = NumType.uint8, var = 'b')
    public short driverBadNum;

    //驱动电机故障代码列表
    @F_num_array(lenExpr = "b", singleType = NumType.uint32)
    public long[] driverBadCodes;

    //发动机故障总数
    @F_num(type = NumType.uint8, var = 'c')
    public short engineBadNum;

    //发动机故障代码列表
    @F_num_array(lenExpr = "c", singleType = NumType.uint32)
    public long[] engineBadCodes;

    //其他故障总数
    @F_num(type = NumType.uint8, var = 'd')
    public short otherBadNum;

    //其他故障代码列表
    @F_num_array(lenExpr = "d", singleType = NumType.uint32)
    public long[] otherBadCodes;


}
