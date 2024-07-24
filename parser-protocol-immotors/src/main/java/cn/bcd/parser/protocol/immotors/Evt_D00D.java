package cn.bcd.parser.protocol.immotors;


import io.netty.buffer.ByteBuf;

public class Evt_D00D extends Evt_4_x {
    public short BMSBusbarTemSumNum;
    public short[] BMSBusbarTem;
    public byte[] BMSBusbarTemV;

    public static Evt_D00D read(ByteBuf data) {
        Evt_D00D evt = new Evt_D00D();
        evt.evtId=data.readUnsignedShort();
        evt.evtLen=data.readUnsignedShort();
        short num = data.readUnsignedByte();
        evt.BMSBusbarTemSumNum = num;
        short[] arr1 = new short[num];
        byte[] arr2 = new byte[num];
        evt.BMSBusbarTem = arr1;
        evt.BMSBusbarTemV = arr2;
        for (int i = 0; i < num; i++) {
            int temp = data.readUnsignedShort();
            arr1[i] = (short) ((temp >> 8) - 40);
            arr2[i] = (byte) ((temp >> 7) & 0x01);
        }
        return evt;
    }

    public void write(ByteBuf data) {
        data.writeShort(evtId);
        data.writeShort(evtLen);
        data.writeByte(BMSBusbarTemSumNum);
        for (int i = 0; i < BMSBusbarTemSumNum; i++) {
            int i1 = BMSBusbarTem[i] + 40;
            int i2 = BMSBusbarTemV[i];
            data.writeShort((i1 << 8) | (i2 << 7));
        }
    }
}
