package cn.bcd.parser.impl.jtt808.data;


import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class TerminalControl implements PacketBody {
    //命令字
    public short flag;
    //命令参数
    public String param;


    static final Charset gbk = Charset.forName("GBK");

    public static TerminalControl read(ByteBuf data, int len) {
        TerminalControl terminalControl = new TerminalControl();
        terminalControl.flag = data.readUnsignedByte();
        terminalControl.param = data.readCharSequence(len - 1, gbk).toString();
        return terminalControl;
    }

    public void write(ByteBuf data) {
        data.writeByte(flag);
        data.writeCharSequence(param, gbk);
    }
}
