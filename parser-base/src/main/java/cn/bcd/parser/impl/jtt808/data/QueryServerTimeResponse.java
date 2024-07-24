package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_date_bytes_6;

import java.util.Date;

public class QueryServerTimeResponse implements PacketBody {
    //服务器时间
    @F_date_bytes_6(zoneId = "+0")
    public Date serverTime;
}
