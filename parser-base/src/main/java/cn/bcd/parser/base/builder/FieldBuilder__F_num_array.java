package cn.bcd.parser.base.builder;

import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.data.*;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.util.ParseUtil;
import cn.bcd.parser.base.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_num_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        if (buildParse_numVal(context)) {
            return;
        }
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final Class<?> arrayElementType = fieldTypeClass.componentType();
        final String arrayElementTypeName = arrayElementType.getName();
        final String sourceValTypeName;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);

        switch (arrayElementTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
                sourceValTypeName = arrayElementTypeName;
            }
            default -> {
                if (arrayElementType.isEnum()) {
                    sourceValTypeName = "int";
                } else {
                    ParseUtil.notSupport_fieldType(context, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_num_array.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            arrLenRes = String.valueOf(anno.len());
        }


        final NumType singleType = anno.singleType();
        final String singleValExpr = anno.singleValExpr();
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final int singleSkip = anno.singleSkip();
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        //优化处理 byte[]数组解析
        if (byte[].class.isAssignableFrom(fieldTypeClass) && (singleType == NumType.int8 || singleType == NumType.uint8) && singleValExpr.isEmpty() && singleSkip == 0) {
            ParseUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
        } else {
            String funcName;
            switch (singleType) {
                case uint8 -> {
                    if (sourceValTypeName.equals("byte")) {
                        funcName = varNameByteBuf + ".readByte()";
                    } else {
                        funcName = varNameByteBuf + ".readUnsignedByte()";
                    }
                }
                case uint16 -> {
                    if (sourceValTypeName.equals("short")) {
                        funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
                    } else {
                        funcName = varNameByteBuf + ".readUnsignedShort" + (bigEndian ? "" : "LE") + "()";
                    }
                }
                case uint24 -> {
                    funcName = varNameByteBuf + ".readUnsignedMedium" + (bigEndian ? "" : "LE") + "()";
                }
                case uint32 -> {
                    if (sourceValTypeName.equals("int")) {
                        funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
                    } else {
                        funcName = varNameByteBuf + ".readUnsignedInt" + (bigEndian ? "" : "LE") + "()";
                    }
                }
                case uint40 -> {
                    funcName = ParseUtil.format("{}.read_uint40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case uint48 -> {
                    funcName = ParseUtil.format("{}.read_uint48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case uint56 -> {
                    funcName = ParseUtil.format("{}.read_uint56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case int8 -> {
                    funcName = varNameByteBuf + ".readByte()";
                }
                case int16 -> {
                    funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
                }
                case int24 -> {
                    funcName = varNameByteBuf + ".readMedium" + (bigEndian ? "" : "LE") + "()";
                }
                case int32 -> {
                    funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
                }
                case int40 -> {
                    funcName = ParseUtil.format("{}.read_int40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case int48 -> {
                    funcName = ParseUtil.format("{}.read_int48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case int56 -> {
                    funcName = ParseUtil.format("{}.read_int56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                }
                case int64 -> {
                    funcName = varNameByteBuf + ".readLong" + (bigEndian ? "" : "LE") + "()";
                }
                case float32 -> {
                    funcName = varNameByteBuf + ".readFloat" + (bigEndian ? "" : "LE") + "()";
                }
                case float64 -> {
                    funcName = varNameByteBuf + ".readDouble" + (bigEndian ? "" : "LE") + "()";
                }
                default -> {
                    funcName = null;
                }
            }
            ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            final String varNameArrayElement = varNameField + "_arrEle";
            ParseUtil.append(body, "final {} {}=({}){};\n", sourceValTypeName, varNameArrayElement, sourceValTypeName, funcName);
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.skipBytes({});\n", varNameByteBuf, singleSkip);
            }
            //表达式运算
            String valCode = ParseUtil.replaceValExprToCode(singleValExpr, varNameArrayElement);
            if (arrayElementType.isEnum()) {
                ParseUtil.append(body, "{}[i]={}.fromInteger((int)({}));\n", arrVarName, arrayElementTypeName, valCode);
            } else {
                //格式化精度
                if ((arrayElementType == float.class || arrayElementType == double.class) && anno.singlePrecision() >= 0) {
                    valCode = ParseUtil.format("{}.round((double){},{})", ParseUtil.class.getName(), valCode, anno.singlePrecision());
                }
                ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, valCode);
            }
            ParseUtil.append(body, "}\n");
        }

        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    public boolean buildParse_numVal(BuilderContext context) {
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final Class<?> arrayElementType = fieldTypeClass.componentType();
        final String arrayElementTypeName = arrayElementType.getName();
        final String sourceValTypeName = ParseUtil.getNumFieldValType(context).getName();
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);

        if (arrayElementType != NumVal_byte.class && arrayElementType != NumVal_short.class
                && arrayElementType != NumVal_int.class && arrayElementType != NumVal_long.class
                && arrayElementType != NumVal_float.class && arrayElementType != NumVal_double.class) {
            return false;
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw ParseException.get("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_num_array.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceExprToCode(anno.lenExpr(), context);
            }
        } else {
            arrLenRes = String.valueOf(anno.len());
        }


        final NumType singleType = anno.singleType();
        final String singleValExpr = anno.singleValExpr();
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final int singleSkip = anno.singleSkip();
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        String funcName;
        switch (singleType) {
            case uint8 -> {
                if (sourceValTypeName.equals("byte")) {
                    funcName = varNameByteBuf + ".readByte()";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedByte()";
                }
            }
            case uint16 -> {
                if (sourceValTypeName.equals("short")) {
                    funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedShort" + (bigEndian ? "" : "LE") + "()";
                }
            }
            case uint24 -> {
                funcName = varNameByteBuf + ".readUnsignedMedium" + (bigEndian ? "" : "LE") + "()";
            }
            case uint32 -> {
                if (sourceValTypeName.equals("int")) {
                    funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedInt" + (bigEndian ? "" : "LE") + "()";
                }
            }
            case uint40 -> {
                funcName = ParseUtil.format("{}.read_uint40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case uint48 -> {
                funcName = ParseUtil.format("{}.read_uint48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case uint56 -> {
                funcName = ParseUtil.format("{}.read_uint56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case int8 -> {
                funcName = varNameByteBuf + ".readByte()";
            }
            case int16 -> {
                funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
            }
            case int24 -> {
                funcName = varNameByteBuf + ".readMedium" + (bigEndian ? "" : "LE") + "()";
            }
            case int32 -> {
                funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
            }
            case int40 -> {
                funcName = ParseUtil.format("{}.read_int40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case int48 -> {
                funcName = ParseUtil.format("{}.read_int48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case int56 -> {
                funcName = ParseUtil.format("{}.read_int56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
            }
            case int64 -> {
                funcName = varNameByteBuf + ".readLong" + (bigEndian ? "" : "LE") + "()";
            }
            case float32 -> {
                funcName = varNameByteBuf + ".readFloat" + (bigEndian ? "" : "LE") + "()";
            }
            case float64 -> {
                funcName = varNameByteBuf + ".readDouble" + (bigEndian ? "" : "LE") + "()";
            }
            default -> {
                funcName = null;
            }
        }
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        final String varNameArrayElement = varNameField + "_arrEle";
        ParseUtil.append(body, "final {} {}=({}){};\n", sourceValTypeName, varNameArrayElement, sourceValTypeName, funcName);
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skipBytes({});\n", varNameByteBuf, singleSkip);
        }
        //表达式运算
        String valCode = ParseUtil.replaceValExprToCode(singleValExpr, varNameArrayElement);
        if (arrayElementType.isEnum()) {
            ParseUtil.append(body, "{}[i]={}.fromInteger((int)({}));\n", arrVarName, arrayElementTypeName, valCode);
        } else {
            //格式化精度
            if ((arrayElementType == float.class || arrayElementType == double.class) && anno.singlePrecision() >= 0) {
                valCode = ParseUtil.format("{}.round((double){},{})", ParseUtil.class.getName(), valCode, anno.singlePrecision());
            }
            ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, valCode);
        }
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);

        return true;
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);
        final Class<?> fieldTypeClass = field.getType();
        final NumType singleType = anno.singleType();
        final int singleSkip = anno.singleSkip();
        final StringBuilder body = context.method_body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        final String singleValExpr = anno.singleValExpr();
        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);

        ParseUtil.append(body, "if({}!=null){\n", valCode);

        if (byte[].class.isAssignableFrom(fieldTypeClass) && (singleType == NumType.int8 || singleType == NumType.uint8) && singleValExpr.isEmpty() && singleSkip == 0) {
            ParseUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, valCode);
        } else {
            final Class<?> arrayElementType = fieldTypeClass.componentType();
            final boolean isFloat = arrayElementType == float.class || arrayElementType == double.class;
            final String arrayElementTypeName = arrayElementType.getName();

            String varNameFieldArr = varNameField + "_arr";
            ParseUtil.append(body, "final {}[] {}={};\n", arrayElementTypeName, varNameFieldArr, valCode);
            ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            String varNameFieldArrEle = varNameField + "_arrEle";
            ParseUtil.append(body, "final {} {}={}[i];\n", arrayElementTypeName, varNameFieldArrEle, varNameFieldArr);
            String arrEleValCode = varNameFieldArrEle;
            if (arrayElementType.isEnum()) {
                arrEleValCode = ParseUtil.format("({}).toInteger()", arrEleValCode);
            }
            if (!singleValExpr.isEmpty()) {
                if (isFloat) {
                    arrEleValCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(singleValExpr), arrEleValCode);
                } else {
                    arrEleValCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(singleValExpr), arrEleValCode);
                }
            }
            final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
            switch (singleType) {
                case uint8, int8 -> {
                    String funcName = "writeByte";
                    ParseUtil.append(body, "{}.{}((int)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case uint16, int16 -> {
                    String funcName = "writeShort" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((int)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case uint24, int24 -> {
                    String funcName = "writeMedium" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((int)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case uint32, int32 -> {
                    String funcName = "writeInt" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((int)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case uint40, int40 -> {
                    String funcName = "write_int40" + (bigEndian ? "" : "_le");
                    ParseUtil.append(body, "{}.{}({},(long)({}));\n", FieldBuilder__F_num.class.getName(), funcName, varNameByteBuf, arrEleValCode);
                }
                case uint48, int48 -> {
                    String funcName = "write_int48" + (bigEndian ? "" : "_le");
                    ParseUtil.append(body, "{}.{}({},(long)({}));\n", FieldBuilder__F_num.class.getName(), funcName, varNameByteBuf, arrEleValCode);
                }
                case uint56, int56 -> {
                    String funcName = "write_int56" + (bigEndian ? "" : "_le");
                    ParseUtil.append(body, "{}.{}({},(long)({}));\n", FieldBuilder__F_num.class.getName(), funcName, varNameByteBuf, arrEleValCode);
                }
                case uint64, int64 -> {
                    String funcName = "writeLong" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((long)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case float32 -> {
                    String funcName = "writeFloat" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((float)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                case float64 -> {
                    String funcName = "writeDouble" + (bigEndian ? "" : "LE");
                    ParseUtil.append(body, "{}.{}((double)({}));\n", varNameByteBuf, funcName, arrEleValCode);
                }
                default -> {
                }
            }
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, singleSkip);
            }
            ParseUtil.append(body, "}\n");
        }
        ParseUtil.append(body, "}\n");
    }

}
