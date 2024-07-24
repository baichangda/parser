package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_date_bytes_6;

import java.util.Date;

public class QueryServerTimeResponse implements PacketBody {
    //服务器时间
    @F_date_bytes_6(zoneId = "+0")
    public Date serverTime;
}
