package cn.bcd.parser.impl.jtt808.data;

import io.netty.buffer.ByteBuf;

public class StorageMultiMediaDataFetchResponse implements PacketBody{
    //应答流水号
    public int sn;
    //多媒体数据总项数
    public int num;
    //多媒体id
    public long id;
    //多媒体类型
    public byte type;
    //通道id
    public short channelId;
    //事件项编码
    public byte code;
    //位置信息汇报
    public Position position;

    public static StorageMultiMediaDataFetchResponse read(ByteBuf data,int len){
        StorageMultiMediaDataFetchResponse storageMultiMediaDataFetchResponse=new StorageMultiMediaDataFetchResponse();
        storageMultiMediaDataFetchResponse.sn=data.readUnsignedShort();
        storageMultiMediaDataFetchResponse.num=data.readUnsignedShort();
        storageMultiMediaDataFetchResponse.id=data.readUnsignedInt();
        storageMultiMediaDataFetchResponse.type=data.readByte();
        storageMultiMediaDataFetchResponse.channelId=data.readUnsignedByte();
        storageMultiMediaDataFetchResponse.code=data.readByte();
        storageMultiMediaDataFetchResponse.position=Position.read(data,len-11);
        return storageMultiMediaDataFetchResponse;
    }

    public void write(ByteBuf data){
        data.writeShort(sn);
        data.writeShort(num);
        data.writeInt((int) id);
        data.writeByte(type);
        data.writeByte(channelId);
        data.writeByte(code);
        position.write(data);
    }
}

