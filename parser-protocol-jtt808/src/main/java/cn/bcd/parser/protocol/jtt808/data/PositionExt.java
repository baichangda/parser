package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class PositionExt {
    //附加信息id
    public short id;
    //附加信息长度
    public short len;
    //附加信息
    public byte[] data;

    public static PositionExt read(ByteBuf data) {
        PositionExt positionExt = new PositionExt();
        positionExt.id = data.readUnsignedByte();
        short tempLen = data.readUnsignedByte();
        positionExt.len = tempLen;
        byte[] temp = new byte[tempLen];
        data.readBytes(temp);
        positionExt.data = temp;
        return positionExt;
    }

    public void write(ByteBuf data){
        data.writeByte(id);
        data.writeByte(len);
        data.writeBytes(this.data);
    }
}
