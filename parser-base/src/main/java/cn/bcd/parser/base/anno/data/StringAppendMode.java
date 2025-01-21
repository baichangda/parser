package cn.bcd.parser.base.anno.data;

import cn.bcd.parser.base.anno.F_string;

/**
 * 说明参考{@link F_string#appendMode()}注释
 */
public enum StringAppendMode {
    /**
     * 不补0
     */
    noAppend,
    /**
     * 低内存地址补0
     * 例如byte[4]、0是低内存地址、4是高内存地址
     */
    lowAddressAppend,
    /**
     * 高内存地址补0
     * 例如byte[4]、0是低内存地址、4是高内存地址
     */
    highAddressAppend
}
