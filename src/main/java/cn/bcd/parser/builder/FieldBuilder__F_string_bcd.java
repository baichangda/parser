package cn.bcd.parser.builder;


import cn.bcd.parser.anno.F_string_bcd;
import cn.bcd.parser.exception.ParseException;
import cn.bcd.parser.util.BcdUtil;
import cn.bcd.parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Field;

public class FieldBuilder__F_string_bcd extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        if (fieldType != String.class) {
            ParseUtil.notSupport_type(context, F_string_bcd.class);
        }
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string_bcd.class.getName());
            } else {
                lenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            lenRes = anno.len() + "";
        }

        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_noAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_lowAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_highAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final Field field = context.field;
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameFieldVal = varNameField + "_val";
        ParseUtil.append(body, "final String {}={};\n", varNameFieldVal, valCode);
        ParseUtil.append(body, "if({}!=null){\n", varNameFieldVal);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string_bcd.class.getName());
            } else {
                lenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            lenRes = anno.len() + "";
        }

        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.write_noAppend({},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.write_lowAddressAppend({},{},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.write_highAddressAppend({},{},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
            }
        }
        ParseUtil.append(body, "}\n", valCode);
    }


    public static String read_noAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        return BcdUtil.bytesToString_8421(bytes);
    }

    public static String read_lowAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        char[] chars = new char[len << 1];
        int startIndex = -1;
        for (int i = 0; i < len; i++) {
            byte b = bytes[i];
            if (startIndex == -1) {
                if (b != 0) {
                    if (((b >> 4) & 0x0F) == 0) {
                        startIndex = (i << 1) + 1;
                    } else {
                        startIndex = i << 1;
                    }
                    System.arraycopy(BcdUtil.BCD_8421_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
                }
            } else {
                System.arraycopy(BcdUtil.BCD_8421_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
            }
        }
        return new String(chars, startIndex, chars.length - startIndex);
    }

    public static String read_highAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        char[] chars = new char[len << 1];
        int endIndex = -1;
        for (int i = len - 1; i >= 0; i--) {
            byte b = bytes[i];
            if (endIndex == -1) {
                if (b != 0) {
                    if ((b & 0x0F) == 0) {
                        endIndex = i << 1;
                    } else {
                        endIndex = (i << 1) + 1;
                    }
                    System.arraycopy(BcdUtil.BCD_8421_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
                }
            } else {
                System.arraycopy(BcdUtil.BCD_8421_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
            }
        }
        return new String(chars, 0, endIndex + 1);
    }

    public static int write_noAppend(ByteBuf byteBuf, String s) {
        byte[] bytes = BcdUtil.stringToBytes_8421(s);
        byteBuf.writeBytes(bytes);
        return bytes.length;
    }

    public static void write_lowAddressAppend(ByteBuf byteBuf, String s, int len) {
        byte[] res = new byte[len];
        char[] charArray = s.toCharArray();
        int sLen = charArray.length;
        int actualLen = sLen >> 1;
        int offset = sLen & 1;
        for (int i = len - 1, j = actualLen - 1; j >= 0; i--, j--) {
            int index = (j << 1) + offset;
            int n1 = charArray[index] - '0';
            int n2 = charArray[index + 1] - '0';
            res[i] = (byte) (n1 << 4 | n2);
        }
        if (offset == 1) {
            res[len - actualLen - 1] = (byte) Character.getNumericValue(charArray[0]);
        }
        byteBuf.writeBytes(res);
    }


    public static void write_highAddressAppend(ByteBuf byteBuf, String s, int len) {
        byte[] res = new byte[len];
        char[] charArray = s.toCharArray();
        int sLen = charArray.length;
        int actualLen = charArray.length >> 1;
        for (int i = 0; i < actualLen; i++) {
            int charIndex = i << 1;
            int n1 = charArray[charIndex] - '0';
            int n2 = charArray[(charIndex) + 1] - '0';
            res[i] = (byte) (n1 << 4 | n2);
        }
        if ((sLen & 1) == 1) {
            res[actualLen] = (byte) (Character.getNumericValue(charArray[actualLen << 1]) << 4);
        }
        byteBuf.writeBytes(res);
    }

    public static void main(String[] args) {
        String s = "2117299841738";
        ByteBuf buffer1 = Unpooled.buffer();
        write_highAddressAppend(buffer1, s, 20);
        String s1 = read_highAddressAppend(buffer1, 20);
        System.out.println(s1);

        ByteBuf buffer2 = Unpooled.buffer();
        write_lowAddressAppend(buffer2, s, 20);
        String s2 = read_lowAddressAppend(buffer2, 20);
        System.out.println(s2);

        ByteBuf buffer3 = Unpooled.buffer();
        int len3 = write_noAppend(buffer3, s);
        String s3 = read_noAppend(buffer3, len3);
        System.out.println(s3);
    }
}
