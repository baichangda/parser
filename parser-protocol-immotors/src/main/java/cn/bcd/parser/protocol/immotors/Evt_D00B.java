package cn.bcd.parser.protocol.immotors;


import io.netty.buffer.ByteBuf;

public class Evt_D00B extends Evt_4_x {
    public short BMSCellVolSumNum;
    public float[] BMSCellVol;
    public byte[] BMSCellVolV;

    public static Evt_D00B read(ByteBuf data) {
        Evt_D00B evt = new Evt_D00B();
        evt.evtId=data.readUnsignedShort();
        evt.evtLen=data.readUnsignedShort();
        short num = data.readUnsignedByte();
        evt.BMSCellVolSumNum = num;
        float[] arr1 = new float[num];
        byte[] arr2 = new byte[num];
        evt.BMSCellVol = arr1;
        evt.BMSCellVolV = arr2;
        for (int i = 0; i < num; i++) {
            int temp = data.readUnsignedShort();
            arr1[i] = (temp >> 3) * 0.001f;
            arr2[i] = (byte) ((temp >> 2) & 0x01);
        }
        return evt;
    }

    public void write(ByteBuf data) {
        data.writeShort(evtId);
        data.writeShort(evtLen);
        data.writeByte(BMSCellVolSumNum);
        for (int i = 0; i < BMSCellVolSumNum; i++) {
            int i1 = (int) (BMSCellVol[i] * 1000);
            int i2 = BMSCellVolV[i];
            data.writeShort((i1 << 3) | (i2 << 2));
        }
    }

    public static void main(String[] args) {
        System.out.println(4.031 / 0.001);
    }
}


