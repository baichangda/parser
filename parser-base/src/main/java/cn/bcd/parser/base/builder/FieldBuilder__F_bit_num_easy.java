package cn.bcd.parser.base.builder;

import cn.bcd.parser.base.anno.F_bit_num_easy;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.util.ParseUtil;
import cn.bcd.parser.base.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class FieldBuilder__F_bit_num_easy extends FieldBuilder {
    record Info(String varNameNum, int startFieldIndex, int endFieldIndex, int maxBit) {

    }

    private Info getInfo(BuilderContext context) {
        Map<String, Object> cache = context.method_cache;
        Object object = cache.get(F_bit_num_easy.class.getName());
        if (object == null) {
            final String varNameField = ParseUtil.getFieldVarName(context);
            int startFieldIndex = context.fieldIndex;
            int endFieldIndex = 0;
            final Field prevField;
            if (context.fieldIndex == 0) {
                prevField = null;
            } else {
                prevField = context.class_fieldList.get(context.fieldIndex - 1);
            }
            final boolean first;
            if (prevField == null) {
                first = true;
            } else {
                F_bit_num_easy prevAnno = prevField.getAnnotation(F_bit_num_easy.class);
                first = prevAnno == null || prevAnno.end();
            }
            int maxBit = 0;
            if (first) {
                for (int i = context.fieldIndex; i < context.class_fieldList.size(); i++) {
                    Field field = context.class_fieldList.get(i);
                    F_bit_num_easy anno = field.getAnnotation(F_bit_num_easy.class);
                    if (anno == null) {
                        endFieldIndex = i - 1;
                        break;
                    } else {
                        maxBit = Math.max(maxBit, anno.bitStart());
                        if (anno.end()) {
                            endFieldIndex = i;
                            break;
                        }
                        if (i == context.class_fieldList.size() - 1) {
                            endFieldIndex = i;
                            break;
                        }
                    }
                }
            }
            final String varNameNum = varNameField + "_num";

            Info info = new Info(varNameNum, startFieldIndex, endFieldIndex, maxBit);
            cache.put(F_bit_num_easy.class.getName(), info);
            return info;
        } else {
            return (Info) object;
        }
    }

    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_bit_num_easy> annoClass = F_bit_num_easy.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_bit_num_easy anno = field.getAnnotation(annoClass);
        final String varNameField = ParseUtil.getFieldVarName(context);
        Info info = getInfo(context);
        String varNameNum = info.varNameNum();
        if (info.startFieldIndex == context.fieldIndex) {
            int maxBit = info.maxBit();
            final String funcName;
            if (maxBit > 0 && maxBit <= 8) {
                funcName = "readByte";
            } else if (maxBit <= 16) {
                funcName = "readShort";
            } else if (maxBit <= 24) {
                funcName = "readMedium";
            } else if (maxBit <= 32) {
                funcName = "readInt";
            } else {
                throw ParseException.get("class[{}] field[{}] anno[{}] maxBit[{}] not support", context.clazz.getName(), field.getName(), F_bit_num_easy.class, maxBit);
            }
            ParseUtil.append(context.method_body, "final int {}={}.{}();\n", varNameNum, FieldBuilder.varNameByteBuf, funcName);
        }

        StringBuilder body = context.method_body;
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

        ParseUtil.append(body, "final {} {}=({})({});\n", sourceValTypeName, varNameField, sourceValTypeName, getBitNumCode_read(varNameNum, anno.bitStart(), anno.bitEnd()));

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
        final Class<F_bit_num_easy> annoClass = F_bit_num_easy.class;
        final Field field = context.field;
        final F_bit_num_easy anno = field.getAnnotation(annoClass);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.method_body;
        final String fieldName = field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);

        final Class<?> fieldType = field.getType();
        final boolean isFloat = fieldType == float.class || fieldType == double.class;
        final char var = anno.var();
        String valCode;
        final String fieldTypeName;
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
        final String varNameVal;
        if (anno.valExpr().isEmpty()) {
            varNameVal = valCode;
        } else {
            if (isFloat) {
                valCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            } else {
                valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            }
            varNameVal = varNameField + "_val";
            ParseUtil.append(body, "final {} {}={};\n", fieldType.getName(), varNameVal, valCode);
        }

        Info info = getInfo(context);
        String varNameNum = info.varNameNum();
        int maxBitEnd = info.maxBit();
        if (info.startFieldIndex == context.fieldIndex) {
            ParseUtil.append(body, "final int {}=0;\n", varNameNum);
        }

        ParseUtil.append(body, "{};\n", getBitNumCode_write(varNameNum, varNameVal, anno.bitStart(), anno.bitEnd()));
        if (info.endFieldIndex == context.fieldIndex) {
            if (maxBitEnd > 0 && maxBitEnd <= 8) {
                ParseUtil.append(body, "{}.writeByte({});\n", FieldBuilder.varNameByteBuf, varNameNum);
            } else if (maxBitEnd <= 16) {
                ParseUtil.append(body, "{}.writeShort({});\n", FieldBuilder.varNameByteBuf, varNameNum);
            } else if (maxBitEnd <= 24) {
                ParseUtil.append(body, "{}.writeMedium({});\n", FieldBuilder.varNameByteBuf, varNameNum);
            } else if (maxBitEnd <= 32) {
                ParseUtil.append(body, "{}.writeInt({});\n", FieldBuilder.varNameByteBuf, varNameNum);
            } else {
                throw ParseException.get("class[{}] field[{}] anno[{}] maxBit[{}] not support", context.clazz.getName(), field.getName(), annoClass.getName(), maxBitEnd);
            }
        }
    }

    private static String getBitNumCode_read(String varNameNum, int startBit, int endBit) {
        if (endBit == 0) {
            return ParseUtil.format("{}&{}", varNameNum, (1 << (startBit - endBit + 1)) - 1);
        } else {
            return ParseUtil.format("({}>>{})&{}", varNameNum, endBit, (1 << (startBit - endBit + 1)) - 1);
        }
    }

    private static String getBitNumCode_write(String varNameNum, String varVal, int startBit, int endBit) {
        if (endBit == 0) {
            return ParseUtil.format("{}|=({}&{})", varNameNum, varVal, (1 << (startBit - endBit + 1)) - 1);
        } else {
            return ParseUtil.format("{}|=(({}&{})<<{})", varNameNum, varVal, (1 << (startBit - endBit + 1)) - 1, endBit);
        }
    }

}
