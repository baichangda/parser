package cn.bcd.parser.base.util;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class BitBuf_writer_log extends BitBuf_writer {

    static Logger logger = LoggerFactory.getLogger(BitBuf_reader_log.class);
    public List<Log> logs = new ArrayList<>();

    public BitBuf_writer_log(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public static void main(String[] args) {
        final long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {

            final ByteBuf bb = Unpooled.buffer();
            final BitBuf_writer_log bitBufWriter = new BitBuf_writer_log(bb);
            bitBufWriter.write(4, 3, true);
            bitBufWriter.write(0, 3, true);
            bitBufWriter.skip(3);
            bitBufWriter.write(-217, 9, false);
            bitBufWriter.finish();
//            System.out.println(ByteBufUtil.hexDump(bb));
        }
        System.out.println(System.currentTimeMillis() - t1);
    }

    public void write(long l, int bit, boolean bigEndian) {
        l = l & ((0x01L << bit) - 1);
        final ByteBuf byteBuf = this.byteBuf;
        int bitOffset = this.bitOffset;
        byte b = this.b;

        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final int byteLen;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
        } else {
            byteLen = (temp >> 3) + 1;
        }

        final WriteLog log = new WriteLog(byteLen, bitOffset, bit, bigEndian);

        log.val1 = l;

        if (!bigEndian) {
            l = Long.reverse(l) >>> (64 - bit);
        }

        log.val2 = l;

        final long newL;
        if (finalBitOffset == 0) {
            newL = l;
        } else {
            newL = l << (8 - finalBitOffset);
        }
        b |= (byte) (newL >> ((byteLen - 1) << 3));
        log.bytes[0] = b;
        for (int i = 1; i < byteLen; i++) {
            byteBuf.writeByte(b);
            b = (byte) (newL >> ((byteLen - i - 1) << 3));
            log.bytes[i] = b;
        }
        if (finalBitOffset == 0) {
            byteBuf.writeByte(b);
            this.bitOffset = 0;
            this.b = 0;
        } else {
            this.bitOffset = finalBitOffset;
            this.b = b;
        }
        logs.add(log);
    }

    public void skip(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        final SkipLog log = new SkipLog(byteLen, bitOffset, bit);

        log.bytes[0] = b;
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
        logs.add(log);
    }

    public void finish() {
        final FinishLog log;
        if (bitOffset == 0) {
            log = null;
        } else {
            byteBuf.writeByte(b);
            log = new FinishLog(1, bitOffset, 8 - bitOffset);
            log.bytes[0] = b;
        }
        b = 0;
        bitOffset = 0;
        if (log != null) {
            logs.add(log);
        }
    }

    public Log[] takeLogs() {
        Log[] temp = logs.toArray(new Log[0]);
        logs.clear();
        return temp;
    }

    public static abstract class Log {
        public final byte[] bytes;

        public final int bitStart;
        public final int bit;

        public final int bitEnd;

        public Log(int byteLen, int bitStart, int bit) {
            this.bytes = new byte[byteLen];
            this.bitStart = bitStart;
            this.bit = bit;
            this.bitEnd = bitStart + bit - 1;
        }


        public final String getLogHex() {
            return ByteBufUtil.hexDump(bytes) + ((bitEnd & 7) == 7 ? "" : "?");
        }

        public void print() {
            logger.info(msg());
        }

        public abstract String msg();
    }

    public static class FinishLog extends Log {

        public boolean write;

        public FinishLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
            this.write = true;
        }

        public FinishLog() {
            super(0, 0, 0);
            this.write = false;
        }

        public final String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }

        @Override
        public String msg() {
            if (write) {
                return StringUtil.format("finish write bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
            } else {
                return StringUtil.format("finish no write");
            }
        }
    }

    public static class SkipLog extends Log {
        public SkipLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
        }

        @Override
        public String msg() {
            return StringUtil.format("skip bit_hex[{}] bit_pos[{}-{}]", getLogHex().toUpperCase(), bitStart, bitEnd);
        }
    }

    public static class WriteLog extends Log {
        public final boolean bigEndian;
        public long val1;
        public long val2;

        public WriteLog(int byteLen, int bitStart, int bit, boolean bigEndian) {
            super(byteLen, bitStart, bit);
            this.bigEndian = bigEndian;
        }

        public final String getLogBit(long l) {
            return Strings.padStart(Long.toBinaryString(l), bit, '0');
        }

        @Override
        public String msg() {
            return StringUtil.format("write bit_bigEndian[{}] bit_val[{}->{}] bit_binary[{}->{}] bit_hex[{}] bit_pos[{}-{}]",
                    bigEndian ? "yes" : "no",
                    val1, val2,
                    getLogBit(val1), getLogBit(val2),
                    getLogHex().toUpperCase(),
                    bitStart, bitEnd);
        }
    }
}
