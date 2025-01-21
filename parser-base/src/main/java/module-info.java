module parser.base {
    requires com.google.common;
    requires io.netty.buffer;
    requires org.javassist;
    requires org.slf4j;

    exports cn.bcd.parser.base;
    exports cn.bcd.parser.base.anno;
    exports cn.bcd.parser.base.builder;
    exports cn.bcd.parser.base.exception;
    exports cn.bcd.parser.base.processor;
    exports cn.bcd.parser.base.util;
    exports cn.bcd.parser.base.anno.data;
}