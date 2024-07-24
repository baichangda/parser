package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

public class PlatformRsa implements PacketBody{
    //rsa公钥{e,n}中的e
    @F_num(type = NumType.uint32)
    public long e;
    //rsa公钥{e,n}中的n
    @F_num_array(singleType = NumType.uint8, len = 128)
    public byte[] n;
}
