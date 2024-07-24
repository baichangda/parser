package cn.bcd.parser.base.util;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class BitBuf_reader_log extends BitBuf_reader {

    static Logger logger = LoggerFactory.getLogger(BitBuf_reader_log.class);

    public List<Log> logs = new ArrayList<>();

    public BitBuf_reader_log(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public static void main(String[] args) {
        final long t1 = System.currentTimeMillis();
        final byte[] source = {
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4
        };
        for (int i = 0; i < 1; i++) {
            final byte[] source2 = {
                    (byte) 0x81, (byte) 0x72, (byte) 0x40,
            };
            ByteBuf bb2 = Unpooled.wrappedBuffer(source2);
            BitBuf_reader_log bitBuf2 = new BitBuf_reader_log(bb2);
            bitBuf2.read(3, true, true);
            bitBuf2.read(3, true, true);
            bitBuf2.skip(3);
            bitBuf2.read(9, false, false);
        }
        System.out.println(System.currentTimeMillis() - t1);

    }

    public long read(int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b;
        if (bitOffset == 0) {
            b = byteBuf.readByte();
        } else {
            b = this.b;
        }

        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final int byteLen;
        long l;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
            l = (b & 0xffL) << (temp - 8);
        } else {
            byteLen = (temp >> 3) + 1;
            l = (b & 0xffL) << (temp - finalBitOffset);
        }

        final ReadLog log = new ReadLog(byteLen, bitOffset, bit, bigEndian, unsigned);
        log.bytes[0] = b;

        for (int i = 1; i < byteLen; i++) {
            b = byteBuf.readByte();
            log.bytes[i] = b;
            l |= (b & 0xffL) << ((byteLen - 1 - i) << 3);
        }

        this.bitOffset = finalBitOffset;
        this.b = b;

        log.val1 = (l >>> (byteLen * 8 - bitOffset - bit)) & ((0x01L << bit) - 1);

        //如果是小端模式、则翻转bit
        final long cRight;
        if (bigEndian) {
            cRight = l >>> ((byteLen << 3) - bitOffset - bit);
        } else {
            cRight = Long.reverse(l) >>> (64 - (byteLen << 3) + bitOffset);
        }

        log.val2 = cRight & ((0x01L << bit) - 1);

        if (!unsigned && ((cRight >> (bit - 1)) & 0x01) == 1) {
            log.val3 = cRight | (-1L << bit);
            log.signed3 = true;
        } else {
            log.val3 = cRight & ((0x01L << bit) - 1);
        }
        logs.add(log);
        return log.val3;
    }

    public void skip(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;

        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);

        final SkipLog log = new SkipLog(byteLen, bitOffset, bit);

        if (byteLen == 1) {
            if (bitOffset == 0) {
                b = byteBuf.readByte();
            }
            log.bytes[0] = b;
        } else {
            if (bitOffset == 0) {
                byteBuf.getBytes(0, log.bytes);
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen);
                } else {
                    byteBuf.skipBytes(byteLen - 1);
                    b = byteBuf.readByte();
                }
            } else {
                log.bytes[0] = b;
                byteBuf.getBytes(byteBuf.readerIndex(), log.bytes, 1, byteLen - 1);
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen - 1);
                } else {
                    byteBuf.skipBytes(byteLen - 2);
                    b = byteBuf.readByte();
                }
            }
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

        public boolean skip;

        public FinishLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
            this.skip = true;
        }

        public FinishLog() {
            super(0, 0, 0);
            this.skip = false;
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
            if (skip) {
                return StringUtil.format("finish skip bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
            } else {
                return StringUtil.format("finish no skip");
            }
        }
    }

    public static class SkipLog extends Log {

        public SkipLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
        }

        public final String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }

        public final String msg() {
            return StringUtil.format("skip bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
        }
    }

    public static class ReadLog extends Log {

        public final boolean unsigned;

        public final boolean bigEndian;
        //原始值
        public long val1;
        //处理大小端之后的值
        public long val2;
        //是否有符号
        public boolean signed3;
        //最终解析值
        public long val3;

        public ReadLog(int byteLen, int bitStart, int bit, boolean bigEndian, boolean unsigned) {
            super(byteLen, bitStart, bit);
            this.bigEndian = bigEndian;
            this.unsigned = unsigned;
        }

        public final String getLogBit(long l, boolean signed) {
            if (signed) {
                return "-" + Strings.padStart(Long.toBinaryString(-l), bit, '0');
            } else {
                return Strings.padStart(Long.toBinaryString(l), bit, '0');
            }
        }

        @Override
        public String msg() {
            return StringUtil.format("read bit_hex[{}] bit_pos[{}-{}] bit_bigEndian[{}] bit_unsigned[{}] bit_binary[{}->{}->{}] bit_val[{}->{}->{}]",
                    getLogHex().toUpperCase(),
                    bitStart, bitEnd,
                    bigEndian ? "yes" : "no",
                    unsigned ? "yes" : "no",
                    getLogBit(val1, false), getLogBit(val2, false), getLogBit(val3, signed3),
                    val1, val2, val3);
        }
    }
}
