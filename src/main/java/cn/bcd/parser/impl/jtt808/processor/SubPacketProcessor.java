package cn.bcd.parser.impl.jtt808.processor;

import cn.bcd.parser.exception.ParseException;
import cn.bcd.parser.impl.jtt808.data.PacketHeader;
import cn.bcd.parser.impl.jtt808.data.SubPacket;
import cn.bcd.parser.processor.ProcessContext;
import cn.bcd.parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class SubPacketProcessor implements Processor<SubPacket> {
    @Override
    public SubPacket process(ByteBuf data, ProcessContext<?> processContext) {
        PacketHeader packetHeader = (PacketHeader) processContext.instance;
        if (packetHeader.subPacketFlag == 0) {
            return null;
        } else {
            SubPacket subPacket = new SubPacket();
            subPacket.total = data.readUnsignedShort();
            subPacket.no = data.readUnsignedShort();
            return subPacket;
        }
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> processContext, SubPacket instance) {
        PacketHeader packetHeader = (PacketHeader) processContext.instance;
        if (packetHeader.subPacketFlag == 1) {
            if (instance == null) {
                throw ParseException.get("subPacketFlag[1] but subPacket is null");
            }
            data.writeShort(instance.total);
            data.writeShort(instance.no);
        }
    }
}
