package cn.bcd.parser.protocol.jtt808;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.processor.Processor;
import cn.bcd.parser.base.util.PerformanceUtil;
import cn.bcd.parser.protocol.jtt808.data.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserTest {
    static Logger logger = LoggerFactory.getLogger(ParserTest.class);


    @Test
    public void test() {
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
//        String data = "7E0200407C0100000000017299841738FFFF000004000000080006EEB6AD02633DF701380003006320070719235901040000000B02020016030200210402002C051E3737370000000000000000000000000000000000000000000000000000001105420000004212064D0000004D4D1307000000580058582504000000632A02000A2B040000001430011E310128637E";
        String data = "7e0200001c018986000000000001004705b000080000000000010000000000000000000000000000240407133527cc7e";
        data = data.replaceAll(" ", "");
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf = Packet.unEscapeAndXor(byteBuf);
        final Processor<Packet> processor = Parser.getProcessor(Packet.class);
        Packet packet = processor.process(byteBuf);
        ByteBuf dest = Unpooled.buffer();
        processor.deProcess(dest, packet);
        logger.info(data.toUpperCase());
        logger.info(ByteBufUtil.hexDump(dest).toUpperCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

    @Test
    public void test_performance() {
        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        String data = "7e0200407c0100000000017299841738ffff000004000000080006eeb6ad02633df701380003006320070719235901040000000b02020016030200210402002c051e3737370000000000000000000000000000000000000000000000000000001105420000004212064d0000004d4d1307000000580058582504000000632a02000a2b040000001430011e310128637e";
        int threadNum = 1;
        logger.info("param threadNum[{}]", threadNum);
        int num = 1000000000;
        Processor<Packet> processor = Parser.getProcessor(Packet.class);
        PerformanceUtil.testPerformance(ByteBufUtil.decodeHexDump(data), threadNum, num, processor::process, processor::deProcess, true);
    }
}
