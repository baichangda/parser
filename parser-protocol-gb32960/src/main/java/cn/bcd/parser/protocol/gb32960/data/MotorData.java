package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.*;

/**
 * 每个驱动电机数据格式
 */
public class MotorData {
    //驱动电机序号
    @F_num(type = NumType.uint8)
    public short no;

    //驱动电机状态
    @F_num(type = NumType.uint8)
    public NumVal_byte status;

    //驱动电机控制器温度
    @F_num(type = NumType.uint8, valExpr = "x-40")
    public NumVal_short controllerTemperature;

    //驱动电机转速
    @F_num(type = NumType.uint16, valExpr = "x-20000")
    public NumVal_int rotateSpeed;

    //驱动电机转矩
    @F_num(type = NumType.uint16, valExpr = "x/10-2000")
    public NumVal_float rotateRectangle;

    //驱动电机温度
    @F_num(type = NumType.uint8, valExpr = "x-40")
    public NumVal_short temperature;

    //电机控制器输入电压
    @F_num(type = NumType.uint16, valExpr = "x/10")
    public NumVal_float inputVoltage;

    //电机控制器直流母线电流
    @F_num(type = NumType.uint16, valExpr = "x/10-1000")
    public NumVal_float current;
}
