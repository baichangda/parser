package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.anno.C_impl;
import cn.bcd.parser.anno.F_date_bytes_6;
import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.NumType;

import java.util.Date;
@C_impl(value = 0x06)
public class PlatformLogoutData implements PacketData {
    //登出时间
    @F_date_bytes_6
    public Date collectTime;

    //登出流水号
    @F_num(type = NumType.uint16)
    public int sn;
}
