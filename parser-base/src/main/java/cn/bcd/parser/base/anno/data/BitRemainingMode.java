package cn.bcd.parser.base.anno.data;

import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_bit_num_array;

public enum BitRemainingMode {
    /**
     * 默认情况下、根据不同的情况选择不同的模式
     * 不是类最后一个字段且下一个字段为 {@link F_bit_num} 或 {@link F_bit_num_array}、此时为{@link #not_ignore}
     * 否则为{@link #ignore}
     */
    Default,
    /**
     * 忽略不满1字节的的bit
     */
    ignore,
    /**
     * 不忽略不满1字节的的bit
     */
    not_ignore
}
