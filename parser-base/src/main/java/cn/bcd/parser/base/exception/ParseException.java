package cn.bcd.parser.base.exception;


import cn.bcd.parser.base.util.ExceptionUtil;
import cn.bcd.parser.base.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 建造此异常类的目的:
 * 1、在所有需要抛非运行时异常的地方,用此异常包装,避免方法调用时候需要捕获异常
 *    此时打印堆栈会打印完整的调用链、包括包装异常的调用链
 *    此时异常信息为null、如果需要获取真实异常信息、调用{@link #getRealExceptionMessage()}
 * 2、在业务需要出异常的时候,定义异常并且抛出
 */
public class ParseException extends RuntimeException {
    public int code = 1;

    private final Throwable target;

    private ParseException(String message) {
        super(message);
        this.target = null;
    }

    private ParseException(Throwable target) {
        super(null, (Throwable) null);
        this.target = target;
    }

    private ParseException(String message, Throwable target) {
        super(message, (Throwable) null);
        this.target = target;
    }

    public Throwable getTargetException() {
        return target;
    }

    @Override
    public Throwable getCause() {
        return target;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getRealExceptionMessage(){
        return ExceptionUtil.getRealException(this).getMessage();
    }


    public ParseException code(int code) {
        this.code = code;
        return this;
    }

    public static ParseException get(String message) {
        return new ParseException(message);
    }

    /**
     * 将异常信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     * 如果需要转义、则\\{}
     *
     * @param message
     * @param params
     * @return
     */
    public static ParseException get(String message, Object... params) {
        return new ParseException(StringUtil.format(message, params));
    }

    public static ParseException get(String message, Object arg) {
        return new ParseException(StringUtil.format(message, arg));
    }

    public static ParseException get(String message, Object arg1, Object arg2) {
        return new ParseException(StringUtil.format(message, arg1, arg2));
    }

    public static ParseException get(String message, Throwable e) {
        return new ParseException(message, e);
    }

    public static ParseException get(Throwable e) {
        return new ParseException(e);
    }

    static Logger logger = LoggerFactory.getLogger(ParseException.class);

    public static void main(String[] args) {
//        throw ParseException.get("[{}]-[{}]", null, 100000);

        try {
            String s = null;
            s.getBytes();
        } catch (Exception e) {
            ParseException e1 = ParseException.get(e);
            ParseException e2 = ParseException.get(e1);
            logger.error("error", e2);
            logger.info(e2.getMessage());
        }


        ParseException e = ParseException.get("测试");
        ParseException e1 = ParseException.get(e);
        ParseException e2 = ParseException.get(e1);
        logger.error("error", e2);
        logger.info(e2.getMessage());

        InvocationTargetException e3 = new InvocationTargetException(e);
        ParseException e4 = ParseException.get(e3);
        logger.error("error", e4);
        logger.info(e4.getMessage());

        ParseException e5 = ParseException.get(e);
        InvocationTargetException e6 = new InvocationTargetException(e5);
        logger.error("error", e6);
        logger.info(e6.getMessage());
    }

}
