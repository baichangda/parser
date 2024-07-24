package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class DataDownStream implements PacketBody {
    //透传消息类型
    public short type;
    //透传消息内容
    public byte[] data;

    public static DataDownStream read(ByteBuf data, int len) {
        DataDownStream dataDownStream = new DataDownStream();
        dataDownStream.type = data.readUnsignedByte();
        byte[] bytes = new byte[len - 1];
        data.readBytes(bytes);
        dataDownStream.data = bytes;
        return dataDownStream;
    }

    public void write(ByteBuf data){
        data.writeByte(type);
        data.writeBytes(this.data);
    }
}
