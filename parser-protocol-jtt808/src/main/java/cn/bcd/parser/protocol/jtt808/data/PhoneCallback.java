package cn.bcd.parser.protocol.jtt808.data;


import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class PhoneCallback implements PacketBody {
    //标志
    public byte flag;
    //电话号码
    public String phoneNumber;

    static final Charset gbk = Charset.forName("GBK");

    public static PhoneCallback read(ByteBuf data, int len) {
        PhoneCallback phoneCallback = new PhoneCallback();
        phoneCallback.flag = data.readByte();
        phoneCallback.phoneNumber = data.readCharSequence(len - 1, gbk).toString();
        return phoneCallback;
    }

    public void write(ByteBuf data) {
        data.writeByte(flag);
        data.writeCharSequence(phoneNumber, gbk);
    }
}
