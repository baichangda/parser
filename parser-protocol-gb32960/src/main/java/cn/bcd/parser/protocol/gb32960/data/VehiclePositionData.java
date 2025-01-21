package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

/**
 * 车辆位置数据
 */
public class VehiclePositionData {
    //定位状态
    @F_num(type = NumType.uint8)
    public byte status;

    //经度
    @F_num(type = NumType.uint32,  valExpr = "x/1000000")
    public double lng;

    //纬度
    @F_num(type = NumType.uint32,  valExpr = "x/1000000")
    public double lat;
}
