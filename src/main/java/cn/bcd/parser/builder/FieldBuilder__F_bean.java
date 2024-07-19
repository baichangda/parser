package cn.bcd.parser.builder;

import cn.bcd.parser.anno.C_impl;
import cn.bcd.parser.anno.F_bean;
import cn.bcd.parser.exception.ParseException;
import cn.bcd.parser.util.ClassUtil;
import cn.bcd.parser.util.ParseUtil;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class FieldBuilder__F_bean extends FieldBuilder {

    final Map<Class<?>, List<Class<?>>> interfaceClassToImplClass = new HashMap<>();

    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final StringBuilder body = context.method_body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        String processContextVarName = context.getProcessContextVarName();
        String fieldTypeName = fieldType.getName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw ParseException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={};\n", varNameField_implClassVal, implClassValCode);
            //找到其实现类子类
            String pkg = fieldTypeName.substring(0, fieldTypeName.lastIndexOf("."));
            List<Class<?>> implClassList = interfaceClassToImplClass.get(fieldType);
            if (implClassList == null) {
                try {
                    implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg).stream()
                            .filter(e2 -> e2.isAnnotationPresent(C_impl.class))
                            .sorted(Comparator.comparing(Class::getName))
                            .toList();
                    interfaceClassToImplClass.put(fieldType, implClassList);
                }catch (IOException | ClassNotFoundException e){
                    throw ParseException.get(e);
                }
            }
            ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
            Class<?> defaultClass = null;
            for (Class<?> implClass : implClassList) {
                C_impl c_impl = implClass.getAnnotation(C_impl.class);
                int[] value = c_impl.value();
                List<Integer> filterValue = new ArrayList<>();
                for (int i : value) {
                    if (i == Integer.MAX_VALUE) {
                        defaultClass = implClass;
                    } else {
                        filterValue.add(i);
                    }
                }
                if (filterValue.isEmpty()) {
                    continue;
                }
                final String implProcessorVarName;
                if (c_impl.processorClass() == Void.class) {
                    implProcessorVarName = context.getProcessorVarName(implClass);
                } else {
                    implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                }
                for (int i = 0; i < filterValue.size() - 1; i++) {
                    ParseUtil.append(body, "case {}:{}\n", value[i]);
                }
                ParseUtil.append(body, "case {}:{\n{}.{}={}.process({},{});\nbreak;\n}\n",
                        filterValue.get(filterValue.size()-1),
                        varNameInstance,
                        field.getName(),
                        implProcessorVarName,
                        varNameByteBuf,
                        processContextVarName);
            }
            if (defaultClass == null) {
                ParseUtil.append(body, "default:{\nthrow {}.get(\"class[{}] field[{}] implClass value[\"+" + varNameField_implClassVal + "+\"] not support\");\n}",
                        ParseException.class.getName(),
                        field.getDeclaringClass().getName(),
                        field.getName());
            } else {
                C_impl c_impl = defaultClass.getAnnotation(C_impl.class);
                final String defaultImplProcessorVarName;
                if (c_impl.processorClass() == Void.class) {
                    defaultImplProcessorVarName = context.getProcessorVarName(defaultClass);
                } else {
                    defaultImplProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                }
                ParseUtil.append(body, "default:{\n{}.{}={}.process({},{});\n}\n",
                        varNameInstance,
                        field.getName(),
                        defaultImplProcessorVarName,
                        varNameByteBuf,
                        processContextVarName);
            }
            ParseUtil.append(body, "}\n");
        } else {
            final String processorVarName = context.getProcessorVarName(fieldType);
            ParseUtil.append(body, "{}.{}=({}){}.process({},{});\n",
                    varNameInstance,
                    field.getName(),
                    fieldTypeName,
                    processorVarName,
                    varNameByteBuf,
                    processContextVarName);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final Field field = context.field;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<?> fieldType = field.getType();
        String fieldTypeName = fieldType.getName();
        final String fieldName = field.getName();
        String processContextVarName = context.getProcessContextVarName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw ParseException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={};\n", varNameField_implClassVal, implClassValCode);
            String pkg = fieldTypeName.substring(0, fieldTypeName.lastIndexOf("."));
            List<Class<?>> implClassList = interfaceClassToImplClass.get(fieldType);
            if (implClassList == null) {
                try {
                    implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg).stream()
                            .filter(e2 -> e2.isAnnotationPresent(C_impl.class))
                            .sorted(Comparator.comparing(Class::getName))
                            .toList();
                    interfaceClassToImplClass.put(fieldType, implClassList);
                }catch (IOException | ClassNotFoundException e){
                    throw ParseException.get(e);
                }
            }
            ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
            Class<?> defaultClass = null;
            for (Class<?> implClass : implClassList) {
                C_impl c_impl = implClass.getAnnotation(C_impl.class);
                int[] value = c_impl.value();
                List<Integer> filterValue = new ArrayList<>();
                for (int i : value) {
                    if (i == Integer.MAX_VALUE) {
                        defaultClass = implClass;
                    } else {
                        filterValue.add(i);
                    }
                }
                if (filterValue.isEmpty()) {
                    continue;
                }
                final String implProcessorVarName;
                if (c_impl.processorClass() == Void.class) {
                    implProcessorVarName = context.getProcessorVarName(implClass);
                } else {
                    implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                }
                for (int i = 0; i < filterValue.size() - 1; i++) {
                    ParseUtil.append(body, "case {}:{}\n", value[i]);
                }
                ParseUtil.append(body, "case {}:{\n{}.deProcess({},{},({})({}));\nbreak;\n}\n",
                        filterValue.get(filterValue.size()-1),
                        implProcessorVarName,
                        varNameByteBuf,
                        processContextVarName,
                        implClass.getName(),
                        varNameInstance + "." + fieldName);
            }
            if (defaultClass == null) {
                ParseUtil.append(body, "default:{\nthrow {}.get(\"class[{}] field[{}] implClass value[\"+" + varNameField_implClassVal + "+\"] not support\");\n}",
                        ParseException.class.getName(),
                        field.getDeclaringClass().getName(),
                        field.getName(),
                        fieldTypeName);
            } else {
                C_impl c_impl = defaultClass.getAnnotation(C_impl.class);
                final String defaultImplProcessorVarName;
                if (c_impl.processorClass() == Void.class) {
                    defaultImplProcessorVarName = context.getProcessorVarName(defaultClass);
                } else {
                    defaultImplProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                }
                ParseUtil.append(body, "default:{\n{}.deProcess({},{},({})({}));\n}\n",
                        defaultImplProcessorVarName,
                        varNameByteBuf,
                        processContextVarName,
                        defaultClass.getName(),
                        varNameInstance + "." + fieldName);
            }
            ParseUtil.append(body, "}\n");
        } else {
            final String processorVarName = context.getProcessorVarName(fieldType);
            ParseUtil.append(body, "{}.deProcess({},{},{});\n",
                    processorVarName,
                    varNameByteBuf,
                    processContextVarName,
                    varNameInstance + "." + fieldName);
        }
    }

    public static void main(String[] args) throws CannotCompileException, IOException {
        CtClass cc = ClassPool.getDefault().makeClass("cn.bcd.parser.builder.TestSwitch");
        String body = """
                public void test(int i){
                    switch(i){
                        case 1,3->java.lang.System.out.println(1);
                        case 2->java.lang.System.out.println(2);
                    }
                }
                """;
//        String body = """
//                public void test(int i){
//                    switch(i){
//                        case 1:{}
//                        case 3:{
//                            java.lang.System.out.println(1);
//                            break;
//                        }
//                        case 2:{
//                            java.lang.System.out.println(2);
//                            break;
//                        }
//                    }
//                }
//                """;
        CtMethod cm = CtNewMethod.make(body, cc);
        cc.addMethod(cm);
        cc.writeFile("src/main/java");
        Class<?> aClass = cc.toClass(FieldBuilder__F_bean.class);
        System.out.println(aClass.getName());
    }

}
