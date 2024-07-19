package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.Parser;
import cn.bcd.parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class TerminalRegisterRequest implements PacketBody {
    //省域id
    public int provinceId;
    //市县域id
    public int cityId;
    //制造商id
    public byte[] manufacturerId;
    //终端型号
    public byte[] terminalType;
    //终端id
    public byte[] terminalId;
    //车牌颜色
    public short plateColor;
    //车牌
    public String plateNo;

    static final Charset gbk = Charset.forName("GBK");

    public static TerminalRegisterRequest read(ByteBuf data, int len) {
        TerminalRegisterRequest terminalRegisterRequest = new TerminalRegisterRequest();
        terminalRegisterRequest.provinceId = data.readUnsignedShort();
        terminalRegisterRequest.cityId = data.readUnsignedShort();
        byte[] bytes1 = new byte[11];
        data.readBytes(bytes1);
        terminalRegisterRequest.manufacturerId = bytes1;
        byte[] bytes2 = new byte[30];
        data.readBytes(bytes2);
        terminalRegisterRequest.terminalType = bytes2;
        byte[] bytes3 = new byte[30];
        data.readBytes(bytes3);
        terminalRegisterRequest.terminalId = bytes3;
        terminalRegisterRequest.plateColor = data.readUnsignedByte();
        terminalRegisterRequest.plateNo = data.readCharSequence(len - 76, gbk).toString();
        return terminalRegisterRequest;
    }

    public void write(ByteBuf data) {
        data.writeShort(provinceId);
        data.writeShort(cityId);
        data.writeBytes(manufacturerId);
        data.writeBytes(terminalType);
        data.writeBytes(terminalId);
        data.writeByte(plateColor);
        data.writeCharSequence(plateNo, gbk);
    }

    public static void main(String[] args) {
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        Processor<Packet> processor = Parser.getProcessor(Packet.class);

        TerminalRegisterRequest terminalRegisterRequest = new TerminalRegisterRequest();
        terminalRegisterRequest.provinceId = 1;
        terminalRegisterRequest.cityId = 1;
        terminalRegisterRequest.manufacturerId = new byte[11];
        terminalRegisterRequest.terminalType = new byte[30];
        terminalRegisterRequest.terminalId = new byte[30];
        terminalRegisterRequest.plateColor = 100;
        terminalRegisterRequest.plateNo = "啊啊啊";

        PacketHeader packetHeader = new PacketHeader();
        packetHeader.msgId = 0x0100;
        packetHeader.msgLen = 76 + terminalRegisterRequest.plateNo.getBytes(gbk).length;
        packetHeader.sn = 1;

        Packet packet = new Packet();
        packet.startFlag = 0x7e;
        packet.header = packetHeader;
        packet.body = terminalRegisterRequest;
        packet.code = 0;
        packet.endFlag = 0x7e;

        ByteBuf buffer = Unpooled.buffer();
        processor.deProcess(buffer, null, packet);
        System.out.println(ByteBufUtil.hexDump(buffer));


        Packet process = processor.process(buffer, null);
        System.out.println(process);
    }
}
