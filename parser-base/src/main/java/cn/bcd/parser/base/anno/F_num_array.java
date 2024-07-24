package cn.bcd.parser.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte[]、short[]、int[]、long[]、float[]、double[]、enum[]
 * 数组长度=总字节数/singleLen
 * {@link #len()}和{@link #lenExpr()} 二选一、代表字段数组长度
 * <p>
 * 枚举类
 * 仅支持整型数字
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 *     public static Example fromInteger(int i){}
 *     public int toInteger(){}
 * }
 * <p>
 * 反解析中
 * 值可以为null、代表空数组
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_num_array {
    /**
     * 数组元素个数
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 数组元素个数表达式,配合var参数使用
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
     * 每个数组元素
     * 读取原始值的数据类型
     * 数据类型
     */
    NumType singleType();

    /**
     * 每个数组元素
     * 在读取后、应该skip的byte长度
     */
    int singleSkip() default 0;

    /**
     * 每个数组元素
     * 值处理表达式
     * 在解析出的原始值得基础上,进行运算
     * 公式中的x变量代表字段原始的值
     * 注意:
     * 表达式需要符合java运算表达式规则
     * 例如:
     * x-10
     * x*10
     * (x+10)*100
     * (x+100)/100
     */
    String singleValExpr() default "";

    /**
     * 每个数组元素
     * 字节序模式
     */
    ByteOrder singleOrder() default ByteOrder.Default;

    /**
     * 每个数组元素结果小数精度、会四舍五入
     * 默认-1、代表不进行精度处理、最大为10
     * 仅当字段类型为float、double时候、此属性才有效
     */
    int singlePrecision() default -1;
}
