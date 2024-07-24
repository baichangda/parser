package cn.bcd.parser.impl.gb32960.data;

import cn.bcd.parser.Parser;
import cn.bcd.parser.anno.C_impl;
import cn.bcd.parser.anno.F_date_bytes_6;
import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.NumType;
import cn.bcd.parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Date;
@C_impl(value = 0x04)
public class VehicleLogoutData implements PacketData {
    //登出时间
    @F_date_bytes_6
    public Date collectTime;

    //登出流水号
    @F_num(type = NumType.uint16)
    public int sn;

    public static void main(String[] args) {
        VehicleLogoutData vehicleLogoutData = new VehicleLogoutData();
        vehicleLogoutData.collectTime = new Date();
        vehicleLogoutData.sn = 1;
        Packet packet = new Packet();
        packet.header = new byte[]{0x23, 0x23};
        packet.flag = PacketFlag.vehicle_logout_data;
        packet.replyFlag = 0xfe;
        packet.vin = "LSJE36096MS140495";
        packet.encodeWay = 1;
        packet.contentLength = 8;
        packet.code = 0;
        packet.data = vehicleLogoutData;
        Processor<Packet> processor= Parser.getProcessor(Packet.class);
        ByteBuf buffer = Unpooled.buffer();
        processor.deProcess(buffer,packet);
        System.out.println(ByteBufUtil.hexDump(buffer));
    }
}
