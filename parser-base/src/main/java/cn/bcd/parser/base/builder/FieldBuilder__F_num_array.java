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
        final Class<?> arrEleType = fieldTypeClass.componentType();
        final String arrEleTypeName = arrEleType.getName();
        final String arrEleValTypeName = ParseUtil.getNumFieldValType(context).getName();
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);

        if (arrEleType != NumVal_byte.class && arrEleType != NumVal_short.class
                && arrEleType != NumVal_int.class && arrEleType != NumVal_long.class
                && arrEleType != NumVal_float.class && arrEleType != NumVal_double.class) {
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
        String varNameArr = varNameField + "_arr";
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final int singleSkip = anno.singleSkip();
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrEleTypeName, varNameArr, arrEleTypeName, arrLenRes);
        String funcName;
        String singleRawValTypeName;
        switch (singleType) {
            case uint8 -> {
                if (arrEleValTypeName.equals("byte")) {
                    funcName = varNameByteBuf + ".readByte()";
                    singleRawValTypeName = "byte";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedByte()";
                    singleRawValTypeName = "short";
                }
            }
            case uint16 -> {
                if (arrEleValTypeName.equals("short")) {
                    funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
                    singleRawValTypeName = "short";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedShort" + (bigEndian ? "" : "LE") + "()";
                    singleRawValTypeName = "int";
                }
            }
            case uint24 -> {
                funcName = varNameByteBuf + ".readUnsignedMedium" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "int";
            }
            case uint32 -> {
                if (arrEleValTypeName.equals("int")) {
                    funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
                    singleRawValTypeName = "int";
                } else {
                    funcName = varNameByteBuf + ".readUnsignedInt" + (bigEndian ? "" : "LE") + "()";
                    singleRawValTypeName = "long";
                }
            }
            case uint40 -> {
                funcName = ParseUtil.format("{}.read_uint40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case uint48 -> {
                funcName = ParseUtil.format("{}.read_uint48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case uint56 -> {
                funcName = ParseUtil.format("{}.read_uint56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case uint64 -> {
                funcName = ParseUtil.format("{}.readLong{}()", FieldBuilder.varNameByteBuf, bigEndian ? "" : "LE");
                singleRawValTypeName = "long";
            }
            case int8 -> {
                funcName = varNameByteBuf + ".readByte()";
                singleRawValTypeName = "byte";
            }
            case int16 -> {
                funcName = varNameByteBuf + ".readShort" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "short";
            }
            case int24 -> {
                funcName = varNameByteBuf + ".readMedium" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "int";
            }
            case int32 -> {
                funcName = varNameByteBuf + ".readInt" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "int";
            }
            case int40 -> {
                funcName = ParseUtil.format("{}.read_int40{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case int48 -> {
                funcName = ParseUtil.format("{}.read_int48{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case int56 -> {
                funcName = ParseUtil.format("{}.read_int56{}({})", FieldBuilder__F_num.class.getName(), bigEndian ? "" : "_le", FieldBuilder.varNameByteBuf);
                singleRawValTypeName = "long";
            }
            case int64 -> {
                funcName = varNameByteBuf + ".readLong" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "long";
            }
            case float32 -> {
                funcName = varNameByteBuf + ".readFloat" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "float";
            }
            case float64 -> {
                funcName = varNameByteBuf + ".readDouble" + (bigEndian ? "" : "LE") + "()";
                singleRawValTypeName = "double";
            }
            default -> {
                funcName = null;
                singleRawValTypeName = null;
            }
        }
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameArr);

        //读取原始数据
        String varNameArrEleRawVal = varNameField + "_arrEleRawVal";
        ParseUtil.append(body, "final {} {}={};\n", singleRawValTypeName, varNameArrEleRawVal, funcName);

        //跳过数据
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skipBytes({});\n", varNameByteBuf, singleSkip);
        }


        //获取值类型
        String varNameArrEleNumValType = varNameField + "_arrEleNumValType";
        String varNameNumValGetter = context.getNumValGetterVarName();
        ParseUtil.append(body, "final int {}={}.getType({}.{},{});\n",
                varNameArrEleNumValType, varNameNumValGetter,
                NumType.class.getName(), singleType.name(), varNameArrEleRawVal);

        //判断值类型
        ParseUtil.append(body, "if({}==0){\n", varNameArrEleNumValType);

        //计算表达式、格式化精度
        String arrEleRawValCode;
        if (singleRawValTypeName.equals(arrEleValTypeName)) {
            arrEleRawValCode = varNameArrEleRawVal;
        } else {
            arrEleRawValCode = ParseUtil.format("({}){}", arrEleValTypeName, varNameArrEleRawVal);
        }
        String varNameArrEleExprVal = varNameField + "_arrEleExprVal";
        if ((arrEleValTypeName.equals("float") || arrEleValTypeName.equals("double")) && anno.singlePrecision() >= 0) {
            ParseUtil.append(body, "final {} {}=({}){}.round((double){},{});\n",
                    arrEleValTypeName,
                    varNameArrEleExprVal,
                    arrEleValTypeName,
                    ParseUtil.class.getName(),
                    ParseUtil.replaceValExprToCode(singleValExpr, arrEleRawValCode),
                    anno.singlePrecision());
        } else {
            ParseUtil.append(body, "final {} {}=({})({});\n",
                    arrEleValTypeName,
                    varNameArrEleExprVal,
                    arrEleValTypeName,
                    ParseUtil.replaceValExprToCode(singleValExpr, arrEleRawValCode));
        }

        //设置值
        ParseUtil.append(body, "{}[i]=new {}(0,{});\n", varNameArr, arrEleTypeName, varNameArrEleExprVal);

        ParseUtil.append(body, "}else{\n");

        //设置值
        ParseUtil.append(body, "{}[i]=new {}({},{});\n", varNameArr, arrEleTypeName, varNameArrEleNumValType, ParseUtil.getNumValDefaultValue(context));

        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), varNameArr);

        return true;
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        if (buildDeParse_numVal(context)) {
            return;
        }
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
            ParseUtil.append(body,FieldBuilder__F_num.getWriteCode(singleType, bigEndian,arrEleValCode));
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, singleSkip);
            }
            ParseUtil.append(body, "}\n");
        }
        ParseUtil.append(body, "}\n");
    }

    public boolean buildDeParse_numVal(BuilderContext context) {
        final Field field = context.field;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);
        final Class<?> arrEleType = field.getType().componentType();
        final String arrEleTypeName = arrEleType.getName();
        final String arrEleRawValTypeName = ParseUtil.getNumFieldValType(context).getName();
        final NumType singleType = anno.singleType();
        final int singleSkip = anno.singleSkip();
        final StringBuilder body = context.method_body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        final String singleValExpr = anno.singleValExpr();
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final String varNameField = ParseUtil.getFieldVarName(context);


        if (arrEleType != NumVal_byte.class && arrEleType != NumVal_short.class
                && arrEleType != NumVal_int.class && arrEleType != NumVal_long.class
                && arrEleType != NumVal_float.class && arrEleType != NumVal_double.class) {
            return false;
        }

        ParseUtil.append(body, "if({}.{}!=null){\n", varNameInstance, fieldName);


        String varNameArr = varNameField + "_arr";
        ParseUtil.append(body, "final {}[] {}={}.{};\n", arrEleTypeName, varNameArr, varNameInstance, fieldName);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameArr);

        //取出数组值
        String varNameArrEle = varNameField + "_arrEle";
        ParseUtil.append(body, "final {} {}={}[i];\n", arrEleTypeName, varNameArrEle, varNameArr);

        //获取值类型
        String varNameArrEleNumValType = varNameField + "_arrEleNumValType";
        ParseUtil.append(body, "final int {}={}.type();\n",
                varNameArrEleNumValType, varNameArrEle);
        final boolean isFloat = arrEleRawValTypeName.equals("float") || arrEleRawValTypeName.equals("double");

        //判断值类型正常
        ParseUtil.append(body, "if({}==0){\n", varNameArrEleNumValType);

        //取出值
        String varNameArrEleRawVal = varNameField + "_arrEleRawVal";
        ParseUtil.append(body, "final {} {}={}.val();\n",
                arrEleRawValTypeName, varNameArrEleRawVal, varNameArrEle);

        //计算表达式
        String varNameArrEleExprVal;
        if (singleValExpr.isEmpty()) {
            varNameArrEleExprVal = varNameArrEleRawVal;
        } else {
            String arrEleExprValCode;
            if (isFloat) {
                arrEleExprValCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(singleValExpr), varNameArrEleRawVal);
            } else {
                arrEleExprValCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(singleValExpr), varNameArrEleRawVal);
            }
            varNameArrEleExprVal = varNameField + "_arrEleExprVal";
            ParseUtil.append(body, "final {} {}=({}){};\n",
                    arrEleRawValTypeName, varNameArrEleExprVal, arrEleRawValTypeName, arrEleExprValCode);
        }

        //写入
        ParseUtil.append(body, FieldBuilder__F_num.getWriteCode(anno.singleType(), bigEndian, varNameArrEleExprVal));

        //写入0
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, singleSkip);
        }

        ParseUtil.append(body, "}else{\n");

        String varNameNumValGetter = context.getNumValGetterVarName();

        String funcSuffix = switch (singleType) {
            case uint8, int8, uint16, int16, uint24, int24, uint32, int32 -> "int";
            case uint40, int40, uint48, int48, uint56, int56, uint64, int64 -> "long";
            default -> null;
        };

        String arrEleValCode = ParseUtil.format("{}.getVal_{}({}.{},{})", varNameNumValGetter, funcSuffix, NumType.class.getName(), singleType.name(), varNameArrEleNumValType);
        //写入
        ParseUtil.append(body, FieldBuilder__F_num.getWriteCode(anno.singleType(), bigEndian, arrEleValCode));

        //写入0
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, singleSkip);
        }


        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "}\n");
        return true;
    }


}
