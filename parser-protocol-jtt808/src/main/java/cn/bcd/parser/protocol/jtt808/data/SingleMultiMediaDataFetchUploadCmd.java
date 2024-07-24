package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;


public class SingleMultiMediaDataFetchUploadCmd implements PacketBody{
    //多媒体id
    @F_num(type = NumType.uint32)
    public long id;
    //删除标志
    @F_num(type = NumType.uint8)
    public byte flag;
}
