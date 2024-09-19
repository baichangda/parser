package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.anno.*;
import cn.bcd.parser.base.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class Packet {
    //头 0-2
    @F_num_array(len = 2, singleType = NumType.uint8)
    public byte[] header;
    //命令标识 2-3
    @F_num(type = NumType.uint8, var = 'f')
    public PacketFlag flag;
    //应答标识 3-4
    @F_num(type = NumType.uint8)
    public short replyFlag;
    //唯一识别码 4-21
    @F_string(len = 17)
    public String vin;
    //数据单元加密方式 21-22
    @F_num(type = NumType.uint8)
    public short encodeWay;
    //数据单元长度 22-24
    @F_num(type = NumType.uint16, globalVar = 'A')
    public int contentLength;
    @F_bean(implClassExpr = "f")
    public PacketData data;
    //异或校验位
    @F_num(type = NumType.uint8)
    public byte code;


    static final Processor<Packet> processor = Parser.getProcessor(Packet.class);

    public static Packet read(ByteBuf data) {
        return processor.process(data);
    }

    public void write(ByteBuf data) {
        processor.deProcess(data, this);
    }

    /**
     * 转换为{@link ByteBuf}
     *
     * @return
     */
    public ByteBuf toByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        write(byteBuf);
        return byteBuf;
    }

    /**
     * 转换为{@link ByteBuf}
     * 修正数据单元长度
     * 修正异或校验位
     *
     * @return
     */
    public ByteBuf toByteBuf_fix() {
        ByteBuf byteBuf = toByteBuf();
        fix_contentLength(byteBuf);
        fix_code(byteBuf);
        return byteBuf;
    }

    /**
     * 修正数据单元长度
     *
     * @param data 只包含一条数据的数据包
     */
    public static void fix_contentLength(ByteBuf data) {
        int actualLen = data.readableBytes() - 25;
        data.setShort(22, actualLen);
    }

    /**
     * 修正异或校验位
     *
     * @param data 只包含一条数据的数据包
     */
    public static void fix_code(ByteBuf data) {
        byte xor = 0;
        int codeIndex = data.readableBytes() - 1;
        for (int i = 0; i < codeIndex; i++) {
            xor ^= data.getByte(i);
        }
        data.setByte(codeIndex, xor);
    }
}
