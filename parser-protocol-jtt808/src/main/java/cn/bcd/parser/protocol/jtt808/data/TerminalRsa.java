package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.NumType;

public class TerminalRsa implements PacketBody{
    //rsa公钥{e,n}中的e
    @F_num(type = NumType.uint32)
    public long e;
    //rsa公钥{e,n}中的n
    @F_num_array(singleType = NumType.uint8, len = 128)
    public byte[] n;
}
