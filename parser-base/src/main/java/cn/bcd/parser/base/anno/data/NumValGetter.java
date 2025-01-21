package cn.bcd.parser.base.anno.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 值获取器
 * 用于字段类型为如下类型的值验证
 * {@link NumVal_byte}
 * {@link NumVal_short}
 * {@link NumVal_int}
 * {@link NumVal_long}
 * {@link NumVal_float}
 * {@link NumVal_double}
 */
public abstract class NumValGetter {
    public static AtomicInteger globalIndex = new AtomicInteger();
    public final static ConcurrentHashMap<Integer, NumValGetter> index_numValGetter = new ConcurrentHashMap<>();

    public static NumValGetter get(int index) {
        return index_numValGetter.get(index);
    }

    public final int index;

    public NumValGetter() {
        index = globalIndex.getAndIncrement();
        index_numValGetter.put(index, this);
    }

    public abstract int getType(NumType numType, int val);

    public abstract int getType(NumType numType, long val);

    public abstract int getVal_int(NumType numType, int type);

    public abstract long getVal_long(NumType numType, int type);
}
