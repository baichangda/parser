package cn.bcd.parser.base.util;


import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.anno.*;
import cn.bcd.parser.base.anno.data.*;
import cn.bcd.parser.base.builder.BuilderContext;
import cn.bcd.parser.base.builder.FieldBuilder;
import cn.bcd.parser.base.exception.ParseException;
import cn.bcd.parser.base.processor.Processor;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ParseUtil {
    static Logger logger = LoggerFactory.getLogger(ParseUtil.class);


    static final double[] pows;

    static {
        pows = new double[11];
        for (int i = 0; i < pows.length; i++) {
            pows[i] = Math.pow(10, i);
        }
    }

    public static void check_var(BuilderContext context, Class<?> annoClass, char var, char globalVar) {
        if (var != '0' && (var < 'a' || var > 'z')) {
            throw ParseException.get("class[{}] field[{}] anno[{}] var[{}] not in [a-z]", context.clazz.getName(), context.field.getName(), annoClass.getName(), var);
        }
        if (globalVar != '0' && (globalVar < 'A' || globalVar > 'Z')) {
            throw ParseException.get("class[{}] field[{}] anno[{}] globalVar[{}] not in [A-Z]", context.clazz.getName(), context.field.getName(), annoClass.getName(), globalVar);
        }
    }

    public static void notSupport_type(BuilderContext context, Class<?> annoClass) {
        throw ParseException.get("class[{}] field[{}] anno[{}] type not support", context.clazz.getName(), context.field.getName(), annoClass.getName());
    }

    public static void notSupport_fieldType(BuilderContext context, Class<?> annoClass) {
        throw ParseException.get("class[{}] field[{}] anno[{}] not support", context.clazz.getName(), context.field.getName(), annoClass.getName());
    }

    public static void notSupport(BuilderContext context, String msg, Object... params) {
        Object[] args = new Object[params.length + 2];
        args[0] = context.clazz.getName();
        args[1] = context.field.getName();
        System.arraycopy(params, 0, args, 2, params.length);
        throw ParseException.get("class[{}] field[{}] " + msg, args);
    }

    public static boolean bigEndian(BitOrder order, BitOrder parentOrder) {
        if (parentOrder == null) {
            if (order == BitOrder.Default) {
                return true;
            } else {
                return order == BitOrder.bigEndian;
            }
        } else {
            if (order == BitOrder.Default) {
                if (parentOrder == BitOrder.Default) {
                    return true;
                } else {
                    return parentOrder == BitOrder.bigEndian;
                }
            } else {
                return order == BitOrder.bigEndian;
            }
        }
    }

    public static boolean bigEndian(ByteOrder order, ByteOrder parentOrder) {
        if (parentOrder == null) {
            if (order == ByteOrder.Default) {
                return true;
            } else {
                return order == ByteOrder.bigEndian;
            }
        } else {
            if (order == ByteOrder.Default) {
                if (parentOrder == ByteOrder.Default) {
                    return true;
                } else {
                    return parentOrder == ByteOrder.bigEndian;
                }
            } else {
                return order == ByteOrder.bigEndian;
            }
        }
    }

    /**
     * 定义类变量、解析其中的变量名称
     *
     * @param context
     * @param valDefine
     * @param params
     * @return
     */
    public static String defineClassVar(final BuilderContext context, Class<?> varClass, final String valDefine, Object... params) {
        return defineClassVar(context, null, varClass, varClass.getSimpleName(), valDefine, params);
    }

    public static String defineClassVar(final BuilderContext context, Consumer<String> doAfterDefine, Class<?> varClass, String varNameSuffix, final String valDefine, Object... params) {
        return context.class_varDefineToVarName.computeIfAbsent(format(valDefine, params), k -> {
            final int size = context.class_varDefineToVarName.size();
            final String varName = "_" + size + "_" + varNameSuffix;
            final CtClass ctClass = context.implCc;
            String define = "private final " + varClass.getName() + " " + varName + "=" + k + ";\n";
            context.class_fieldDefineBody.append(define);
            try {
                final CtField ctField = CtField.make(define, ctClass);
                ctClass.addField(ctField);
            } catch (CannotCompileException e) {
                throw ParseException.get(e);
            }
            if (doAfterDefine != null) {
                doAfterDefine.accept(varName);
            }
            return varName;
        });
    }


    private static String getFieldByteBufReaderIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_readerIndex";
    }

    private static String getFieldByteBufWriterIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_writerIndex";
    }

    private static String getFieldLogBytesVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_bytes";
    }

    /**
     * 当日志开启情况下
     * 判断字段是否应该记录日志
     * 如下注解的字段不会记录日志
     * {@link F_bean}
     * {@link F_bean_list}
     * <p>
     * 注意bit注解字段会记录日志
     * {@link F_bit_num}
     * {@link F_bit_num_array}
     *
     * @param context
     * @return
     */
    public static boolean needLog(final BuilderContext context) {
//        Field field = context.field;
//        return !field.isAnnotationPresent(F_bean.class) && !field.isAnnotationPresent(F_bean_list.class);
        return true;
    }

    public static boolean needBitBuf(List<Field> fieldList) {
        return fieldList.stream().anyMatch(e -> e.isAnnotationPresent(F_bit_num.class) || e.isAnnotationPresent(F_bit_num_array.class));
    }

    public static void newBitBuf_parse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final String bitBuf_reader_className = Parser.logCollector_parse == null ? BitBuf_reader.class.getName() : BitBuf_reader_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_reader" : "getBitBuf_reader_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameProcessContext, funcName);
    }

    public static void newBitBuf_deParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final String bitBuf_writer_className = Parser.logCollector_parse == null ? BitBuf_writer.class.getName() : BitBuf_writer_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_writer" : "getBitBuf_writer_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameProcessContext, funcName);
    }

    public static void appendBitLogCode_parse(final BuilderContext context) {
        final String varNameBitBuf = context.getBitBuf_parse();
        if (varNameBitBuf != null) {
            final Class<?> clazz = context.clazz;
            final String fieldName = context.field.getName();
            append(context.method_body, "{}.logCollector_parse.collect_field({}.class,\"{}\",2,new Object[]{{}.takeLogs(),{}});\n",
                    Parser.class.getName()
                    , clazz.getName()
                    , fieldName
                    , varNameBitBuf
                    , boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()));
        }
    }

    public static void appendBitLogCode_deParse(final BuilderContext context) {
        final String varNameBitBuf = context.getBitBuf_deParse();
        if (varNameBitBuf != null) {
            final Class<?> clazz = context.clazz;
            final String fieldName = context.field.getName();
            append(context.method_body, "{}.logCollector_deParse.collect_field({}.class,\"{}\",2,new Object[]{{},{}.takeLogs()});\n",
                    Parser.class.getName()
                    , clazz.getName()
                    , fieldName
                    , boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType())
                    , varNameBitBuf);
        }
    }

    public static void prependLogCode_parse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String varName = getFieldByteBufReaderIndexVarName(context);
        append(context.method_body, "final int {}={}.readerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_parse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String fieldByteBufReaderIndexVarName = getFieldByteBufReaderIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.method_body, "final byte[] {}=new byte[{}.readerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName);
        append(context.method_body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName, fieldLogBytesVarName);
        append(context.method_body, "{}.logCollector_parse.collect_field({}.class,\"{}\",0,new Object[]{{},{}});\n",
                Parser.class.getName(),
                context.clazz.getName(),
                context.field.getName(),
                fieldLogBytesVarName,
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()));
    }

    public static void prependLogCode_deParse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String varName = getFieldByteBufWriterIndexVarName(context);
        append(context.method_body, "final int {}={}.writerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_deParse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }

        final String fieldByteBufWriterIndexVarName = getFieldByteBufWriterIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.method_body, "final byte[] {}=new byte[{}.writerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName);
        append(context.method_body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName, fieldLogBytesVarName);
        append(context.method_body, "{}.logCollector_deParse.collect_field({}.class,\"{}\",0,new Object[]{{},{}});\n",
                Parser.class.getName(),
                context.clazz.getName(),
                context.field.getName(),
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()),
                fieldLogBytesVarName);
    }

    public static String getFieldVarName(final BuilderContext context) {
        return context.field.getName();
    }

    public static String replaceValExprToCode(final String expr, final String valExpr) {
        if (expr.isEmpty()) {
            return valExpr;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c == ' ') {
                continue;
            }
            if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && c != '.' && !Character.isDigit(c)) {
                sb.append(valExpr);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String replaceValExprToCode_round(final String expr, final String valExpr) {
        return format("{}.round(", ParseUtil.class.getName()) + replaceValExprToCode(expr, valExpr) + ")";
    }

    public static String unBoxing(final String num, final Class<?> clazz) {
        if (clazz == byte.class) {
            return num + ".byteValue()";
        } else if (clazz == short.class) {
            return num + ".shortValue()";
        } else if (clazz == int.class) {
            return num + ".intValue()";
        } else if (clazz == long.class) {
            return num + ".longValue()";
        } else if (clazz == float.class) {
            return num + ".floatValue()";
        } else if (clazz == double.class) {
            return num + ".doubleValue()";
        } else {
            return num;
        }
    }

    public static String boxing(final String num, final Class<?> clazz) {
        if (clazz == byte.class) {
            return "Byte.valueOf(" + num + ")";
        } else if (clazz == short.class) {
            return "Short.valueOf(" + num + ")";
        } else if (clazz == int.class) {
            return "Integer.valueOf(" + num + ")";
        } else if (clazz == long.class) {
            return "Long.valueOf(" + num + ")";
        } else if (clazz == float.class) {
            return "Float.valueOf(" + num + ")";
        } else if (clazz == double.class) {
            return "Double.valueOf(" + num + ")";
        } else {
            return num;
        }
    }

    public static String replaceExprToCode(final String lenExpr, final BuilderContext context) {
        final Map<Character, String> map = context.method_varToFieldName;
        final Field field = context.field;
        final StringBuilder sb = new StringBuilder();
        final char[] chars = lenExpr.toCharArray();
        for (char c : chars) {
            if (c == ' ') {
                continue;
            }
            if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && !Character.isDigit(c)) {
                if (Character.isUpperCase(c)) {
                    String globalVarName = context.getGlobalVarName(c);
                    sb.append(globalVarName);
                } else {
                    final String s = map.get(c);
                    if (s == null) {
                        throw ParseException.get("class[{}] field[{}] expr[{}] can't find char[{}] value", field.getDeclaringClass().getName(), field.getName(), lenExpr, c);
                    }
                    //所有的len字段必须转化为int运算
                    sb.append("(int)(").append(s).append(")");
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String replaceExprToCode_class(final String lenExpr, BuilderContext context) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = lenExpr.toCharArray();
        for (char c : chars) {
            if (c == ' ') {
                continue;
            }
            if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && !Character.isDigit(c)) {
                if (Character.isUpperCase(c)) {
                    String globalVarName = context.getGlobalVarName(c);
                    sb.append(globalVarName);
                } else {
                    final String s = context.method_varToFieldName.get(c);
                    if (s == null) {
                        throw ParseException.get("class[{}] c_skip lenExpr[{}] can't find char[{}] value", context.clazz.getName(), lenExpr, c);
                    }
                    //所有的len字段必须转化为int运算
                    sb.append("(int)(").append(s).append(")");
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(final String message, final Object... params) {
        return MessageFormatter.arrayFormat(message, params, null).getMessage();
    }

    public static void append(final StringBuilder sb, final String message, Object... params) {
        sb.append(format(message, params));
    }

    public static long round(double d) {
        if (d > 0d) {
            return Math.round(d);
        } else if (d == 0d) {
            return 0;
        } else {
            return -Math.round(-d);
        }
    }

    public static int round(float f) {
        if (f > 0d) {
            return Math.round(f);
        } else if (f == 0d) {
            return 0;
        } else {
            return -Math.round(-f);
        }
    }

    public static double round(double d, int i) {
        if (d > 0) {
            if (i == 0) {
                return Math.round(d);
            } else {
                return Math.round(d * pows[i]) / pows[i];
            }

        } else if (d < 0) {
            if (i == 0) {
                return -Math.round(-d);
            } else {
                return -Math.round(-d * pows[i]) / pows[i];
            }
        } else {
            return 0;
        }
    }

    public static boolean needParse(Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (Parser.anno_fieldBuilder.containsKey(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static List<Field> getParseFields(Class<?> clazz) {
        return ClassUtil.getAllFields(clazz).stream().filter(ParseUtil::needParse).collect(Collectors.toList());
    }

    private static String getProcessorSuffix(ByteOrder byteOrder, BitOrder bitOrder, NumValGetter numValGetter) {
        return "_" + (byteOrder == ByteOrder.smallEndian ? 0 : 1)
                + "_" + (bitOrder == BitOrder.smallEndian ? 0 : 1)
                + "_" + (Parser.logCollector_parse == null ? 0 : 1)
                + "_" + (Parser.logCollector_deParse == null ? 0 : 1)
                + "_" + (numValGetter.index)
                ;
    }

    public static String getProcessorKey(Class<?> clazz, ByteOrder byteOrder, BitOrder bitOrder, NumValGetter numValGetter) {
        return clazz.getName() + getProcessorSuffix(byteOrder, bitOrder, numValGetter);
    }


    /**
     * 生成类的序号
     */
    private static int processorIndex = 0;

    public static String getProcessorClassName(Class<?> clazz, ByteOrder byteOrder, BitOrder bitOrder, NumValGetter numValGetter) {
        String clazzName = Processor.class.getName();
        return clazzName.substring(0, clazzName.lastIndexOf("."))
                + ".P_"
                + (processorIndex++) + "_"
                + clazz.getSimpleName()
                + getProcessorSuffix(byteOrder, bitOrder, numValGetter);
    }

    public static Map<Class<? extends Annotation>, FieldBuilder> getAllFieldBuild() {
        String parserClassName = Parser.class.getName();
        String pkg = parserClassName.substring(0, parserClassName.lastIndexOf("."));
        Map<Class<? extends Annotation>, FieldBuilder> map = new HashMap<>();
        try {
            List<Class<?>> classes = ClassUtil.getClasses(pkg + ".builder");
            for (Class<?> clazz : classes) {
                if (clazz != FieldBuilder.class && FieldBuilder.class.isAssignableFrom(clazz)) {
                    FieldBuilder instance = (FieldBuilder) clazz.getConstructor().newInstance();
                    String clazzSimpleName = clazz.getSimpleName();
                    String annoSimpleClassName = clazzSimpleName.substring(clazzSimpleName.indexOf("__") + 2);
                    String annoClassName = pkg + ".anno." + annoSimpleClassName;
                    map.put((Class<? extends Annotation>) Class.forName(annoClassName), instance);
                }
            }
        } catch (Exception e) {
            throw ParseException.get(e);
        }

        StringJoiner sj = new StringJoiner("\n");
        for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : map.entrySet()) {
            sj.add(format("Anno[{}] FieldBuilder[{}]", entry.getKey().getName(), entry.getValue().getClass().getName()));
        }
        logger.info("scan pkg[{}] list[{}]:\n{}", pkg, map.size(), sj);
        return map;
    }

    public static int getClassByteLenIfPossible(Class<?> clazz) {

        int all = 0;
        List<Field> parseFields = getParseFields(clazz);
        int bit = 0;
        int maxBitEndEasy = 0;
        for (int i = 0; i < parseFields.size(); i++) {
            Field parseField = parseFields.get(i);
            F_skip f_skip = parseField.getAnnotation(F_skip.class);
            if (f_skip != null) {
                if (f_skip.lenExprBefore().isEmpty() && f_skip.lenExprAfter().isEmpty()) {
                    all += (f_skip.lenBefore() + f_skip.lenAfter());
                } else {
                    return -1;
                }
            }

            F_num f_num = parseField.getAnnotation(F_num.class);
            if (f_num != null) {
                switch (f_num.type()) {
                    case uint8, int8 -> all += 1;
                    case uint16, int16 -> all += 2;
                    case uint24, int24 -> all += 3;
                    case uint32, int32, float32 -> all += 4;
                    case uint40, int40 -> all += 5;
                    case uint48, int48 -> all += 6;
                    case uint56, int56 -> all += 7;
                    case uint64, int64, float64 -> all += 8;
                    default -> {
                        return -1;
                    }
                }
                continue;
            }

            F_num_array f_num_array = parseField.getAnnotation(F_num_array.class);
            if (f_num_array != null) {
                int len = f_num_array.len();
                int singleSkip = f_num_array.singleSkip();
                if (len > 0) {
                    switch (f_num_array.singleType()) {
                        case uint8, int8 -> all += (1 + singleSkip) * len;
                        case uint16, int16 -> all += (2 + singleSkip) * len;
                        case uint24, int24 -> all += (3 + singleSkip) * len;
                        case uint32, int32, float32 -> all += (4 + singleSkip) * len;
                        case uint40, int40 -> all += (5 + singleSkip) * len;
                        case uint48, int48 -> all += (6 + singleSkip) * len;
                        case uint56, int56 -> all += (7 + singleSkip) * len;
                        case uint64, int64, float64 -> all += (8 + singleSkip) * len;
                        default -> {
                            return -1;
                        }
                    }
                } else {
                    return -1;
                }
                continue;
            }


            F_bit_num f_bit_num = parseField.getAnnotation(F_bit_num.class);
            if (f_bit_num != null) {
                bit += (f_bit_num.skipBefore() + f_bit_num.len() + f_bit_num.skipAfter());
                boolean ignore;
                switch (f_bit_num.bitRemainingMode()) {
                    case ignore -> ignore = true;
                    case not_ignore -> ignore = false;
                    default -> {
                        if (i == parseFields.size() - 1) {
                            ignore = true;
                        } else {
                            Field nextField = parseFields.get(i + 1);
                            ignore = !nextField.isAnnotationPresent(F_bit_num.class)
                                    && !nextField.isAnnotationPresent(F_bit_num_array.class);
                        }
                    }
                }
                if (ignore) {
                    all += (bit / 8) + (bit % 8 == 0 ? 0 : 1);
                    bit = 0;
                }
                continue;
            }

            F_bit_num_array f_bit_num_array = parseField.getAnnotation(F_bit_num_array.class);
            if (f_bit_num_array != null) {
                int len = f_bit_num_array.len();
                if (len > 0) {
                    bit += (f_bit_num_array.skipBefore() + f_bit_num_array.skipAfter());
                    bit += len * (f_bit_num_array.singleLen() + f_bit_num_array.singleSkip());
                    boolean ignore;
                    switch (f_bit_num_array.bitRemainingMode()) {
                        case ignore -> ignore = true;
                        case not_ignore -> ignore = false;
                        default -> {
                            if (i == parseFields.size() - 1) {
                                ignore = true;
                            } else {
                                Field nextField = parseFields.get(i + 1);
                                ignore = !nextField.isAnnotationPresent(F_bit_num.class)
                                        && !nextField.isAnnotationPresent(F_bit_num_array.class);
                            }
                        }
                    }
                    if (ignore) {
                        all += (bit / 8) + (bit % 8 == 0 ? 0 : 1);
                        bit = 0;
                    }
                } else {
                    return -1;
                }
                continue;
            }

            F_bit_num_easy f_bit_num_easy = parseField.getAnnotation(F_bit_num_easy.class);
            if (f_bit_num_easy != null) {
                maxBitEndEasy = Math.max(maxBitEndEasy, f_bit_num_easy.bitEnd());
                boolean end;
                if (f_bit_num_easy.end()) {
                    end = true;
                } else {
                    if (i == parseFields.size() - 1) {
                        end = true;
                    } else {
                        Field nextField = parseFields.get(i + 1);
                        end = !nextField.isAnnotationPresent(F_bit_num_easy.class);
                    }
                }
                if (end) {
                    all += (maxBitEndEasy / 8) + (maxBitEndEasy % 8 == 0 ? 0 : 1);
                    maxBitEndEasy = 0;
                }
            }


            F_string f_string = parseField.getAnnotation(F_string.class);
            if (f_string != null) {
                int len = f_string.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_string_bcd f_string_bcd = parseField.getAnnotation(F_string_bcd.class);
            if (f_string_bcd != null) {
                int len = f_string_bcd.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_date_bcd f_date_bcd = parseField.getAnnotation(F_date_bcd.class);
            if (f_date_bcd != null) {
                int len = f_date_bcd.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_date_bytes_6 f_date_bytes_6 = parseField.getAnnotation(F_date_bytes_6.class);
            if (f_date_bytes_6 != null) {
                all += 6;
                continue;
            }
            F_date_bytes_7 f_date_bytes_7 = parseField.getAnnotation(F_date_bytes_7.class);
            if (f_date_bytes_7 != null) {
                all += 7;
                continue;
            }
            F_date_ts f_date_ts = parseField.getAnnotation(F_date_ts.class);
            if (f_date_ts != null) {
                switch (f_date_ts.mode()) {
                    case uint64_ms, uint64_s, float64_ms, float64_s -> all += 8;
                    case uint32_s -> all += 4;
                    default -> {
                        return -1;
                    }
                }
                continue;
            }

            F_bean f_bean = parseField.getAnnotation(F_bean.class);
            if (f_bean != null) {
                Class<?> parseFieldType = parseField.getType();
                C_skip c_skip = parseFieldType.getAnnotation(C_skip.class);
                if (c_skip == null) {
                    int beanLen = getClassByteLenIfPossible(parseFieldType);
                    if (beanLen == -1) {
                        return -1;
                    } else {
                        all += beanLen;
                    }
                } else {
                    if (c_skip.lenExpr().isEmpty()) {
                        all += c_skip.len();
                    } else {
                        return -1;
                    }
                }
            }

            F_bean_list f_bean_list = parseField.getAnnotation(F_bean_list.class);
            if (f_bean_list != null) {
                int listLen = f_bean_list.listLen();
                if (listLen > 0) {
                    final Class<?> fieldType = parseField.getType();
                    final Class<?> fieldBeanType;
                    if (fieldType.isArray()) {
                        fieldBeanType = fieldType.getComponentType();
                    } else if (List.class.isAssignableFrom(fieldType)) {
                        fieldBeanType = (Class<?>) ((ParameterizedType) parseField.getGenericType()).getActualTypeArguments()[0];
                    } else {
                        return -1;
                    }
                    final int beanLen;
                    C_skip c_skip = fieldBeanType.getAnnotation(C_skip.class);
                    if (c_skip == null) {
                        beanLen = getClassByteLenIfPossible(fieldBeanType);
                    } else {
                        if (c_skip.lenExpr().isEmpty()) {
                            beanLen = c_skip.len();
                        } else {
                            return -1;
                        }
                    }
                    if (beanLen == -1) {
                        return -1;
                    } else {
                        all += beanLen * listLen;
                    }
                } else {
                    return -1;
                }
            }

            F_customize f_customize = parseField.getAnnotation(F_customize.class);
            if (f_customize != null) {
                return -1;
            }

        }
        return all;
    }

    public static void appendSkip_parse(int len, String lenExpr, BuilderContext context) {
        String lenValCode;
        if (len == 0) {
            lenValCode = ParseUtil.replaceExprToCode(lenExpr, context);
        } else {
            lenValCode = len + "";
        }
        final String fieldByteBufReaderIndexVarName = getFieldByteBufReaderIndexVarName(context) + "_skip_" + (context.method_varIndex++);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context) + "_skip_" + (context.method_varIndex++);
        if (Parser.logCollector_parse != null) {
            append(context.method_body, "final int {}={}.readerIndex();\n", fieldByteBufReaderIndexVarName, FieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(context.method_body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, lenValCode);
        if (Parser.logCollector_parse != null) {
            append(context.method_body, "final byte[] {}=new byte[{}.readerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName);
            append(context.method_body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName, fieldLogBytesVarName);
            append(context.method_body, "{}.logCollector_parse.collect_field({}.class,\"{}\",1,new Object[]{{}});\n",
                    Parser.class.getName(),
                    context.clazz.getName(),
                    context.field.getName(),
                    fieldLogBytesVarName);
        }
    }

    public static void appendSkip_deParse(int len, String lenExpr, BuilderContext context) {
        String lenValCode;
        if (len == 0) {
            lenValCode = ParseUtil.replaceExprToCode(lenExpr, context);
        } else {
            lenValCode = len + "";
        }
        final String fieldByteBufWriterIndexVarName = getFieldByteBufWriterIndexVarName(context) + "_skip_" + (context.method_varIndex++);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context) + "_skip_" + (context.method_varIndex++);
        if (Parser.logCollector_deParse != null) {
            append(context.method_body, "final int {}={}.writerIndex();\n", fieldByteBufWriterIndexVarName, FieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(context.method_body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, lenValCode);
        if (Parser.logCollector_deParse != null) {
            append(context.method_body, "final byte[] {}=new byte[{}.writerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName);
            append(context.method_body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName, fieldLogBytesVarName);
            append(context.method_body, "{}.logCollector_deParse.collect_field({}.class,\"{}\",1,new Object[]{{}});\n",
                    Parser.class.getName(),
                    context.clazz.getName(),
                    context.field.getName(),
                    fieldLogBytesVarName);
        }
    }

    public static void appendPutGlobalVar(BuilderContext context, char var, String val) {
        append(context.method_body, "{}.putGlobalVar({},(int)({}));\n", FieldBuilder.varNameProcessContext, getGlobalVarIndex(var), val);
    }

    public static void appendGetGlobalVar(BuilderContext context, char var) {
        String globalVarName = getGlobalVarName(var);
        append(context.method_body, "final int {} = {}.getGlobalVar({});\n", globalVarName, FieldBuilder.varNameProcessContext, getGlobalVarIndex(var));
    }

    private static int getGlobalVarIndex(char var) {
        return var - 'A';
    }

    public static String getGlobalVarName(char var) {
        return "globalVar_" + var;
    }

    public static void appendNumValGetter_parse(BuilderContext context, NumType numType, String varNameRawVal, String okValCode) {
        String varNameField = ParseUtil.getFieldVarName(context);
        String varNameNumValType = varNameField + "_numValType";
        String varNameNumValGetter = context.getNumValGetterVarName();
        append(context.method_body, "final int {}={}.getType({}.{},{});\n",
                varNameNumValType,
                varNameNumValGetter,
                NumType.class.getName(), numType.name(),
                varNameRawVal);
        Class<?> fieldType = context.field.getType();
        String fieldName = context.field.getName();
        String defaultValCode = getNumValDefaultValue(context);
        append(context.method_body, """
                        if({}==0){
                            {}.{}=new {}(0,({})({}));
                        }else{
                            {}.{}=new {}({},{});
                        }
                        """,
                varNameNumValType,
                FieldBuilder.varNameInstance, fieldName, fieldType.getName(), getNumFieldValType(context).getName(), okValCode,
                FieldBuilder.varNameInstance, fieldName, fieldType.getName(), varNameNumValType, defaultValCode);
    }

    public static String appendNumValGetter_deParse(BuilderContext context, NumType numType, String varNameVal, String okValCode) {
        String varNameField = ParseUtil.getFieldVarName(context);
        String varNameNumValGetter = context.getNumValGetterVarName();
        String varNameNumValType = varNameField + "_numValType";
        append(context.method_body, "final int {}={}.{}.type();\n",
                varNameNumValType,
                FieldBuilder.varNameInstance,
                varNameField);
        String funcSuffix = switch (numType) {
            case uint8, int8, uint16, int16, uint24, int24, uint32, int32 -> "int";
            case uint40, int40, uint48, int48, uint56, int56, uint64, int64 -> "long";
            default -> null;
        };
        if (funcSuffix == null) {
            notSupport(context, "numType[{}] not support", numType.name());
        }

        Class<?> numValType = getNumFieldValType(context);
        append(context.method_body, "final {} {};\n", numValType.getName(), varNameVal);
        append(context.method_body, """
                        if({}==0){
                            {}={};
                        }else{
                            {}={}.getVal_{}({}.{},{});
                        }
                        """,
                varNameNumValType,
                varNameVal, okValCode,
                varNameVal, varNameNumValGetter, funcSuffix, NumType.class.getName(), numType.name(), varNameNumValType);
        return varNameVal;
    }

    public static String getNumValDefaultValue(BuilderContext context) {
        Class<?> fieldType = context.field.getType();
        String defaultValCode;
        if (fieldType == NumVal_byte.class) {
            defaultValCode = "(byte)0";
        } else if (fieldType == NumVal_short.class) {
            defaultValCode = "(short)0";
        } else if (fieldType == NumVal_int.class) {
            defaultValCode = "0";
        } else if (fieldType == NumVal_long.class) {
            defaultValCode = "0L";
        } else if (fieldType == NumVal_float.class) {
            defaultValCode = "0F";
        } else if (fieldType == NumVal_double.class) {
            defaultValCode = "0D";
        } else {
            notSupport(context, "fieldType[{}] not support", fieldType.getName());
            defaultValCode = null;
        }
        return defaultValCode;
    }

    public static Class<?> getNumFieldValType(BuilderContext context) {
        Class<?> fieldType;
        if (context.field.isAnnotationPresent(F_num.class)) {
            fieldType = context.field.getType();
        } else {
            fieldType = context.field.getType().getComponentType();
        }
        Class<?> valType;
        if (fieldType == byte.class || fieldType == short.class || fieldType == int.class
                || fieldType == long.class || fieldType == float.class || fieldType == double.class) {
            valType = fieldType;
        } else if (fieldType == NumVal_byte.class) {
            valType = byte.class;
        } else if (fieldType == NumVal_short.class) {
            valType = short.class;
        } else if (fieldType == NumVal_int.class) {
            valType = int.class;
        } else if (fieldType == NumVal_long.class) {
            valType = long.class;
        } else if (fieldType == NumVal_float.class) {
            valType = float.class;
        } else if (fieldType == NumVal_double.class) {
            valType = double.class;
        } else {
            if (fieldType.isEnum()) {
                valType = int.class;
            } else {
                notSupport(context, "fieldType[{}] not support", fieldType.getName());
                valType = null;
            }
        }
        return valType;
    }

    public static void main(String[] args) {
//        getAllFieldBuild();
//        System.out.println(getClassByteLenIfPossible(Evt_0001.class));
//        System.out.println(getClassByteLenIfPossible(Evt_0800.class));
//        System.out.println(getClassByteLenIfPossible(Evt_0006.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D006.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D010.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D011.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D012.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D013.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D014.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D015.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D016.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D017.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D008.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D009.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D00A.class));
    }
}
