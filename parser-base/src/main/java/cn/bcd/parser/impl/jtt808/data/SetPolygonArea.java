package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.builder.FieldBuilder__F_date_bytes_6;
import cn.bcd.parser.util.DateUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SetPolygonArea implements PacketBody {
    //区域id
    public long id;
    //区域属性
    public short attr;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //最高速度
    public int speed;
    //超速持续时间
    public short duration;
    //区域总顶点数
    public int num;
    //顶点项
    public PolygonAreaItem[] items;
    //夜间最高速度
    public int nightSpeed;
    //名称长度
    public int nameLen;
    //名称
    public String name;

    public static SetPolygonArea read(ByteBuf data) {
        SetPolygonArea setPolygonArea = new SetPolygonArea();
        setPolygonArea.id = data.readUnsignedInt();
        short attr = data.readShort();
        setPolygonArea.attr = attr;
        if ((attr & 0x01) != 0) {
            setPolygonArea.startTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
            setPolygonArea.endTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
        }
        if (((attr >> 1) & 0x01) != 0) {
            setPolygonArea.speed = data.readUnsignedShort();
            setPolygonArea.duration = data.readUnsignedByte();
        }
        int num = data.readUnsignedShort();
        setPolygonArea.num = num;
        PolygonAreaItem[] items = new PolygonAreaItem[num];
        setPolygonArea.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = PolygonAreaItem.read(data);
        }
        if (((attr >> 1) & 0x01) != 0) {
            setPolygonArea.nightSpeed = data.readUnsignedShort();
        }
        setPolygonArea.nameLen = data.readUnsignedShort();
        setPolygonArea.name = data.readCharSequence(setPolygonArea.nameLen, StandardCharsets.UTF_8).toString();
        return setPolygonArea;
    }

    public void write(ByteBuf data) {
        data.writeInt((int) id);
        data.writeShort(attr);
        if ((attr & 0x01) != 0) {
            FieldBuilder__F_date_bytes_6.write(data, startTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
            FieldBuilder__F_date_bytes_6.write(data, endTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
        }
        data.writeShort(num);
        for (PolygonAreaItem item : items) {
            item.write(data);
        }
        if (((attr >> 1) & 0x01) != 0) {
            data.writeShort(nightSpeed);
        }
        data.writeShort(nameLen);
        data.writeCharSequence(name, StandardCharsets.UTF_8);
    }
}
