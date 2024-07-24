package cn.bcd.parser.base.anno;

import cn.bcd.parser.base.processor.Processor;

import java.lang.annotation.*;

/**
 * 用于和{@link F_bean}标注接口字段时候
 * 标注到其接口类型的实现类上
 * 注意:
 * 被标注的类必须要和其接口在一个包下、否则会出现扫描不到子类的情况
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface C_impl {
    /**
     * 用于配合{@link F_bean#implClassExpr()}使用
     * 当表达式解析为此值时候
     * 表明其实现类为当前注解标注的类
     *
     * 特殊值{@link Integer#MAX_VALUE}代表默认分支、当所有分支不命中时候、使用此注解标注的类
     */
    int[] value();

    /**
     * 处理类
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass() default Void.class;

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
}
