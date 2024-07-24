package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class SetCircleArea implements PacketBody {
    //设置属性
    public byte attr;
    //区域总数
    public short num;
    //区域项
    public CircleAreaItem[] items;

    public static SetCircleArea read(ByteBuf data) {
        SetCircleArea setCircleArea = new SetCircleArea();
        setCircleArea.attr = data.readByte();
        short num = data.readUnsignedByte();
        setCircleArea.num = num;
        CircleAreaItem[] items = new CircleAreaItem[num];
        setCircleArea.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = CircleAreaItem.read(data);
        }
        return setCircleArea;
    }

    public void write(ByteBuf data){
        data.writeByte(attr);
        data.writeByte(num);
        for (CircleAreaItem item : items) {
            item.write(data);
        }
    }
}
