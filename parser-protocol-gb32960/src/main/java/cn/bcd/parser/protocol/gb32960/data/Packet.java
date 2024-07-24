package cn.bcd.parser.protocol.gb32960.data;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.anno.*;
import cn.bcd.parser.base.processor.Processor;
import io.netty.buffer.ByteBuf;


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
}
