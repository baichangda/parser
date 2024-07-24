package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.builder.FieldBuilder__F_date_bytes_6;
import cn.bcd.parser.base.util.DateUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SetPath implements PacketBody {
    //路线id
    public long id;
    //路线属性
    public short attr;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //路线拐点总数
    public int num;
    //拐点项
    public CornerItem[] items;
    //名称长度
    public int nameLen;
    //路线名称
    public String name;

    public static SetPath read(ByteBuf data) {
        SetPath setPath = new SetPath();
        setPath.id = data.readUnsignedInt();
        short attr = data.readShort();
        setPath.attr = attr;
        if ((attr & 0x01) != 0) {
            setPath.startTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
            setPath.endTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateUtil.ZONE_OFFSET, 2000));
        }
        int num = data.readUnsignedShort();
        setPath.num = num;
        CornerItem[] items = new CornerItem[num];
        setPath.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = CornerItem.read(data);
        }
        setPath.nameLen = data.readUnsignedShort();
        setPath.name = data.readCharSequence(setPath.nameLen, StandardCharsets.UTF_8).toString();
        return setPath;
    }

    public void write(ByteBuf data) {
        data.writeInt((int) id);
        data.writeShort(attr);
        if ((attr & 0x01) != 0) {
            FieldBuilder__F_date_bytes_6.write(data, startTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
            FieldBuilder__F_date_bytes_6.write(data, endTime.getTime(), DateUtil.ZONE_OFFSET, 2000);
        }
        data.writeShort(num);
        for (CornerItem item : items) {
            item.write(data);
        }
        data.writeShort(nameLen);
        data.writeCharSequence(name, StandardCharsets.UTF_8);
    }
}
