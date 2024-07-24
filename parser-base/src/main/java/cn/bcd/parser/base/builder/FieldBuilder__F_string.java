package cn.bcd.parser.base.builder;


import cn.bcd.parser.base.anno.F_string;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.util.ParseUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class FieldBuilder__F_string extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_string anno = field.getAnnotation(F_string.class);
        if (fieldType != String.class) {
            ParseUtil.notSupport_type(context, F_string.class);
        }
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string.class.getName());
            } else {
                lenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final String varNameField = ParseUtil.getFieldVarName(context);
        final Charset charset = Charset.forName(anno.charset());
        final String charsetClassName = Charset.class.getName();
        final String charsetVarName = ParseUtil.defineClassVar(context, Charset.class, "{}.forName(\"{}\")", charsetClassName, charset.name());
        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}={}.readCharSequence({},{}).toString();\n", varNameInstance, field.getName(), varNameByteBuf, lenRes, charsetVarName);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_lowAddressAppend({},{},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string.class.getName(), varNameByteBuf, lenRes, charsetVarName);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_highAddressAppend({},{},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string.class.getName(), varNameByteBuf, lenRes, charsetVarName);
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameFieldVal = varNameField + "_val";
        ParseUtil.append(body, "final String {}={};\n", varNameFieldVal, valCode);
        ParseUtil.append(body, "if({}!=null){\n", varNameFieldVal);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string.class.getName());
            } else {
                lenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            lenRes = anno.len() + "";
        }
        final Charset charset = Charset.forName(anno.charset());
        final String charsetClassName = Charset.class.getName();
        final String charsetVarName = ParseUtil.defineClassVar(context, Charset.class, "{}.forName(\"{}\")", charsetClassName, charset.name());
        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.writeBytes({}.getBytes({}));\n", varNameByteBuf, varNameFieldVal, charsetVarName);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.write_lowAddressAppend({},{},{},{});\n", FieldBuilder__F_string.class.getName(), varNameByteBuf, varNameFieldVal, lenRes, charsetVarName);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.write_highAddressAppend({},{},{},{});\n", FieldBuilder__F_string.class.getName(), varNameByteBuf, varNameFieldVal, lenRes, charsetVarName);
            }
        }
        ParseUtil.append(body, "}\n", valCode);
    }

    public static String read_lowAddressAppend(ByteBuf byteBuf, int len, Charset charset) {
        final byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        int startIndex = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                startIndex = i;
                break;
            }
        }
        return new String(bytes, startIndex, len - startIndex, charset);
    }

    public static String read_highAddressAppend(ByteBuf byteBuf, int len, Charset charset) {
        final byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        int endIndex = bytes.length - 1;
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] != 0) {
                endIndex = i;
                break;
            }
        }
        return new String(bytes, 0, endIndex + 1, charset);
    }


    public static void write_lowAddressAppend(ByteBuf byteBuf, String str, int len, Charset charset) {
        byte[] bytes = str.getBytes(charset);
        int leaveLen = len - bytes.length;
        if (leaveLen > 0) {
            byteBuf.writeZero(leaveLen);
        }
        byteBuf.writeBytes(bytes);
    }

    public static void write_highAddressAppend(ByteBuf byteBuf, String str, int len, Charset charset) {
        byte[] bytes = str.getBytes(charset);
        byteBuf.writeBytes(bytes);
        int leaveLen = len - bytes.length;
        if (leaveLen > 0) {
            byteBuf.writeZero(leaveLen);
        }
    }
}
