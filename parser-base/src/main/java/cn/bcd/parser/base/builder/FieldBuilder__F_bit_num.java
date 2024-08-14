package cn.bcd.parser.base.builder;

import cn.bcd.parser.base.anno.BitRemainingMode;
import cn.bcd.parser.base.anno.F_bit_num;
import cn.bcd.parser.base.anno.F_bit_num_array;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.util.ParseUtil;
import cn.bcd.parser.base.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.List;

public class FieldBuilder__F_bit_num extends FieldBuilder {

    private boolean finish(BuilderContext context) {
        List<Field> fieldList = context.class_fieldList;
        F_bit_num f_bit_num = context.field.getAnnotation(F_bit_num.class);
        if (f_bit_num.bitRemainingMode() == BitRemainingMode.ignore) {
            return true;
        } else {
            if (context.fieldIndex == fieldList.size() - 1) {
                return f_bit_num.bitRemainingMode() == BitRemainingMode.Default;
            } else {
                if (f_bit_num.bitRemainingMode() == BitRemainingMode.Default) {
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
        final Class<F_bit_num> annoClass = F_bit_num.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_bit_num anno = field.getAnnotation(annoClass);
        ParseUtil.check_var(context, annoClass, anno.var(), anno.globalVar());
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.bitOrder);
        final boolean unsigned = anno.unsigned();
        int skipBefore = anno.skipBefore();
        int skipAfter = anno.skipAfter();
        final StringBuilder body = context.method_body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameBitBuf = context.getBitBuf_parse();
        if (skipBefore > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipBefore);
        }

        final String sourceValTypeName;
        switch (fieldTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
                sourceValTypeName = fieldTypeName;
            }
            default -> {
                if (fieldTypeClass.isEnum()) {
                    sourceValTypeName = "int";
                } else {
                    ParseUtil.notSupport_fieldType(context, annoClass);
                    sourceValTypeName = null;
                }
            }
        }


        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw ParseException.get("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        ParseUtil.append(body, "final {} {}=({}){}.read({},{},{});\n", sourceValTypeName, varNameField, sourceValTypeName, varNameBitBuf, len, bigEndian, unsigned);

        String valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField);
        if (fieldTypeClass.isEnum()) {
            ParseUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        } else {
            //格式化精度
            if ((fieldTypeClass == float.class || fieldTypeClass == double.class) && anno.precision() >= 0) {
                valCode = ParseUtil.format("{}.round((double){},{})", ParseUtil.class.getName(), valCode, anno.precision());
            }
            ParseUtil.append(body, "{}.{}=({})({});\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        }

        if (skipAfter > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipAfter);
        }

        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

        final char var = anno.var();
        if (var != '0') {
            context.method_varToFieldName.put(var, varNameField);
        }
        final char globalVar = anno.globalVar();
        if (globalVar != '0') {
            ParseUtil.appendPutGlobalVar(context, globalVar, varNameField);
        }

    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_bit_num> annoClass = F_bit_num.class;
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(annoClass);
        ParseUtil.check_var(context, annoClass, anno.var(), anno.globalVar());
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.bitOrder);
        final boolean unsigned = anno.unsigned();
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.method_body;
        final String fieldName = field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final boolean isFloat = fieldType == float.class || fieldType == double.class;
        final char var = anno.var();
        int skipBefore = anno.skipBefore();
        int skipAfter = anno.skipAfter();
        final String varNameBitBuf = context.getBitBuf_deParse();

        if (skipBefore > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipBefore);
        }

        final String fieldTypeName;
        String valCode;
        //先判断是否是枚举类型、如果是枚举转换为int
        if (fieldType.isEnum()) {
            valCode = ParseUtil.format("{}.toInteger()", varNameInstance + "." + fieldName);
            fieldTypeName = "int";
        } else {
            valCode = varNameInstance + "." + fieldName;
            fieldTypeName = fieldType.getName();
        }

        //判断是否用到变量中、如果用到了、需要定义变量
        if (var != '0') {
            ParseUtil.append(body, "final {} {}={};\n", fieldTypeName, varNameField, valCode);
            context.method_varToFieldName.put(var, varNameField);
            valCode = varNameField;
        }

        //判断是否用到全局变量中、如果用到了、添加进去
        if (anno.globalVar() != '0') {
            ParseUtil.appendPutGlobalVar(context, anno.globalVar(), valCode);
        }

        //最后判断是否用了值表达式、如果用了、进行表达式处理
        if (!anno.valExpr().isEmpty()) {
            if (isFloat) {
                valCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            } else {
                valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            }
        }

        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw ParseException.get("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        ParseUtil.append(body, "{}.write((long)({}),{},{});\n", varNameBitBuf, valCode, len, bigEndian);

        if (skipAfter > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, skipAfter);
        }
        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
    }


}
