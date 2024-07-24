package cn.bcd.parser.base.util;

import org.slf4j.helpers.MessageFormatter;

public class StringUtil {
    /**
     * 将信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     * 如果需要转义、则\\{}
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(String message, Object... params) {
        return MessageFormatter.arrayFormat(message, params, null).getMessage();
    }

    public static String format(String message, Object arg) {
        return MessageFormatter.format(message, arg).getMessage();
    }

    public static String format(String message, Object arg1, Object arg2) {
        return MessageFormatter.format(message, arg1, arg2).getMessage();
    }

    public static void main(String[] args) {
        System.out.println(format("{}-{}", 123, null));
        System.out.println(format("{}-\\{}-{}", 123, null, "abc"));
    }

}
