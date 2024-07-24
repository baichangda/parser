package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.exception.ParseException;
import io.netty.buffer.ByteBuf;

public class VehicleControlRequest implements PacketBody {
    //控制类型数量
    public int num;
    //控制类型
    public VehicleControlType[] types;

    public static VehicleControlRequest read(ByteBuf data) {
        VehicleControlRequest vehicleControlRequest = new VehicleControlRequest();
        int num = data.readUnsignedShort();
        vehicleControlRequest.num = num;
        VehicleControlType[] types = new VehicleControlType[num];
        vehicleControlRequest.types = types;
        for (int i = 0; i < num; i++) {
            int id = data.readUnsignedShort();
            switch (id) {
                case 0x0001 -> {
                    VehicleControlType vehicleControlType = new VehicleControlType();
                    vehicleControlType.id = id;
                    vehicleControlType.param = new byte[]{data.readByte()};
                    types[i] = vehicleControlType;
                }
                default -> {
                    throw ParseException.get("VehicleControlType id[{}] not support", id);
                }
            }
        }
        return vehicleControlRequest;
    }

    public void write(ByteBuf data) {
        data.writeShort(num);
        if (types != null) {
            for (VehicleControlType vehicleControlType : types) {
                data.writeShort(vehicleControlType.id);
                data.writeBytes(vehicleControlType.param);
            }
        }
    }
}
