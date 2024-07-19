package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

/**
 * 每个可充电储能子系统上温度数据格式
 */
public class StorageTemperatureData {
    //可充电储能子系统号
    @F_num(type = NumType.uint8)
    public short no;

    //可充电储能温度探针个数
    @F_num(type = NumType.uint16, var = 'n')
    public int num;

    //可充电储能子系统各温度探针检测到的温度值
    @F_num_array(lenExpr = "n", singleValExpr = "x-40", singleType = NumType.uint8)
    public short[] temperatures;
}
