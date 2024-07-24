package cn.bcd.parser.base.builder;

import cn.bcd.parser.base.anno.BitRemainingMode;
import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_bit_num_array;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.util.ParseUtil;
import cn.bcd.parser.base.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.List;

public class FieldBuilder__F_bit_num_array extends FieldBuilder {

    private boolean finish(BuilderContext context) {
        List<Field> fieldList = context.class_fieldList;
        F_bit_num_array f_bit_num_array = context.field.getAnnotation(F_bit_num_array.class);
        if (f_bit_num_array.bitRemainingMode() == BitRemainingMode.ignore) {
            return true;
        } else {
            if (context.fieldIndex == fieldList.size() - 1) {
                return f_bit_num_array.bitRemainingMode() == BitRemainingMode.Default;
            } else {
                if (f_bit_num_array.bitRemainingMode() == BitRemainingMode.Default) {
                    Field next = fieldList.get(context.fieldIndex + 1);
                    F_bit_num next_f_bit_num = next.getAnnotation(F_bit_num.class);
                    F_bit_num_array next_f_bit_num_array = next.getAnnotation(F_bit_num_array.class);
                    return next_f_bit_num == null && next_f_bit_num_array == null;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final Class<?> arrayElementType = fieldTypeClass.componentType();
        final String arrayElementTypeName = arrayElementType.getName();
        final Class<F_bit_num_array> annoClass = F_bit_num_array.class;
        final F_bit_num_array anno = context.field.getAnnotation(annoClass);

        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.bitOrder);
        final boolean unsigned = anno.singleUnsigned();
        final String varNameBitBuf = context.getBitBuf_parse();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final String valExpr = anno.singleValExpr();
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        int skipBefore = anno.skipBefore();
        int skipAfter = anno.skipAfter();
        if(skipBefore>0){
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipBefore);
        }
        switch (arrayElementTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
            }
            default -> {
                if (!arrayElementType.isEnum()) {
                    ParseUtil.notSupport_fieldType(context, annoClass);
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_bit_num_array.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            arrLenRes = anno.len() + "";
        }


        String arrVarName = varNameField + "_arr";
        final String varNameArrayElement = varNameField + "_arrEle";


        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        ParseUtil.append(body, "final {} {}=({}){}.read({},{},{});\n", arrayElementTypeName, varNameArrayElement, arrayElementTypeName, varNameBitBuf, singleLen, bigEndian, unsigned);
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        String valCode = ParseUtil.replaceValExprToCode(valExpr, varNameArrayElement);
        //格式化精度
        if ((arrayElementType == float.class || arrayElementType == double.class) && anno.singlePrecision() >= 0) {
            valCode = ParseUtil.format("{}.round((double){},{})", ParseUtil.class.getName(), valCode, anno.singlePrecision());
        }
        ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, valCode);
        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
        if(skipAfter>0){
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipAfter);
        }
        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_bit_num_array> annoClass = F_bit_num_array.class;
        final F_bit_num_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.bitOrder);
        final boolean unsigned = anno.singleUnsigned();
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final Class<?> arrElementType = fieldTypeClass.componentType();
        final boolean isFloat = arrElementType == float.class || arrElementType == double.class;
        final StringBuilder body = context.method_body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();

        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameBitBuf = context.getBitBuf_deParse();
        int skipBefore = anno.skipBefore();
        int skipAfter = anno.skipAfter();
        ParseUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
        if (skipBefore>0){
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipBefore);
        }
        final String varNameFieldArr = varNameField + "_arr";
        ParseUtil.append(body, "final {}[] {}={};\n", arrElementType, varNameFieldArr, valCode);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
        if (anno.singleValExpr().isEmpty()) {
            ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, varNameFieldArr + "[i]", singleLen, bigEndian, unsigned);
        } else {
            if (isFloat) {
                ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.singleValExpr()), varNameFieldArr + "[i]"), singleLen, bigEndian, unsigned);
            } else {
                ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.singleValExpr()), varNameFieldArr + "[i]"), singleLen, bigEndian, unsigned);
            }
        }
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        ParseUtil.append(body, "}\n");
        if (skipAfter>0){
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipAfter);
        }
        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
        ParseUtil.append(body, "}\n");
    }

}
