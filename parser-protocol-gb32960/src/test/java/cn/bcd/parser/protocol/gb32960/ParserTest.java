package cn.bcd.parser.protocol.gb32960;

import cn.bcd.parser.base.Parser;
import cn.bcd.parser.base.anno.data.NumVal_float;
import cn.bcd.parser.base.anno.data.NumVal_short;
import cn.bcd.parser.base.util.PerformanceUtil;
import cn.bcd.parser.protocol.gb32960.data.Packet;
import cn.bcd.parser.protocol.gb32960.data.VehicleRunData;
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
        String data = "232302FE4C534A4533363039364D5331343034393501014D180918120025010103FF014A001726AEFFFF275154011F20000000020201044C4E204E1B431108271002044B4E204E204511082710050100000000000000000601200FC101020FB901013E01023C07000000000000000000080101FFFF2751006C00016C0FBF0FB90FBF0FBD0FBF0FBB0FBF0FBD0FC00FBC0FC00FBB0FBA0FBB0FBB0FBB0FBD0FB90FBB0FBC0FBB0FBD0FBC0FBE0FBA0FBF0FBE0FC00FBD0FBE0FBC0FC10FBE0FBD0FBB0FBF0FBE0FB90FBE0FBC0FBE0FBC0FB90FB90FBE0FBD0FBF0FBD0FBE0FBC0FC00FBC0FBF0FBD0FBD0FBF0FBC0FBE0FBE0FBE0FBD0FC00FBE0FBD0FBD0FBF0FBF0FC00FC00FC00FBF0FC10FBE0FBC0FBF0FBE0FBF0FBC0FBF0FBD0FC10FBE0FBE0FBB0FBF0FBE0FBF0FBD0FC10FBC0FBC0FBF0FBC0FC00FBF0FBE0FBD0FBE0FBF0FBF0FBD0FC10FBF0FBF0FBE0FC00FBC0FC1090101000C3E3C3C3C3E3C3C3C3D3C3C3C29";
        data = data.replaceAll(" ", "");
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        Packet packet = Packet.read(byteBuf);
//        ((VehicleRunData)packet.data).vehicleBaseData.vehicleSpeed=new NumVal_float(0,(short)33.3);
        ByteBuf dest = Unpooled.buffer();
        packet.write(dest);
        logger.info(data.toUpperCase());
        logger.info(ByteBufUtil.hexDump(dest).toUpperCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

    @Test
    public void test_performance() {
        Parser.disableByteBufCheck();
        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        String data = "232302FE4C534A4533363039364D5331343034393501014D180918120025010103FF014A001726AEFFFF275154011F20000000020201044C4E204E1B431108271002044B4E204E204511082710050100000000000000000601200FC101020FB901013E01023C07000000000000000000080101FFFF2751006C00016C0FBF0FB90FBF0FBD0FBF0FBB0FBF0FBD0FC00FBC0FC00FBB0FBA0FBB0FBB0FBB0FBD0FB90FBB0FBC0FBB0FBD0FBC0FBE0FBA0FBF0FBE0FC00FBD0FBE0FBC0FC10FBE0FBD0FBB0FBF0FBE0FB90FBE0FBC0FBE0FBC0FB90FB90FBE0FBD0FBF0FBD0FBE0FBC0FC00FBC0FBF0FBD0FBD0FBF0FBC0FBE0FBE0FBE0FBD0FC00FBE0FBD0FBD0FBF0FBF0FC00FC00FC00FBF0FC10FBE0FBC0FBF0FBE0FBF0FBC0FBF0FBD0FC10FBE0FBE0FBB0FBF0FBE0FBF0FBD0FC10FBC0FBC0FBF0FBC0FC00FBF0FBE0FBD0FBE0FBF0FBF0FBD0FC10FBF0FBF0FBE0FC00FBC0FC1090101000C3E3C3C3C3E3C3C3C3D3C3C3C29";
        int threadNum = 1;
        logger.info("param threadNum[{}]", threadNum);
        int num = 2000000000;
        PerformanceUtil.testPerformance(ByteBufUtil.decodeHexDump(data), threadNum, num, Packet::read, (buf, instance) -> instance.write(buf), true);
    }
}
