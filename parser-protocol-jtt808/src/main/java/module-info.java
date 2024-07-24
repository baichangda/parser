module parser.protocol.jtt808p {
    requires parser.base;
    requires com.fasterxml.jackson.annotation;
    requires org.javassist;
    requires org.slf4j;
    requires io.netty.buffer;

    exports cn.bcd.parser.protocol.jtt808.data;
    exports cn.bcd.parser.protocol.jtt808.processor;
}