package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_bean_list;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;
import io.netty.buffer.ByteBuf;

public class PositionDataUpload implements PacketBody {
    //数据项个数
    @F_num(type = NumType.uint16, var = 'n')
    public int num;
    //位置数据类型
    @F_num(type = NumType.uint8)
    public byte type;
    //位置汇报数据项
    @F_bean_list(listLenExpr = "n")
    public PositionReportItem[] items;

    public static PositionDataUpload read(ByteBuf data) {
        PositionDataUpload positionDataUpload = new PositionDataUpload();
        int num = data.readUnsignedShort();
        positionDataUpload.num = num;
        positionDataUpload.type = data.readByte();
        PositionReportItem[] items = new PositionReportItem[num];
        positionDataUpload.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = PositionReportItem.read(data);
        }
        return positionDataUpload;
    }

    public void write(ByteBuf data) {
        data.writeShort(num);
        data.writeByte(type);
        for (PositionReportItem item : items) {
            item.write(data);
        }
    }
}
