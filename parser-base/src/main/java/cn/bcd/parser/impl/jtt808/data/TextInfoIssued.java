package cn.bcd.parser.impl.jtt808.data;


import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class TextInfoIssued implements PacketBody {
    //标志
    public byte flag;
    //文本类型
    public byte type;
    //文本信息
    public String info;

    static final Charset gbk = Charset.forName("GBK");
    public static TextInfoIssued read(ByteBuf data, int len){
        TextInfoIssued textInfoIssued = new TextInfoIssued();
        textInfoIssued.flag = data.readByte();
        textInfoIssued.type = data.readByte();
        textInfoIssued.info = data.readCharSequence(len - 2, gbk).toString();
        return textInfoIssued;
    }

    public void write(ByteBuf data){
        data.writeByte(flag);
        data.writeByte(type);
        data.writeCharSequence(info,gbk);
    }
}
