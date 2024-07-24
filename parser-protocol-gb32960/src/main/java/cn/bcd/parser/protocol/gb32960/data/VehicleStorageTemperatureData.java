package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_bean_list;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

/**
 * 可充电储能装置温度数据
 */
public class VehicleStorageTemperatureData {
    //可充电储能子系统个数
    @F_num(type = NumType.uint8, var = 'a')
    public short num;

    //可充电储能子系统温度信息列表
    @F_bean_list(listLenExpr = "a")
    public StorageTemperatureData[] content;
}
