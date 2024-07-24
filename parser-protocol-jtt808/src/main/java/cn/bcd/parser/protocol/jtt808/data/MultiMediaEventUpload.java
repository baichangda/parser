package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class MultiMediaEventUpload implements PacketBody {
    //多媒体数据id
    @F_num(type = NumType.uint32)
    public long id;
    //多媒体类型
    @F_num(type = NumType.uint8)
    public byte type;
    //多媒体格式编码
    @F_num(type = NumType.uint8)
    public byte code;
    //事件项编码
    @F_num(type = NumType.uint8)
    public byte eventCode;
    //通道id
    @F_num(type = NumType.uint8)
    public short channelId;
}
