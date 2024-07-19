package cn.bcd.parser.builder;


import io.netty.buffer.ByteBuf;

public abstract class FieldBuilder {
    public final static String varNameThis = "$0";
    public final static String varNameByteBuf = "$1";
    public final static String varNameProcessContext = "$2";

    public final static String varNameInstance = "_instance";
    /**
     * 如果bean存在注解{@link cn.bcd.parser.anno.C_skip}、会有如下操作
     * 解析bean开始时候、记录{@link ByteBuf#readerIndex()}
     * 反解析bean开始时候、记录{@link ByteBuf#writerIndex()}
     */
    public final static String varNameStartIndex = "_start_index";
    public final static String varNameShouldSkip= "_should_skip";


    public final static String varNameBitBuf = "_bitBuf";

    public abstract void buildParse(final BuilderContext context);


    public void buildDeParse(final BuilderContext context) {

    }
}
