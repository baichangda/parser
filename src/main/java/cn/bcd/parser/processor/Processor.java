package cn.bcd.parser.processor;

import cn.bcd.parser.anno.F_customize;
import io.netty.buffer.ByteBuf;

public interface Processor<T> {

    default T process(final ByteBuf data) {
        return process(data, new ProcessContext<>(data));
    }

    default void deProcess(final ByteBuf data, T instance) {
        deProcess(data, new ProcessContext<>(data), instance);
    }

    /**
     * @param data
     * @param processContext 具体指的是当前解析返回值赋值字段所在类的解析环境、其中{@link ProcessContext#instance}代表的是所在类的实例
     *                       不能为null
     *                       主要用于{@link F_customize}获取父类bean
     *                       需要注意的是、如果{@link F_customize#processorClass()}中的类被多个地方复用
     *                       则需要注意每个地方的解析方法{@link #process(ByteBuf, ProcessContext)}的parentContext不一样
     *                       例如:
     *                       有如下类定义关系
     *                       class A{public B b}
     *                       class B{public C c}
     *                       class C{public D d}
     *                       class D{public int n}
     *                       那么当解析D类字段n的时候、 {@link #process(ByteBuf, ProcessContext)}的parentContext代表类D的解析环境、可以有如下取值
     *                       processContext.instance=d
     *                       processContext.processContext.instance=c
     *                       processContext.processContext.processContext.instance=b
     *                       processContext.processContext.processContext.processContext.instance=a
     * @return
     */
    T process(final ByteBuf data, final ProcessContext<?> processContext);

    /**
     * @param data
     * @param processContext 和{{@link #process(ByteBuf)}}原理一致
     * @param instance
     */
    void deProcess(final ByteBuf data, final ProcessContext<?> processContext, T instance);
}
