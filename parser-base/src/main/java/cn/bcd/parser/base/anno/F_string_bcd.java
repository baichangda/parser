package cn.bcd.parser.base.anno;

import cn.bcd.parser.base.anno.data.StringAppendMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于原始数据是字节数组以bcd编码方式代表字符串
 *
 * 用于如下类型字段
 * String
 * <p>
 * 反解析中
 * 值可以为null、此时null代表空字符串
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_string_bcd {
    /**
     * 占用字节数
     * 1-8
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 字段所占字节长度表达式
     * 用于固定长度字段解析,配合var参数使用,代表的是Byte的长度
     * 与{@link #len()}互斥
     * 变量取值来源于var、globalVar
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     * a*(b-2)+A
     */
    String lenExpr() default "";

    /**
     * 在写入时候
     * 当字符串长度小于指定长度时候、需要填充0
     * 填充模式如下
     * {@link StringAppendMode#noAppend} 不补
     * {@link StringAppendMode#lowAddressAppend} 低内存地址补
     * {@link StringAppendMode#highAddressAppend} 高内存地址补
     * <p>
     * 内存地址解释
     * 假如有byte[4]、其中0是低内存地址、3是高内存地址
     */
    StringAppendMode appendMode() default StringAppendMode.noAppend;

}
