package cn.bcd.parser.impl.jtt808.data;

import io.netty.buffer.ByteBuf;

public class DrivingRecorderDownStream implements PacketBody {
    //命令字
    public byte flag;
    //数据块
    public byte[] content;

    public static DrivingRecorderDownStream read(ByteBuf data, int len) {
        DrivingRecorderDownStream drivingRecorderDownStream = new DrivingRecorderDownStream();
        drivingRecorderDownStream.flag = data.readByte();
        byte[] content = new byte[len - 1];
        data.readBytes(content);
        drivingRecorderDownStream.content = content;
        return drivingRecorderDownStream;
    }

    public void write(ByteBuf data){
        data.writeByte(flag);
        data.writeBytes(content);
    }
}
