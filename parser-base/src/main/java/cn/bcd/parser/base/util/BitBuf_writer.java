package cn.bcd.parser.base.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class BitBuf_writer {
    public final ByteBuf byteBuf;

    public byte b;

    //当前写入字节bit偏移量
    public int bitOffset = 0;

    public BitBuf_writer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public static void main(String[] args) {
        final long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
//            final ByteBuf bb = Unpooled.buffer();
//            final BitBuf_writer bitBufWriter = BitBuf_writer.newBitBuf(bb);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(28900L, 15);

//            System.out.println(ByteBufUtil.hexDump(bb));


            final ByteBuf bb = Unpooled.buffer();
            final BitBuf_writer bitBufWriter = new BitBuf_writer(bb);
            bitBufWriter.write(4, 3, true, true);
            bitBufWriter.write(0, 3, true, true);
            bitBufWriter.skip(3);
            bitBufWriter.write(-217, 9, false, false);
            bitBufWriter.finish();
            System.out.println(ByteBufUtil.hexDump(bb));
        }
        System.out.println(System.currentTimeMillis() - t1);
    }


    public void write(long l, int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        int bitOffset = this.bitOffset;
        byte b = this.b;
        if (!unsigned && l < 0) {
            l = l & ((0x01L << bit) - 1);
        }
        if (!bigEndian) {
            l = Long.reverse(l) >>> (64 - bit);
        }
        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final long newL;
        final int byteLen;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
            newL = l;
        } else {
            byteLen = (temp >> 3) + 1;
            newL = l << (8 - finalBitOffset);
        }
        if (bitOffset == 0) {
            b = (byte) (newL >> ((byteLen - 1) << 3));
        } else {
            b |= (byte) (newL >> ((byteLen - 1) << 3));
        }
        for (int i = 1; i < byteLen; i++) {
            byteBuf.writeByte(b);
            b = (byte) (newL >> ((byteLen - i - 1) << 3));
        }
        bitOffset = finalBitOffset;
        if (bitOffset == 0) {
            byteBuf.writeByte(b);
        }

        this.bitOffset = bitOffset;
        this.b = b;
    }

    public void skip(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        if (byteLen == 1) {
            if (newBitOffsetZero) {
                byteBuf.writeByte(b);
                b = 0;
            }
        } else {
            if (bitOffset == 0) {
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen);
                } else {
                    byteBuf.writeZero(byteLen - 1);
                }

            } else {
                byteBuf.writeByte(b);
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen - 1);
                } else {
                    byteBuf.writeZero(byteLen - 2);
                }
            }
            b = 0;
        }
        this.bitOffset = temp & 7;
        this.b = b;
    }

    public void finish() {
        if (bitOffset > 0) {
            byteBuf.writeByte(b);
        }
        b = 0;
        bitOffset = 0;
    }
}
