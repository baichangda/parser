package cn.bcd.parser.impl.jtt808.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.netty.buffer.ByteBuf;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface AreaOrPathItem {
     void write(ByteBuf data);
}
