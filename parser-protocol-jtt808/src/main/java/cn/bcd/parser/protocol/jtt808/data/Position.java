package cn.bcd.parser.protocol.jtt808.data;


import io.netty.buffer.ByteBuf;

public class Position implements PacketBody {
    //位置基础数据
    public PositionBase base;
    //位置附加数据
    public PositionExt[] exts;

    public static Position read(ByteBuf data, int len) {
        Position position = new Position();
        position.base = PositionBase.read(data);
        int byteLen = len - 28;
        int readerIndex = data.readerIndex();
        int index = 0;
        PositionExt[] temp = new PositionExt[128];
        while (byteLen > data.readerIndex() - readerIndex) {
            temp[index++] = PositionExt.read(data);
        }
        PositionExt[] exts = new PositionExt[index];
        System.arraycopy(temp, 0, exts, 0, index);
        position.exts = exts;
        return position;
    }

    public void write(ByteBuf data) {
        base.write(data);
        for (PositionExt ext : exts) {
            ext.write(data);
        }
    }
}
