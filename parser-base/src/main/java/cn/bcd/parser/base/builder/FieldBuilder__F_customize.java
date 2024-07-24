package cn.bcd.parser.base.builder;


import cn.bcd.parser.base.anno.F_customize;
import cn.bcd.parser.base.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_customize extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        ParseUtil.check_var(context, F_customize.class, anno.var(), anno.globalVar());
        final Class<?> processorClass = anno.processorClass();
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String processorClassVarName = context.getCustomizeProcessorVarName(processorClass, anno.processorArgs());
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldType = field.getType();
        final String fieldTypeClassName = fieldType.getName();
        final String processContextVarName = context.getProcessContextVarName();
        final String unBoxing = ParseUtil.unBoxing(ParseUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, processContextVarName), fieldType);
        if (anno.var() == '0') {
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), unBoxing);
        } else {
            ParseUtil.append(body, "final {} {}={};\n", fieldTypeClassName, varNameField, unBoxing);
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
            context.method_varToFieldName.put(anno.var(), varNameField);
        }

        final char globalVar = anno.globalVar();
        if (globalVar != '0') {
            ParseUtil.appendPutGlobalVar(context, globalVar, varNameField);
        }

    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        ParseUtil.check_var(context, F_customize.class, anno.var(), anno.globalVar());
        final Class<?> processorClass = anno.processorClass();
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varInstanceName = FieldBuilder.varNameInstance;
        char var = anno.var();
        final String valCode;
        if (var == '0') {
            valCode = varInstanceName + "." + field.getName();
        } else {
            ParseUtil.append(body, "final {} {}={};\n", field.getType().getName(), varNameField, varInstanceName + "." + field.getName());
            valCode = varNameField;
        }

        //判断是否用到全局变量中、如果用到了、添加进去
        if (anno.globalVar() != '0') {
            ParseUtil.appendPutGlobalVar(context, anno.globalVar(), valCode);
        }

        final String processContextVarName = context.getProcessContextVarName();
        final String processorClassVarName = context.getCustomizeProcessorVarName(processorClass, anno.processorArgs());
        ParseUtil.append(body, "{}.deProcess({},{},{});\n", processorClassVarName, FieldBuilder.varNameByteBuf, processContextVarName, valCode);
    }

}
