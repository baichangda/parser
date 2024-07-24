package cn.bcd.parser.impl.jtt808.data;


import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.F_string;
import cn.bcd.parser.anno.NumType;

public class TerminalAuthentication implements PacketBody {
    //鉴权码长度
    @F_num(type = NumType.uint8, var = 'n')
    public short codeLen;
    //鉴权码内容
    @F_string(lenExpr = "n", charset = "GBK")
    public String code;
    //终端imei
    @F_num_array(singleType = NumType.uint8, len = 15)
    public byte[] imei;
    //软件版本号
    @F_num_array(singleType = NumType.uint8, len = 20)
    public byte[] version;

}
