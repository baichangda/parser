package cn.bcd.parser.base.util;

import cn.bcd.parser.base.exception.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/7/27.
 */
public class ExceptionUtil {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);


    /**
     * 获取真实的异常
     *
     * @param throwable
     * @return
     */
    public static Throwable getRealException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable temp = throwable;
        while (true) {
            if (temp instanceof ParseException ex) {
                if (ex.getTargetException() == null) {
                    return temp;
                } else {
                    temp = ex.getTargetException();
                }
            } else if (temp instanceof InvocationTargetException ex) {
                if (ex.getTargetException() == null) {
                    return temp;
                } else {
                    temp = ex.getTargetException();
                }
            } else {
                return temp;
            }
        }
    }
}
