package cn.bcd.parser.base.anno.data;

public enum DateTsMode {
    /**
     * 协议定义uint64、代表时间戳毫秒
     */
    uint64_ms,
    /**
     * 协议定义uint64、代表时间戳秒
     */
    uint64_s,
    /**
     * 协议定义uint32、代表时间戳秒
     */
    uint32_s,
    /**
     * 协议定义float64、代表时间戳毫秒
     */
    float64_ms,
    /**
     * 协议定义float64、代表秒、精度为0.001、小数位代表毫秒
     */
    float64_s,

}
