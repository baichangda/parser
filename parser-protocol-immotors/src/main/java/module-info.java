module parser.protocol.immotors {
    requires parser.base;
    requires com.fasterxml.jackson.annotation;
    requires org.slf4j;
    requires io.netty.buffer;

    exports cn.bcd.parser.protocol.immotors;
}