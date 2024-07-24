package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class PolygonAreaItem implements AreaOrPathItem {
    //顶点纬度
    public double lat;
    //顶点经度
    public double lng;

    public static PolygonAreaItem read(ByteBuf data) {
        PolygonAreaItem item = new PolygonAreaItem();
        item.lat = data.readUnsignedInt() / 1000000d;
        item.lng = data.readUnsignedInt() / 1000000d;
        return item;
    }

    public void write(ByteBuf data) {
        data.writeInt((int) (lat * 1000000));
        data.writeInt((int) (lng * 1000000));
    }
}
