package cn.bcd.parser.impl.gb32960.data;


import cn.bcd.parser.anno.C_impl;
import cn.bcd.parser.impl.gb32960.processor.VehicleRunDataProcessor;

import java.util.Date;

@C_impl(value = {0x02, 0x03}, processorClass = VehicleRunDataProcessor.class)
public class VehicleRunData implements PacketData {
    //数据采集时间
    public Date collectTime;
    //整车数据
    public VehicleBaseData vehicleBaseData;
    //驱动电机数据
    public VehicleMotorData vehicleMotorData;
    //燃料电池数据
    public VehicleFuelBatteryData vehicleFuelBatteryData;
    //发动机数据
    public VehicleEngineData vehicleEngineData;
    //车辆位置数据
    public VehiclePositionData vehiclePositionData;
    //极值数据
    public VehicleLimitValueData vehicleLimitValueData;
    //报警数据
    public VehicleAlarmData vehicleAlarmData;
    //可充电储能装置电压数据
    public VehicleStorageVoltageData vehicleStorageVoltageData;
    //可充电储能装置温度数据
    public VehicleStorageTemperatureData vehicleStorageTemperatureData;
}
