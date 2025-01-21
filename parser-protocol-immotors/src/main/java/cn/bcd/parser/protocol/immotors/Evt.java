package cn.bcd.parser.protocol.immotors;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Evt {
    @F_num(type = NumType.uint16)
    public int evtId;
}
