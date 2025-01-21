package cn.bcd.parser.protocol.jtt808.data;


import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.F_string;
import cn.bcd.parser.base.anno.data.NumType;

public class IssuedTerminalUpgradeRequest implements PacketBody {
    //升级类型
    @F_num(type = NumType.uint8)
    public byte type;
    //制造商id
    @F_num_array(singleType = NumType.uint8, len = 5)
    public byte[] manufacturerId;
    //终端固件版本号长度
    @F_num(type = NumType.uint8, var = 'b')
    public short terminalFirmwareVersionLen;
    //终端固件版本号
    @F_string(lenExpr = "b", charset = "GBK")
    public String terminalFirmwareVersion;
    //升级数据包长度
    @F_num(type = NumType.uint32, var = 'n')
    public long len;
    //升级数据包
    @F_num_array(singleType = NumType.uint8, lenExpr = "n")
    public byte[] packet;
}
