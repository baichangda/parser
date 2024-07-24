package cn.bcd.parser.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte、short、int、long、float、double、枚举类
 *
 * 枚举类
 * 仅支持整型数字
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 *     public static Example fromInteger(int i){}
 *     public int toInteger(){}
 * }
 *
 * 位解析
 * 最高支持连续32位
 *
 * 相邻的此注解字段会视为一组、也可以通过{@link #end()}在连续的注解字段中分多个组
 * 同一组的字段定义必须按照{@link #bitStart()}由高到低定义
 * 根据 bit组总长度 来读取对应长度字节
 * 1-8 1
 * 8-16 2
 * 17-24 3
 * 25-32 4
 *
 * 对比{@link F_bit_num}而言
 * 这个更轻量、生成的代码效率更高
 * 优先推荐使用此注解
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_num_easy {
    /**
     * bit开始、bit高位
     * 包含
     * 例如3字节、bit最高位为23、bit最低位为0
     */
    int bitStart();

    /**
     * bit结束、bit低位
     * 包含
     * 例如3字节、bit最高位为23、bit最低位为0
     */
    int bitEnd();

    /**
     * 表示当前bit组结束
     */
    boolean end() default false;

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
     * 取值a-z、A-Z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,供其他注解长度表达式使用
     */
    char var() default '0';

    /**
     * 全局变量名称、作用于一个对象解析的生命周期中
     * 要求标注字段必须为数字类型
     * 此变量值会在{@link cn.bcd.parser.base.processor.ProcessContext#globalVars}中、跟随解析参数传递
     * 取值a-z、A-Z、0表示不作为变量
     * 标注此标记的会在解析时候将值缓存,仅供其他注解长度表达式使用
     */
    char globalVar() default '0';

    /**
     * 结果小数精度、会四舍五入
     * 默认-1、代表不进行精度处理、最大为10
     * 仅当字段类型为float、double时候、此属性才有效
     */
    int precision() default -1;
}
