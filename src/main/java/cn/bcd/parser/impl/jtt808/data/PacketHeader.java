package cn.bcd.parser.impl.jtt808.data;


import cn.bcd.parser.anno.*;
import cn.bcd.parser.impl.jtt808.processor.SubPacketProcessor;

public class PacketHeader {
    //消息id
    @F_num(type = NumType.uint16)
    public int msgId;
    //消息体属性-版本标志
    @F_bit_num_easy(bitStart = 14, bitEnd = 14)
    public byte versionFlag;
    //消息体属性-分包
    @F_bit_num_easy(bitStart = 13, bitEnd = 13)
    public byte subPacketFlag;
    //消息体属性-数据加密方式
    @F_bit_num_easy(bitStart = 12, bitEnd = 10)
    public byte encryptType;
    //消息体属性-消息体长度
    @F_bit_num_easy(bitStart = 9, bitEnd = 0)
    public int msgLen;
    //协议版本号
    @F_num(type = NumType.uint8)
    public short version;
    //终端手机号
    @F_string_bcd(len = 10, appendMode = StringAppendMode.lowAddressAppend)
    public String phoneNumber;
    //消息流水号
    @F_num(type = NumType.uint16)
    public int sn;
    @F_customize(processorClass = SubPacketProcessor.class)
    public SubPacket subPacket;
}
