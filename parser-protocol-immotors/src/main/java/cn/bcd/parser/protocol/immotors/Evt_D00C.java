package cn.bcd.parser.protocol.immotors;


import io.netty.buffer.ByteBuf;

public class Evt_D00C extends Evt_4_x {
    public short BMSCellTemSumNum;
    public short[] BMSCellTem;
    public byte[] BMSCellTemV;

    public static Evt_D00C read(ByteBuf data) {
        Evt_D00C evt = new Evt_D00C();
        evt.evtId=data.readUnsignedShort();
        evt.evtLen=data.readUnsignedShort();
        short num = data.readUnsignedByte();
        evt.BMSCellTemSumNum = num;
        short[] arr1 = new short[num];
        byte[] arr2 = new byte[num];
        evt.BMSCellTem = arr1;
        evt.BMSCellTemV = arr2;
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
        data.writeByte(BMSCellTemSumNum);
        for (int i = 0; i < BMSCellTemSumNum; i++) {
            int i1 = BMSCellTem[i] + 40;
            int i2 = BMSCellTemV[i];
            data.writeShort((i1 << 8) | (i2 << 7));
        }
    }
}
