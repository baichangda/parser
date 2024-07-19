module parser.main {
    requires com.fasterxml.jackson.annotation;
    requires com.google.common;
    requires io.netty.buffer;
    requires org.javassist;
    requires org.slf4j;

    exports cn.bcd.parser;
}