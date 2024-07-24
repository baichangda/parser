package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class CornerItem implements AreaOrPathItem {
    //拐点id
    public long id;
    //路段id
    public long roadId;
    //拐点纬度
    public double lat;
    //拐点经度
    public double lng;
    //路段宽度
    public short width;
    //路段属性
    public byte attr;
    //路段行驶过长阈值
    public int threshold1;
    //路段行驶过长阈值
    public int threshold2;
    //路段最高限速
    public int speed;
    //路段超速持续时间
    public short duration;
    //路段夜间最高速度
    public int nightSpeed;

    public static CornerItem read(ByteBuf data) {
        CornerItem item = new CornerItem();
        item.id = data.readUnsignedInt();
        item.roadId = data.readUnsignedInt();
        item.lat = data.readUnsignedInt() / 1000000d;
        item.lng = data.readUnsignedInt() / 1000000d;
        item.width = data.readUnsignedByte();
        byte attr = data.readByte();
        item.attr = attr;
        if ((attr & 0x01) != 0) {
            item.threshold1 = data.readUnsignedShort();
            item.threshold2 = data.readUnsignedShort();
        }
        if (((attr >> 1) & 0x01) != 0) {
            item.speed = data.readUnsignedShort();
            item.duration = data.readUnsignedByte();
            item.nightSpeed = data.readUnsignedShort();
        }
        return item;
    }

    public void write(ByteBuf data) {
        data.writeInt((int) id);
        data.writeInt((int) roadId);
        data.writeInt((int) (lat * 1000000));
        data.writeInt((int) (lng * 1000000));
        data.writeByte(width);
        data.writeByte(attr);
        if ((attr & 0x01) != 0) {
            data.writeShort(threshold1);
            data.writeShort(threshold2);
        }
        if (((attr >> 1) & 0x01) != 0) {
            data.writeShort(speed);
            data.writeByte(duration);
            data.writeShort(nightSpeed);
        }
    }
}
