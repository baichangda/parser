package cn.bcd.parser.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于如下类型
 * {@link java.util.Date}
 * {@link java.time.Instant}
 * {@link java.time.LocalDateTime}
 * {@link java.time.OffsetDateTime}
 * {@link java.time.ZonedDateTime}
 * int 此时代表时间戳秒
 * long 此时代表时间戳毫秒
 * {@link String} 此时使用{@link #stringFormat()}、{@link #valueZoneId()}格式化
**/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date_bytes_6 {

    /**
     * 用于表示原始值的时区
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String zoneId() default "+8";

    /**
     * 年份偏移量、结果年份=baseYear+原始值
     */
    int baseYear() default 2000;

    /**
     * 转换日期为字符串的格式
     * 在字段类型如下时候会使用到
     * {@link String}
     */
    String stringFormat() default "yyyyMMddHHmmssSSS";

    /**
     * 转换为字段值需要用到的时区
     * 在字段类型如下时候会使用到
     * {@link java.time.LocalDateTime}
     * {@link java.time.OffsetDateTime}
     * {@link java.time.ZonedDateTime}
     * {@link String}
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String valueZoneId() default "+8";

}
