package cn.bcd.parser.util;

public class BcdUtil {
    public static final char[] BCD_8421_DUMP_TABLE = new char[308];

    static {
        final char[] DIGITS = "0123456789".toCharArray();
        for (char c1 : DIGITS) {
            int n1 = c1 - '0';
            for (char c2 : DIGITS) {
                int n2 = c2 - '0';
                int i = ((n1 << 4) | n2) << 1;
                BCD_8421_DUMP_TABLE[i] = c1;
                BCD_8421_DUMP_TABLE[i + 1] = c2;
            }
        }
    }

    public static String bytesToString_8421(byte[] bytes) {
        char[] chars = new char[bytes.length << 1];
        for (int i = 0; i < bytes.length; i++) {
            System.arraycopy(BCD_8421_DUMP_TABLE, (bytes[i] & 0xff) << 1, chars, i << 1, 2);
        }
        return new String(chars);
    }

    /**
     * 将8421编码字符串转换为字节数组
     * 如果字符串长度不是偶数、则字符串前面补1个0
     * 因为2个8421编码字符串转化为1个字节
     *
     * @param s
     * @return
     */
    public static byte[] stringToBytes_8421(String s) {
        int length = s.length();
        char[] charArray = s.toCharArray();
        if ((length & 1) == 0) {
            //8421编码字符串转字节数组(偶数)
            byte[] bytes = new byte[length >> 1];
            for (int i = 0; i < bytes.length; i++) {
                int charIndex = i << 1;
                int n1 = charArray[charIndex] - '0';
                int n2 = charArray[charIndex + 1] - '0';
                bytes[i] = (byte) ((n1 << 4) | n2);
            }
            return bytes;
        } else {
            //8421编码字符串转字节数组(奇数)
            byte[] bytes = new byte[(length + 1) >> 1];
            for (int i = 1; i < bytes.length; i++) {
                int charIndex = (i << 1) - 1;
                int n1 = charArray[charIndex] - '0';
                int n2 = charArray[charIndex + 1] - '0';
                bytes[i] = (byte) ((n1 << 4) | n2);
            }
            bytes[0] = (byte) (charArray[0] - '0');
            return bytes;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < BCD_8421_DUMP_TABLE.length; i++) {
            System.out.println(i + " " + BCD_8421_DUMP_TABLE[i]);
        }
        System.out.println(bytesToString_8421(new byte[]{(byte) 133, (byte) 153}));
        System.out.println(bytesToString_8421(new byte[]{(byte) 66, (byte) 87}));
        System.out.println(bytesToString_8421(new byte[]{(byte) 0x12, (byte) 99}));
    }
}
