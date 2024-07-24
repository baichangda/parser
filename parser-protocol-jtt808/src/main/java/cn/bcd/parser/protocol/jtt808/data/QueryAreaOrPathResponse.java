package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.exception.ParseException;
import io.netty.buffer.ByteBuf;

public class QueryAreaOrPathResponse implements PacketBody {
    //查询类型
    public byte type;
    //查询返回的数据数量
    public int num;
    //数据项
    public AreaOrPathItem[] items;

    public static QueryAreaOrPathResponse read(ByteBuf data) {
        QueryAreaOrPathResponse queryAreaOrPathResponse = new QueryAreaOrPathResponse();
        byte type = data.readByte();
        queryAreaOrPathResponse.type = type;
        int num = data.readInt();
        queryAreaOrPathResponse.num = num;
        AreaOrPathItem[] items = new AreaOrPathItem[num];
        queryAreaOrPathResponse.items = items;
        switch (type) {
            case 1 -> {
                for (int i = 0; i < num; i++) {
                    items[i] = CircleAreaItem.read(data);
                }
            }
            case 2 -> {
                for (int i = 0; i < num; i++) {
                    items[i] = RectangleAreaItem.read(data);
                }
            }
            case 3 -> {
                for (int i = 0; i < num; i++) {
                    items[i] = PolygonAreaItem.read(data);
                }
            }
            case 4 -> {
                for (int i = 0; i < num; i++) {
                    items[i] = CornerItem.read(data);
                }
            }
            default -> throw ParseException.get("QueryAreaOrPathResponse type[{}] not support", type);
        }
        return queryAreaOrPathResponse;
    }

    public void write(ByteBuf data){
        data.writeByte(type);
        data.writeInt(num);
        for (AreaOrPathItem item : items) {
            item.write(data);
        }
    }
}
