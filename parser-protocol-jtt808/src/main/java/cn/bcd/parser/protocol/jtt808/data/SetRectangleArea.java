package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class SetRectangleArea implements PacketBody {
    //设置属性
    public byte attr;
    //区域总数
    public short num;
    //区域项
    public RectangleAreaItem[] items;

    public static SetRectangleArea read(ByteBuf data) {
        SetRectangleArea setRectangleArea = new SetRectangleArea();
        setRectangleArea.attr = data.readByte();
        short num = data.readUnsignedByte();
        setRectangleArea.num = num;
        RectangleAreaItem[] items = new RectangleAreaItem[num];
        setRectangleArea.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = RectangleAreaItem.read(data);
        }
        return setRectangleArea;
    }

    public void write(ByteBuf data) {
        data.writeByte(attr);
        data.writeByte(num);
        for (RectangleAreaItem item : items) {
            item.write(data);
        }
    }
}
