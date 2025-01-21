package cn.bcd.parser.base.anno;

import cn.bcd.parser.base.anno.data.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于原始数据是常规的数字的数据类型、见{@link NumType}
 * <p>
 * 适用如下字段类型
 * byte、short、int、long、float、double、枚举类
 * 还有如下基础类型包装类、用于对原始值需要进行无效异常判断、然后再进行偏移量倍率处理
 * {@link NumVal_byte}
 * {@link NumVal_short}
 * {@link NumVal_int}
 * {@link NumVal_long}
 * {@link NumVal_float}
 * {@link NumVal_double}
 * <p>
 * 枚举类
 * 仅支持整型数字
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_num {

    /**
     * 读取原始值的数据类型
     * 数据类型
     */
    NumType type();


    /**
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
    String valExpr() default "";

    /**
     * 变量名称、仅作用于当前类
     * 取值a-z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,供其他注解长度表达式使用
     */
    char var() default '0';

    /**
     * 全局变量名称、作用于一个对象解析的生命周期中
     * 要求标注字段必须为数字类型
     * 此变量值会在{@link cn.bcd.parser.base.processor.ProcessContext#globalVars}中、跟随解析参数传递
     * 取值A-Z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,仅供其他注解长度表达式使用
     */
    char globalVar() default '0';


    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;

    /**
     * 结果小数精度、会四舍五入
     * 默认-1、代表不进行精度处理、最大为10
     * 仅当字段类型为float、double时候、此属性才有效
     */
    int precision() default -1;
}
