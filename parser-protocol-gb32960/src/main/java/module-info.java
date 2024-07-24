module parser.protocol.gb32960p {
    requires parser.base;
    requires com.fasterxml.jackson.annotation;
    requires org.javassist;
    requires org.slf4j;
    requires io.netty.buffer;

    exports cn.bcd.parser.protocol.gb32960.data;
    exports cn.bcd.parser.protocol.gb32960.processor;
}