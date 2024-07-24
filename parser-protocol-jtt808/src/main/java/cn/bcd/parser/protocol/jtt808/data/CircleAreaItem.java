package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.builder.FieldBuilder__F_date_bytes_6;
import cn.bcd.parser.base.util.DateUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class CircleAreaItem implements AreaOrPathItem {
    //区域id
    public long id;
    //区域属性
    public short attr;
    //中心点纬度
    public double lat;
    //中心点经度
    public double lng;
    //半径
    public long radius;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //最高速度
    public int speed;
    //超速持续时间
    public short duration;
    //夜间最高速度
    public int nightSpeed;
    //名称长度
    public int nameLen;
    //名称
    public String name;

    public static CircleAreaItem read(ByteBuf data) {
        CircleAreaItem item = new CircleAreaItem();
        item.id = data.readUnsignedInt();
        short attr = data.readShort();
        item.attr = attr;
        item.lat = data.readUnsignedInt() / 1000000d;
        item.lng = data.readUnsignedInt() / 1000000d;
        item.radius = data.readUnsignedInt();
        if ((attr & 0x01) != 0) {
            item.startTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
            item.endTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
        }
        if (((attr >> 1) & 0x01) != 0) {
            item.speed = data.readUnsignedShort();
            item.duration = data.readUnsignedByte();
            item.nightSpeed = data.readUnsignedShort();
        }
        item.nameLen = data.readUnsignedShort();
        item.name = data.readCharSequence(item.nameLen, StandardCharsets.UTF_8).toString();
        return item;
    }

    public void write(ByteBuf data) {
        data.writeInt((int) id);
        data.writeShort(attr);
        data.writeInt((int) (lat * 1000000));
        data.writeInt((int) (lng * 1000000));
        data.writeInt((int) radius);
        if ((attr & 0x01) != 0) {
            FieldBuilder__F_date_bytes_6.write(data, startTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
            FieldBuilder__F_date_bytes_6.write(data, endTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
        }
        if (((attr >> 1) & 0x01) != 0) {
            data.writeShort(speed);
            data.writeByte(duration);
            data.writeShort(nightSpeed);
        }
        data.writeShort(nameLen);
        data.writeCharSequence(name, StandardCharsets.UTF_8);
    }
}
