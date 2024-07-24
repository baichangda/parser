package cn.bcd.parser.impl.jtt808.data;

import io.netty.buffer.ByteBuf;

public class DataUpStream implements PacketBody {
    //透传消息类型
    public short type;
    //透传消息内容
    public byte[] data;

    public static DataUpStream read(ByteBuf data, int len) {
        DataUpStream dataUpStream = new DataUpStream();
        dataUpStream.type = data.readUnsignedByte();
        byte[] bytes = new byte[len - 1];
        data.readBytes(bytes);
        dataUpStream.data = bytes;
        return dataUpStream;
    }

    public void write(ByteBuf data){
        data.writeByte(type);
        data.writeBytes(this.data);
    }
}
