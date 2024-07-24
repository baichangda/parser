package cn.bcd.parser.protocol.jtt808.data;

import io.netty.buffer.ByteBuf;

public class QueryPositionResponse implements PacketBody {
    //应答流水号
    public int sn;

    //位置信息
    public Position position;

    public static QueryPositionResponse read(ByteBuf data, int len) {
        QueryPositionResponse queryPositionResponse = new QueryPositionResponse();
        queryPositionResponse.sn = data.readUnsignedShort();
        queryPositionResponse.position = Position.read(data, len - 2);
        return queryPositionResponse;
    }

    public void write(ByteBuf data){
        data.writeShort(sn);
        position.write(data);
    }
}
