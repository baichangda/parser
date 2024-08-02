package cn.bcd.parser.base;

import cn.bcd.parser.base.anno.*;
import cn.bcd.parser.base.builder.BuilderContext;
import cn.bcd.parser.base.builder.FieldBuilder;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.processor.ProcessContext;
import cn.bcd.parser.base.processor.Processor;
import cn.bcd.parser.base.util.BitBuf_reader_log;
import cn.bcd.parser.base.util.BitBuf_writer_log;
import cn.bcd.parser.base.util.LogUtil;
import cn.bcd.parser.base.util.ParseUtil;
import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import javassist.*;
import javassist.bytecode.SignatureAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 解析器
 * 配合注解完成解析工作
 * 会扫描当前类和其父类的所有字段
 * 会忽视如下字段
 * 1、没有被{@link Parser#anno_fieldBuilder}中注解标注的字段
 * 2、static或者final修饰的字段
 * 3、非public字段
 * 解析字段的顺序为 父类字段在子类之前
 *
 * <p>
 * 工作原理:
 * 使用javassist框架配合自定义注解、生成一套解析代码
 * 使用方法:
 * 1、首先获取类处理器
 * {@link #getProcessor(Class)}
 * {@link #getProcessor(Class, ByteOrder, BitOrder)}
 * 2、调用解析或者反解析
 * <p>
 * 解析调用入口:
 * {@link Processor#process(ByteBuf, ProcessContext)}
 * <p>
 * 反解析调用入口:
 * {@link Processor#deProcess(ByteBuf, ProcessContext, Object)}
 * <p>
 * 性能表现:
 * 由于是字节码增强技术、和手动编写代码解析效率一样
 * <p>
 * 可配置方法
 * {@link #enableGenerateClassFile()} 生成class类文件、文件声称在{@link Processor}同目录下
 * {@link #enablePrintBuildLog()} 开启打印build日志
 * {@link #withDefaultLogCollector_parse()} 开启解析日志采集、此方法开启后会在解析程序中插入日志采集功能、降低程序性能、建议只在开发调试阶段开启
 * {@link #withDefaultLogCollector_deParse()} 开启反解析日志采集、此方法开启后会在反解析程序中插入日志采集功能、降低程序性能、建议只在开发调试阶段开启
 * <p>
 * 注意:
 * 如果启动了解析和反解析日志、并不是所有字段都会打印、逻辑参考
 * {@link ParseUtil#needLog(BuilderContext)}
 */
public class Parser {
    private final static Logger logger = LoggerFactory.getLogger(Parser.class);

    public final static Map<Class<? extends Annotation>, FieldBuilder> anno_fieldBuilder;

    static {
        anno_fieldBuilder = ParseUtil.getAllFieldBuild();
    }

    public final static Map<String, Processor<?>> beanProcessorKey_processor = new HashMap<>();
    /**
     * 解析log采集器
     * 需要注意的是、此功能用于调试、会在生成的class中加入日志代码、影响性能
     * 而且此功能开启时候避免多线程调用解析、会产生日志混淆、不易调试
     */
    public static LogCollector_parse logCollector_parse;
    public static LogCollector_deParse logCollector_deParse;
    /**
     * 是否在src/main/java下面生成class文件
     * 主要用于开发测试阶段、便于查看生成的结果
     */
    private static boolean generateClassFile = false;
    /**
     * 是否打印javassist生成class的过程日志
     */
    private static boolean printBuildLog = false;

    /**
     *  禁用ByteBuf检查
     *  这样做会使性能提高10%~20%
     *  原因是在读取{@link ByteBuf}时候会进行如下两个检查
     *  {@link AbstractByteBuf#checkAccessible} 可访问检查
     *  {@link AbstractByteBuf#checkBounds} 边界检查
     */
    public static void disableByteBufCheck() {
        System.setProperty("io.netty.buffer.checkBounds", "false");
        System.setProperty("io.netty.buffer.checkAccessible", "false");
    }

    public static void withDefaultLogCollector_parse() {
        logCollector_parse = new LogCollector_parse() {
            /**
             * @param clazz
             * @param type 1、C_skip
             * @param args
             */
            public void collect_class(Class<?> clazz, int type, Object... args) {
                switch (type) {
                    case 1 -> {
                        logger.info("--parse skip class{} {}", LogUtil.getFieldStackTrace(clazz, null), args[0]);
                    }
                    default -> {
                    }
                }
            }

            /**
             *
             * @param clazz
             * @param fieldName
             * @param type 1：F_skip
             *             2、F_bit_num
             *             0、other
             * @param args
             */
            @Override
            public void collect_field(Class<?> clazz, String fieldName, int type, Object... args) {
                try {
                    Field field = clazz.getField(fieldName);
                    Class<?> fieldDeclaringClass = field.getDeclaringClass();
                    switch (type) {
                        case 1 -> {
                            byte[] content = (byte[]) args[0];
                            logger.info("--parse skip field{}--[{}].[{}] skip len[{}] hex[{}]"
                                    , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                    , clazz.getSimpleName()
                                    , fieldName
                                    , content.length
                                    , ByteBufUtil.hexDump(content).toUpperCase()
                            );
                        }
                        case 2 -> {
                            BitBuf_reader_log.Log[] logs = (BitBuf_reader_log.Log[]) args[0];
                            Object val = args[1];
                            for (BitBuf_reader_log.Log log : logs) {
                                logger.info("--parse bit field{}--[{}].[{}] val[{}] {}"
                                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                        , clazz.getSimpleName()
                                        , fieldName
                                        , val
                                        , log.msg());
                            }
                        }
                        default -> {
                            byte[] content = (byte[]) args[0];
                            Object val = args[1];
                            logger.info("--parse field{}--[{}].[{}] [{}]->[{}]"
                                    , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                    , clazz.getSimpleName()
                                    , fieldName
                                    , ByteBufUtil.hexDump(content).toUpperCase()
                                    , val
                            );
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw ParseException.get(e);
                }
            }
        };

    }

    public static void withDefaultLogCollector_deParse() {
        logCollector_deParse = new LogCollector_deParse() {

            @Override
            public void collect_class(Class<?> clazz, int type, Object... args) {
                switch (type) {
                    case 1 -> {
                        logger.info("--deParse skip class{} {}", LogUtil.getFieldStackTrace(clazz, null), args[0]);
                    }
                    default -> {
                    }
                }
            }

            @Override
            public void collect_field(Class<?> clazz, String fieldName, int type, Object... args) {
                try {
                    Field field = clazz.getField(fieldName);
                    Class<?> fieldDeclaringClass = field.getDeclaringClass();
                    switch (type) {
                        case 1 -> {
                            byte[] content = (byte[]) args[0];
                            logger.info("--deParse skip field{}--[{}].[{}] append len[{}] [{}]"
                                    , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                    , clazz.getSimpleName()
                                    , fieldName
                                    , content.length
                                    , ByteBufUtil.hexDump(content).toUpperCase());
                        }
                        case 2 -> {
                            Object val = args[0];
                            BitBuf_writer_log.Log[] logs = (BitBuf_writer_log.Log[]) args[1];
                            for (BitBuf_writer_log.Log log : logs) {
                                logger.info("--deParse bit field{}--[{}].[{}] val[{}] {}"
                                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                        , clazz.getSimpleName()
                                        , fieldName
                                        , val
                                        , log.msg());
                            }
                        }
                        default -> {
                            Object val = args[0];
                            byte[] content = (byte[]) args[1];
                            logger.info("--deParse field{}--[{}].[{}] [{}]->[{}]"
                                    , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                                    , clazz.getSimpleName()
                                    , fieldName
                                    , val
                                    , ByteBufUtil.hexDump(content).toUpperCase());
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw ParseException.get(e);
                }
            }

        };
    }

    public static void enablePrintBuildLog() {
        printBuildLog = true;
    }

    public static void enableGenerateClassFile() {
        generateClassFile = true;
    }

    private static void buildMethodBody_process(BuilderContext context) {
        final List<Field> fieldList = context.class_fieldList;
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_parse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            context.fieldIndex = i;
            boolean bitField = field.isAnnotationPresent(F_bit_num.class) || field.isAnnotationPresent(F_bit_num_array.class);
            F_skip f_skip = field.getAnnotation(F_skip.class);
            if (f_skip != null && (f_skip.lenBefore() != 0 || !f_skip.lenExprBefore().isEmpty())) {
                ParseUtil.appendSkip_parse(f_skip.lenBefore(), f_skip.lenExprBefore(), context);
            }
            if (logCollector_parse != null) {
                if (!bitField) {
                    ParseUtil.prependLogCode_parse(context);
                }
            }
            try {
                for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : anno_fieldBuilder.entrySet()) {
                    Class<? extends Annotation> annoClass = entry.getKey();
                    if (field.isAnnotationPresent(annoClass)) {
                        entry.getValue().buildParse(context);
                    }
                }
            } finally {
                if (logCollector_parse != null) {
                    if (bitField) {
                        ParseUtil.appendBitLogCode_parse(context);
                    } else {
                        ParseUtil.appendLogCode_parse(context);
                    }
                }
            }
            if (f_skip != null && (f_skip.lenAfter() != 0 || !f_skip.lenExprAfter().isEmpty())) {
                ParseUtil.appendSkip_parse(f_skip.lenAfter(), f_skip.lenExprAfter(), context);
            }
        }

    }

    private static void buildMethodBody_deProcess(BuilderContext context) {
        final List<Field> fieldList = context.class_fieldList;
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_deParse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            context.fieldIndex = i;
            boolean logBit = field.isAnnotationPresent(F_bit_num.class);
            F_skip f_skip = field.getAnnotation(F_skip.class);
            if (f_skip != null && (f_skip.lenBefore() != 0 || !f_skip.lenExprBefore().isEmpty())) {
                ParseUtil.appendSkip_deParse(f_skip.lenBefore(), f_skip.lenExprBefore(), context);
            }
            if (logCollector_deParse != null) {
                if (!logBit) {
                    ParseUtil.prependLogCode_deParse(context);
                }
            }
            try {
                for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : anno_fieldBuilder.entrySet()) {
                    Class<? extends Annotation> annoClass = entry.getKey();
                    if (field.isAnnotationPresent(annoClass)) {
                        entry.getValue().buildDeParse(context);
                    }
                }
            } finally {
                if (logCollector_deParse != null) {
                    if (logBit) {
                        ParseUtil.appendBitLogCode_deParse(context);
                    } else {
                        ParseUtil.appendLogCode_deParse(context);
                    }
                }
            }
            if (f_skip != null && (f_skip.lenAfter() != 0 || !f_skip.lenExprAfter().isEmpty())) {
                ParseUtil.appendSkip_deParse(f_skip.lenAfter(), f_skip.lenExprAfter(), context);
            }
        }
    }

    private static <T> Class<T> buildClass(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder) throws CannotCompileException, NotFoundException, IOException {
        final String processor_class_name = Processor.class.getName();
        final String byteBufClassName = ByteBuf.class.getName();
        final String clazzName = clazz.getName();

        String implProcessor_class_name = ParseUtil.getProcessorClassName(clazz, byteOrder, bitOrder);
        final CtClass cc = ClassPool.getDefault().makeClass(implProcessor_class_name);

        //添加泛型
        SignatureAttribute.ClassSignature class_cs = new SignatureAttribute.ClassSignature(null, null, new SignatureAttribute.ClassType[]{
                new SignatureAttribute.ClassType(processor_class_name, new SignatureAttribute.TypeArgument[]{
                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(clazzName))
                })
        });
        cc.setGenericSignature(class_cs.encode());

        cc.setModifiers(Modifier.FINAL | Modifier.PUBLIC);

        StringBuilder classFieldDefineBody = new StringBuilder();
        StringBuilder constructBody = new StringBuilder();
        final CtConstructor constructor = CtNewConstructor.make(new CtClass[]{}, null, cc);
        final Map<String, String> classVarDefineToVarName = new HashMap<>();

        //添加实现、定义process方法
        final CtClass interface_cc = ClassPool.getDefault().get(processor_class_name);
        cc.addInterface(interface_cc);
        final CtMethod process_cm = CtNewMethod.make(
                /**
                 * 在这里定义返回值为Object类型
                 * 因为正常的继承、asm实现方法需要额外创建一个桥接方法、针对泛型部分的参数为Object类型
                 */
                ClassPool.getDefault().get(Object.class.getName()),
                "process",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName())
                }, null, null, cc);

        cc.addMethod(process_cm);
        //process方法体
        StringBuilder processBody = new StringBuilder();
        processBody.append("\n{\n");
        ParseUtil.append(processBody, "final {} {}=new {}();\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        final List<Field> fieldList = ParseUtil.getParseFields(clazz);
        BuilderContext parseBuilderContext = new BuilderContext(classFieldDefineBody, constructBody, processBody, clazz, cc, classVarDefineToVarName, byteOrder, bitOrder, fieldList);

        C_skip c_skip = clazz.getAnnotation(C_skip.class);
        if (c_skip == null) {
            buildMethodBody_process(parseBuilderContext);
        } else {
            int classByteLen = ParseUtil.getClassByteLenIfPossible(clazz);
            if (classByteLen == -1) {
                ParseUtil.append(processBody, "final int {}={}.readerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
                buildMethodBody_process(parseBuilderContext);
                String lenValCode;
                if (c_skip.len() == 0) {
                    lenValCode = ParseUtil.replaceExprToCode_class(c_skip.lenExpr(), parseBuilderContext);
                } else {
                    lenValCode = c_skip.len() + "";
                }
                ParseUtil.append(processBody, "final int {}={}-{}.readerIndex()+{};\n", FieldBuilder.varNameShouldSkip, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(processBody, "if({}>0){\n", FieldBuilder.varNameShouldSkip);
                ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, FieldBuilder.varNameShouldSkip);
                if (logCollector_parse != null) {
                    ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,1,new Object[]{\"@C_skip skip[\"+{}+\"]\"});\n", Parser.class.getName(), clazzName, FieldBuilder.varNameShouldSkip);
                }
                ParseUtil.append(processBody, "}\n");
            } else {
                buildMethodBody_process(parseBuilderContext);
                if (c_skip.len() == 0) {
                    String lenValCode = ParseUtil.replaceExprToCode(c_skip.lenExpr(), parseBuilderContext);
                    String skipCode = "(" + lenValCode + "-" + classByteLen + ")";
                    ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipCode);
                    if (logCollector_parse != null) {
                        ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,1,new Object[]{\"@C_skip skip[\"+{}+\"]\"});\n", Parser.class.getName(), clazzName, skipCode);
                    }
                } else {
                    int skip = c_skip.len() - classByteLen;
                    if (skip > 0) {
                        ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skip);
                        if (logCollector_parse != null) {
                            ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,1,new Object[]{\"@C_skip skip[{}]\"});\n", Parser.class.getName(), clazzName, skip);
                        }
                    }
                }
            }
        }
        ParseUtil.append(processBody, "return {};\n", FieldBuilder.varNameInstance);
        processBody.append("}");

        //添加实现、定义deProcess方法
        final CtMethod deProcess_cm = CtNewMethod.make(
                ClassPool.getDefault().get(void.class.getName()),
                "deProcess",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName()),
                        ClassPool.getDefault().get(Object.class.getName()),
                }, null, null, cc);

        cc.addMethod(deProcess_cm);
        //deProcess方法体
        StringBuilder deProcessBody = new StringBuilder();
        deProcessBody.append("\n{\n");
        ParseUtil.append(deProcessBody, "final {} {}=({})$3;\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        BuilderContext deParseBuilderContext = new BuilderContext(classFieldDefineBody, constructBody, deProcessBody, clazz, cc, classVarDefineToVarName, byteOrder, bitOrder, fieldList);
        if (c_skip == null) {
            buildMethodBody_deProcess(deParseBuilderContext);
        } else {
            int classByteLen = ParseUtil.getClassByteLenIfPossible(clazz);
            if (classByteLen == -1) {
                ParseUtil.append(deProcessBody, "final int {}={}.writerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
                buildMethodBody_deProcess(deParseBuilderContext);
                String lenValCode;
                if (c_skip.len() == 0) {
                    lenValCode = ParseUtil.replaceExprToCode(c_skip.lenExpr(), parseBuilderContext);
                } else {
                    lenValCode = c_skip.len() + "";
                }
                ParseUtil.append(deProcessBody, "final int {}={}-{}.writerIndex()+{};\n", FieldBuilder.varNameShouldSkip, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(deProcessBody, "if({}>0){\n", FieldBuilder.varNameShouldSkip);
                ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, FieldBuilder.varNameShouldSkip);
                if (logCollector_parse != null) {
                    ParseUtil.append(deProcessBody, "{}.logCollector_deParse.collect_class({}.class,1,new Object[]{\"@C_skip append[\"+{}+\"]\"});\n", Parser.class.getName(), clazzName, FieldBuilder.varNameShouldSkip);
                }
                ParseUtil.append(deProcessBody, "}\n");
            } else {
                buildMethodBody_deProcess(deParseBuilderContext);
                if (c_skip.len() == 0) {
                    String lenValCode = ParseUtil.replaceExprToCode(c_skip.lenExpr(), parseBuilderContext);
                    String skipCode = "(" + lenValCode + "-" + classByteLen + ")";
                    ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, skipCode);
                    if (logCollector_deParse != null) {
                        ParseUtil.append(deProcessBody, "{}.logCollector_deParse.collect_class({}.class,1,new Object[]{\"@C_skip append[\"+{}+\"]\"});\n", Parser.class.getName(), clazzName, skipCode);
                    }
                } else {
                    int skip = c_skip.len() - classByteLen;
                    if (skip > 0) {
                        ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, skip);
                        if (logCollector_deParse != null) {
                            ParseUtil.append(deProcessBody, "{}.logCollector_deParse.collect_class({}.class,1,new Object[]{\"@C_skip append[{}]\"});\n", Parser.class.getName(), clazzName, skip);
                        }
                    }
                }
            }
        }
        deProcessBody.append("}");


        //开始创建类
        if (printBuildLog) {
            logger.info("\n----------clazz[{}] class field define body-------------\n{}\n", clazz.getName(), classFieldDefineBody.toString());
        }

        constructBody.insert(0, "\n{\n");
        constructBody.append("}\n");
        if (printBuildLog) {
            logger.info("\n----------clazz[{}] constructor body-------------{}\n", clazz.getName(), constructBody.toString());
        }
        constructor.setBody(constructBody.toString());
        cc.addConstructor(constructor);

        if (printBuildLog) {
            logger.info("\n-----------class[{}] process-----------{}\n", clazz.getName(), processBody.toString());
        }
        process_cm.setBody(processBody.toString());

        if (printBuildLog) {
            logger.info("\n-----------class[{}] deProcess-----------{}\n", clazz.getName(), deProcessBody.toString());
        }
        deProcess_cm.setBody(deProcessBody.toString());

        if (generateClassFile) {
            cc.writeFile("src/main/java");
        }
        return (Class<T>) cc.toClass(Processor.class);
//        return cc.toClass();
    }

    /**
     * 获取类解析器
     * 使用默认字节序模式和位模式
     *
     * @param clazz 实体类类型
     * @param <T>
     * @return
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz) {
        return getProcessor(clazz, ByteOrder.Default, BitOrder.Default);
    }

    /**
     * 获取类解析器
     *
     * @param clazz     实体类类型
     * @param byteOrder 实体类字节码实现 字节序模式
     * @param bitOrder  实体类字节码实现 位模式
     * @param <T>
     * @return
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder) {
        final String key = ParseUtil.getProcessorKey(clazz, byteOrder, bitOrder);
        Processor<T> processor = (Processor<T>) beanProcessorKey_processor.get(key);
        if (processor == null) {
            synchronized (beanProcessorKey_processor) {
                processor = (Processor<T>) beanProcessorKey_processor.get(key);
                if (processor == null) {
                    try {
                        final Class<T> processClass = Parser.buildClass(clazz, byteOrder, bitOrder);
                        processor = (Processor<T>) processClass.getConstructor().newInstance();
                        beanProcessorKey_processor.put(key, processor);
                    } catch (CannotCompileException | NotFoundException | IOException | NoSuchMethodException |
                             InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw ParseException.get(e);
                    }
                }
            }
        }
        return processor;
    }

    public interface LogCollector_parse {

        void collect_class(Class<?> clazz, int type, Object... args);

        void collect_field(Class<?> clazz, String fieldName, int type, Object... args);
    }


    public interface LogCollector_deParse {
        void collect_field(Class<?> clazz, String fieldName, int type, Object... args);

        void collect_class(Class<?> clazz, int type, Object... args);

    }
}
