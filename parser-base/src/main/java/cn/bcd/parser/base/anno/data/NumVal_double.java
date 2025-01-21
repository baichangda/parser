package cn.bcd.parser.base.anno.data;

/**
 * @param type 0代表值无问题
 *             其他情况代表值有各种异常情况、此时无需查看val
 * @param val 当type为0时有效
 */
public record NumVal_double(int type, double val){
}
