package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_bean;
import cn.bcd.parser.base.anno.F_customize;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;
import cn.bcd.parser.protocol.jtt808.processor.PacketBodyProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packet {
    static Logger logger = LoggerFactory.getLogger(Packet.class);
    //标识位 0x7e
    @F_num(type = NumType.uint8)
    public byte startFlag;
    //消息头
    @F_bean
    public PacketHeader header;
    //消息体
    @F_customize(processorClass = PacketBodyProcessor.class)
    public PacketBody body;
    //校验码
    @F_num(type = NumType.uint8)
    public byte code;
    //标识位 0x7e
    @F_num(type = NumType.uint8)
    public byte endFlag;

    public static ByteBuf xorAndEscape(ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        ByteBuf res = Unpooled.buffer();
        res.writeByte(0x7e);
        byte xor = 0;
        for (int i = 1; i < readableBytes - 2; i++) {
            byte b = byteBuf.getByte(i);
            switch (b) {
                case 0x7d -> {
                    res.writeByte(0x7d);
                    res.writeByte(0x01);
                }
                case 0x7e -> {
                    res.writeByte(0x7d);
                    res.writeByte(0x02);
                }
                default -> res.writeByte(b);
            }
            xor ^= b;
        }
        switch (xor) {
            case 0x7d -> {
                res.writeByte(0x7d);
                res.writeByte(0x01);
            }
            case 0x7e -> {
                res.writeByte(0x7d);
                res.writeByte(0x02);
            }
            default -> res.writeByte(xor);
        }
        res.writeByte(0x7e);
        return res;
    }

    public static ByteBuf unEscapeAndXor(ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        ByteBuf res = Unpooled.buffer();
        res.writeByte(0x7e);
        byte xor = 0;
        for (int i = 1; i < readableBytes - 2; i++) {
            byte b = byteBuf.getByte(i);
            if (b == 0x7d) {
                i += 1;
                byte next = byteBuf.getByte(i);
                switch (next) {
                    case 0x01 -> {
                    }
                    case 0x02 -> b = 0x7e;
                    default -> {
                        logger.error("deEscape error,hex[{}] index[{}] byte[{}] not support", ByteBufUtil.hexDump(byteBuf), i, next);
                        return null;
                    }
                }
            }
            res.writeByte(b);
            xor ^= b;
        }
        if (xor == byteBuf.getByte(readableBytes - 2)) {
            res.writeByte(xor);
            res.writeByte(0x7e);
            return res;
        } else {
            logger.error("xor error,hex[{}]", ByteBufUtil.hexDump(byteBuf));
            return null;
        }
    }

}
