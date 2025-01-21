package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_date_bytes_6;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

import java.util.Date;

public class StorageMultiMediaDataFetchRequest implements PacketBody{
    //多媒体类型
    @F_num(type = NumType.uint8)
    public byte type;
    //通道id
    @F_num(type = NumType.uint8)
    public short id;
    //事件项编码
    @F_num(type = NumType.uint8)
    public byte code;
    //起始时间
    @F_date_bytes_6
    public Date startTime;
    //结束时间
    @F_date_bytes_6
    public Date endTime;
}
