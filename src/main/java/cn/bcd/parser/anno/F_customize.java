package cn.bcd.parser.anno;

import cn.bcd.parser.processor.Processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 用户自己实现解析逻辑
 * <p>
 * 反解析中
 * 值可以为null、null的含义由定制逻辑自己实现
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface F_customize {
    /**
     * 处理类
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass();

    /**
     * 处理类参数
     * 在new {@link #processorClass()}时候、会传入指定参数、以,分割
     * 空字符串、则不传入参数
     * 参数类型支持java类型有、int、long、float、double、String
     * 例如有5个参数
     * int、long、float、double、String
     * 则值可以是
     * "100,1000L,1.123F,100.123,\"test\""
     */
    String processorArgs() default "";

    /**
     * 变量名称、仅作用于当前类
     * 取值a-z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,供其他注解长度表达式使用
     */
    char var() default '0';

    /**
     * 全局变量名称、作用于一个对象解析的生命周期中
     * 要求标注字段必须为数字类型
     * 此变量值会在{@link cn.bcd.parser.processor.ProcessContext#globalVars}中、跟随解析参数传递
     * 取值A-Z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,仅供其他注解长度表达式使用
     */
    char globalVar() default '0';
}
