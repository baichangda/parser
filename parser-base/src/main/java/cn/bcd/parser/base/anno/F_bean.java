package cn.bcd.parser.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于原始数据为结构体
 *
 * 适用于
 * 实体类字段
 * 接口类字段、必须定义{@link #implClassExpr()}属性、且子类配合使用{@link C_impl}标注
 * 如果是接口类、则其子类必须定义到和接口同一包下、因为只会扫描此包下的实现类
 * <p>
 * 反解析中
 * 值不能为null
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bean {

    /**
     * 当字段类型为接口类型时候、此属性才会生效
     * 变量取值来源于var、globalVar
     * 使用globalVar时候必须在变量前面带上@
     * 例如:
     * m
     */
    String implClassExpr() default "";
}
