package cn.bcd.parser.impl.jtt808.data;


import cn.bcd.parser.anno.*;

public class QueryTerminalPropResponse implements PacketBody {
    //终端类型
    @F_num(type = NumType.uint16)
    public int type;
    //制造商id
    @F_num_array(singleType = NumType.uint8, len = 5)
    public byte[] manufacturerId;
    //终端型号
    @F_num_array(singleType = NumType.uint8, len = 30)
    public byte[] model;
    //终端id
    @F_num_array(singleType = NumType.uint8, len = 30)
    public byte[] id;
    //终端SIM卡ICCID
    @F_string(len = 10, appendMode = StringAppendMode.lowAddressAppend)
    public String iccid;
    //终端硬件版本号长度
    @F_num(type = NumType.uint8, var = 'a')
    public short terminalHardwareVersionLen;
    //终端硬件版本号
    @F_string(lenExpr = "a", charset = "GBK")
    public String terminalHardwareVersion;
    //终端固件版本号长度
    @F_num(type = NumType.uint8, var = 'b')
    public short terminalFirmwareVersionLen;
    //终端固件版本号
    @F_string(lenExpr = "b", charset = "GBK")
    public String terminalFirmwareVersion;
    //gnss属性
    @F_num(type = NumType.uint8)
    public byte gnss;
    //通信模块属性
    @F_num(type = NumType.uint8)
    public byte communication;
}
