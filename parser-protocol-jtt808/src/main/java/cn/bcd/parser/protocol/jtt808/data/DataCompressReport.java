package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class DataCompressReport implements PacketBody {
    //压缩消息长度
    public int len;
    //压缩消息体
    public byte[] data;

    public static DataCompressReport read(ByteBuf data, int len) {
        DataCompressReport dataCompressReport = new DataCompressReport();
        dataCompressReport.len = data.readInt();
        byte[] bytes = new byte[len - 4];
        data.readBytes(bytes);
        dataCompressReport.data = bytes;
        return dataCompressReport;
    }

    public void write(ByteBuf data) {
        data.writeInt(len);
        data.writeBytes(this.data);
    }
}
