package cn.bcd.parser.base.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PerformanceUtil {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceUtil.class);

    /**
     * 解析性能测试
     *
     * @param bytes
     * @param threadNum
     * @param num
     * @param <T>
     */
    public static <T> void testPerformance(byte[] bytes, int threadNum, int num, Function<ByteBuf, T> parseFunc, BiConsumer<ByteBuf, T> deParseConsumer, boolean parse) {
        final LongAdder count = new LongAdder();
        final ExecutorService[] pools = new ExecutorService[threadNum];
        for (int i = 0; i < pools.length; i++) {
            pools[i] = Executors.newSingleThreadExecutor();
        }
        if (parse) {
            for (ExecutorService pool : pools) {
                pool.execute(() -> {
                    testParse(bytes, num, count, parseFunc);
                });
            }
        } else {
            final ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            final T t = parseFunc.apply(buf);
            for (ExecutorService pool : pools) {
                pool.execute(() -> {
                    testDeParse(t, num, count, deParseConsumer);
                });
            }
        }
        final ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            long sum = count.sumThenReset() / 3;
            logger.info("{} threadNum:{} num:{} totalSpeed/s:{} perThreadSpeed/s:{}", parse ? "parse" : "deParse", threadNum, num, sum, sum / threadNum);
        }, 3, 3, TimeUnit.SECONDS);

        try {
            for (ExecutorService pool : pools) {
                pool.shutdown();
            }
            for (ExecutorService pool : pools) {
                while (!pool.awaitTermination(1, TimeUnit.HOURS)) {

                }
            }
            monitor.shutdown();
            while (!monitor.awaitTermination(1, TimeUnit.HOURS)) {

            }
        } catch (InterruptedException e) {
            logger.error("interrupted", e);
        }
    }

    public static <T> void testParse(byte[] bytes, int num, LongAdder count, Function<ByteBuf, T> parseFunc) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            final T t = parseFunc.apply(byteBuf);
            count.increment();
        }
    }

    public static <T> void testDeParse(T obj, int num, LongAdder count, BiConsumer<ByteBuf, T> deParseConsumer) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            deParseConsumer.accept(byteBuf, obj);
            count.increment();
        }
    }

}
