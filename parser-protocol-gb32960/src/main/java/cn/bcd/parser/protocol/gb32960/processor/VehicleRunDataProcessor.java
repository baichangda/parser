package cn.bcd.parser.protocol.gb32960.processor;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.builder.FieldBuilder__F_date_bytes_6;
import cn.bcd.parser.base.processor.ProcessContext;
import cn.bcd.parser.base.processor.Processor;
import cn.bcd.parser.base.util.DateUtil;
import cn.bcd.parser.protocol.gb32960.data.*;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class VehicleRunDataProcessor implements Processor<VehicleRunData> {

    Logger logger= LoggerFactory.getLogger(VehicleRunDataProcessor.class);

    final Processor<VehicleBaseData> processor_vehicleBaseData = Parser.getProcessor(VehicleBaseData.class);
    final Processor<VehicleMotorData> processor_vehicleMotorData = Parser.getProcessor(VehicleMotorData.class);
    final Processor<VehicleFuelBatteryData> processor_vehicleFuelBatteryData = Parser.getProcessor(VehicleFuelBatteryData.class);
    final Processor<VehicleEngineData> processor_vehicleEngineData = Parser.getProcessor(VehicleEngineData.class);
    final Processor<VehiclePositionData> processor_vehiclePositionData = Parser.getProcessor(VehiclePositionData.class);
    final Processor<VehicleLimitValueData> processor_vehicleLimitValueData = Parser.getProcessor(VehicleLimitValueData.class);
    final Processor<VehicleAlarmData> processor_vehicleAlarmData = Parser.getProcessor(VehicleAlarmData.class);
    final Processor<VehicleStorageVoltageData> processor_vehicleStorageVoltageData = Parser.getProcessor(VehicleStorageVoltageData.class);
    final Processor<VehicleStorageTemperatureData> processor_vehicleStorageTemperatureData = Parser.getProcessor(VehicleStorageTemperatureData.class);

    @Override
    public VehicleRunData process(ByteBuf data, ProcessContext<?> processContext) {
        ProcessContext<?> parentContext=new ProcessContext<>(data,processContext);
        VehicleRunData instance = new VehicleRunData();
        instance.collectTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
        final Packet packet = (Packet) processContext.instance;
        int allLen = packet.contentLength - 6;
        int beginLeave = data.readableBytes();
        A:
        while (data.isReadable()) {
            int curLeave = data.readableBytes();
            if (beginLeave - curLeave >= allLen) {
                break;
            }
            short flag = data.readUnsignedByte();
            switch (flag) {
                case 1 -> {
                    //2.1、整车数据
                    instance.vehicleBaseData = processor_vehicleBaseData.process(data, parentContext);
                }
                case 2 -> {
                    //2.2、驱动电机数据
                    instance.vehicleMotorData = processor_vehicleMotorData.process(data, parentContext);
                }
                case 3 -> {
                    //2.3、燃料电池数据
                    instance.vehicleFuelBatteryData = processor_vehicleFuelBatteryData.process(data, parentContext);
                }
                case 4 -> {
                    //2.4、发动机数据
                    instance.vehicleEngineData = processor_vehicleEngineData.process(data, parentContext);
                }
                case 5 -> {
                    //2.5、车辆位置数据
                    instance.vehiclePositionData = processor_vehiclePositionData.process(data, parentContext);
                }
                case 6 -> {
                    //2.6、极值数据
                    instance.vehicleLimitValueData = processor_vehicleLimitValueData.process(data, parentContext);
                }
                case 7 -> {
                    //2.7、报警数据
                    instance.vehicleAlarmData = processor_vehicleAlarmData.process(data, parentContext);
                }
                case 8 -> {
                    //2.8、可充电储能装置电压数据
                    instance.vehicleStorageVoltageData = processor_vehicleStorageVoltageData.process(data, parentContext);
                }
                case 9 -> {
                    //2.9、可充电储能装置温度数据
                    instance.vehicleStorageTemperatureData = processor_vehicleStorageTemperatureData.process(data, parentContext);
                }
                default -> {
                    logger.warn("flag[" + flag + "] not support");
                    //2.8、如果是自定义数据,只做展现,不解析
                    //2.8.1、解析长度
                    int dataLen = data.readUnsignedShort();
                    //2.8.2、获取接下来的报文
                    byte[] content = new byte[dataLen];
                    data.getBytes(0, content);
                    break A;
                }
            }
        }
        return instance;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> processContext, VehicleRunData instance) {
        ProcessContext<?> parentContext=new ProcessContext<>(data,processContext);
        FieldBuilder__F_date_bytes_6.write(data, instance.collectTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
        if (instance.vehicleBaseData != null) {
            data.writeByte(1);
            processor_vehicleBaseData.deProcess(data, parentContext, instance.vehicleBaseData);
        }
        if (instance.vehicleMotorData != null) {
            data.writeByte(2);
            processor_vehicleMotorData.deProcess(data, parentContext, instance.vehicleMotorData);
        }
        if (instance.vehicleFuelBatteryData != null) {
            data.writeByte(3);
            processor_vehicleFuelBatteryData.deProcess(data, parentContext, instance.vehicleFuelBatteryData);
        }
        if (instance.vehicleEngineData != null) {
            data.writeByte(4);
            processor_vehicleEngineData.deProcess(data, parentContext, instance.vehicleEngineData);
        }
        if (instance.vehiclePositionData != null) {
            data.writeByte(5);
            processor_vehiclePositionData.deProcess(data, parentContext, instance.vehiclePositionData);
        }
        if (instance.vehicleLimitValueData != null) {
            data.writeByte(6);
            processor_vehicleLimitValueData.deProcess(data, parentContext, instance.vehicleLimitValueData);
        }
        if (instance.vehicleAlarmData != null) {
            data.writeByte(7);
            processor_vehicleAlarmData.deProcess(data, parentContext, instance.vehicleAlarmData);
        }
        if (instance.vehicleStorageVoltageData != null) {
            data.writeByte(8);
            processor_vehicleStorageVoltageData.deProcess(data, parentContext, instance.vehicleStorageVoltageData);
        }
        if (instance.vehicleStorageTemperatureData != null) {
            data.writeByte(9);
            processor_vehicleStorageTemperatureData.deProcess(data, parentContext, instance.vehicleStorageTemperatureData);
        }
    }
}
