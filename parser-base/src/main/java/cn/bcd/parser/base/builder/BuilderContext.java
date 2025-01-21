package cn.bcd.parser.base.builder;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.anno.data.BitOrder;
import cn.bcd.parser.base.anno.data.ByteOrder;
import cn.bcd.parser.base.anno.data.NumValGetter;
import cn.bcd.parser.base.processor.ProcessContext;
import cn.bcd.parser.base.processor.Processor;
import cn.bcd.parser.base.util.*;
import io.netty.buffer.ByteBuf;
import javassist.CtClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderContext {
    /**
     * 类
     */
    public final Class<?> clazz;
    /**
     * 生产的{@link Processor}子类
     */
    public final CtClass implCc;

    /**
     * 当前字段
     */
    public Field field;

    /**
     * 当前字段所在所有字段中的索引
     */
    public int fieldIndex;

    /**
     * 传递进来的字节序
     */
    public final ByteOrder byteOrder;
    public final BitOrder bitOrder;

    /**
     * 类变量定义体
     */
    public final StringBuilder class_fieldDefineBody;
    /**
     * 构造方法体
     */
    public final StringBuilder class_constructBody;

    /**
     * 类全局变量定义内容对应变量名称
     * 避免重复定义类变量
     * 解析反解析共用
     */
    public final Map<String, String> class_varDefineToVarName;

    /**
     * 字段集合
     */
    public final List<Field> class_fieldList;

    /**
     * parse方法体
     */
    public final StringBuilder method_body;

    /**
     * 解析/反解析 方法中
     * 用于给
     * {@link Processor#process(ByteBuf, ProcessContext)}
     * {@link Processor#deProcess(ByteBuf, ProcessContext, Object)}
     * 的参数对象、对象复用、避免构造多个
     * 解析和反解析不共用
     */
    public String method_processContextVarName;

    /**
     * 解析/反解析 方法中使用的变量对应字段名
     * 解析和反解析不共用
     */
    public final Map<Character, String> method_varToFieldName = new HashMap<>();

    /**
     * 构造 解析/反解析 方法所使用的缓存
     * 解析和反解析不共用
     * 解析或反解析期间所有字段共享缓存
     */
    public final Map<String, Object> method_cache = new HashMap<>();

    /**
     * 变量序号
     * 为了保证在一个 解析/反解析 方法中、定义的临时变量不重复
     * 解析和反解析不共用
     */
    public int method_varIndex = 0;


    public final NumValGetter numValGetter;

    public BuilderContext(StringBuilder class_fieldDefineBody, StringBuilder class_constructBody, StringBuilder method_body, Class<?> clazz,
                          CtClass implCc, Map<String, String> class_varDefineToVarName, ByteOrder byteOrder, BitOrder bitOrder,
                          List<Field> class_fieldList, NumValGetter numValGetter) {
        this.class_fieldDefineBody = class_fieldDefineBody;
        this.class_constructBody = class_constructBody;
        this.method_body = method_body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.class_varDefineToVarName = class_varDefineToVarName;
        this.byteOrder = byteOrder;
        this.bitOrder = bitOrder;
        this.class_fieldList = class_fieldList;
        this.numValGetter = numValGetter;
    }

    public final String getProcessContextVarName() {
        if (method_processContextVarName == null) {
            method_processContextVarName = "processContext";
            final String processContextClassName = ProcessContext.class.getName();
            ParseUtil.append(method_body, "final {} {}=new {}({},{});\n",
                    processContextClassName,
                    method_processContextVarName,
                    processContextClassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameProcessContext
            );
        }
        return method_processContextVarName;
    }

    public final String getCustomizeProcessorVarName(Class<?> processorClass, String processorArgs) {
        final String processorClassName = processorClass.getName();
        return ParseUtil.defineClassVar(this, processorClass, "new {}({})", processorClassName, processorArgs);
    }

    public final String getNumValGetterVarName() {
        return ParseUtil.defineClassVar(this, null, NumValGetter.class,
                NumValGetter.class.getSimpleName(),
                "{}.get({})",
                NumValGetter.class.getName(),numValGetter.index);
    }

    public final String getProcessorVarName(Class<?> beanClazz) {
        return ParseUtil.defineClassVar(this, e -> {
                    Parser.getProcessor(beanClazz, byteOrder, bitOrder, numValGetter);
                }, Processor.class,
                Processor.class.getSimpleName() + "_" + beanClazz.getSimpleName(),
                "{}.beanProcessorKey_processor.get(\"{}\")",
                Parser.class.getName(), ParseUtil.getProcessorKey(beanClazz, byteOrder, bitOrder, numValGetter));
    }

    public final String getGlobalVarName(char c) {
        return (String) method_cache.computeIfAbsent(ParseUtil.getGlobalVarName(c), k -> {
            ParseUtil.appendGetGlobalVar(this, c);
            return k;
        });
    }

    public final String getBitBuf_parse() {
        if (!method_cache.containsKey("hasBitBuf")) {
            final String bitBuf_reader_className = Parser.logCollector_parse == null ? BitBuf_reader.class.getName() : BitBuf_reader_log.class.getName();
            final String funcName = Parser.logCollector_parse == null ? "getBitBuf_reader" : "getBitBuf_reader_log";
            ParseUtil.append(method_body, "final {} {}={}.{}();\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameProcessContext, funcName);
            method_cache.put("hasBitBuf", true);
        }
        return FieldBuilder.varNameBitBuf;
    }

    public final String getBitBuf_deParse() {
        if (!method_cache.containsKey("hasBitBuf")) {
            final String bitBuf_writer_className = Parser.logCollector_parse == null ? BitBuf_writer.class.getName() : BitBuf_writer_log.class.getName();
            final String funcName = Parser.logCollector_parse == null ? "getBitBuf_writer" : "getBitBuf_writer_log";
            ParseUtil.append(method_body, "final {} {}={}.{}();\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameProcessContext, funcName);
            method_cache.put("hasBitBuf", true);
        }
        return FieldBuilder.varNameBitBuf;
    }
}
