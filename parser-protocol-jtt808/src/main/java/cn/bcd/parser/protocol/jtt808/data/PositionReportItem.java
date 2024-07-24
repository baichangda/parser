package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class PositionReportItem {
    //位置汇报数据体长度
    public int len;
    //位置汇报数据体
    public Position position;

    public static PositionReportItem read(ByteBuf data) {
        PositionReportItem positionReportItem = new PositionReportItem();
        int len = data.readUnsignedShort();
        positionReportItem.len = len;
        positionReportItem.position = Position.read(data, len);
        return positionReportItem;
    }

    public void write(ByteBuf data){
        data.writeShort(len);
        position.write(data);
    }
}
