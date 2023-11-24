package scau.yhq.test.blocking;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CompareInsertToBlockingQueueWithMe {

    public static final int MAX_SIZE = 2 << 25;

    public static final long INSERT_PER_TIME = 2 << 10;

    public static final int THREAD_COUNT = 16;

    //
    private static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    private static Scheduler<Integer> scheduler = new Scheduler<>();


    @Benchmark
    @Threads(THREAD_COUNT)
    public void testScheduler() {
//        for (int i = 0; i < INSERT_PER_TIME; i++) {
//            scheduler.addObject(1);
//        }

        scheduler.addObject(1);

    }

    @Benchmark
    @Threads(THREAD_COUNT)
    public void testBlockingQueue() {
//        for (int i = 0; i < INSERT_PER_TIME; i++) {
//            blockingQueue.add(1);
//            if (blockingQueue.size() > (MAX_SIZE >> 5)) {
//                synchronized (blockingQueue) {
//                    if (blockingQueue.size() > (MAX_SIZE >> 5)) {
//                        blockingQueue.clear();
//                    }
//                }
//            }
//        }
        blockingQueue.add(1);
        if (blockingQueue.size() > (MAX_SIZE >> 1)) {
            synchronized (blockingQueue) {
                if (blockingQueue.size() > (MAX_SIZE >> 1)) {
                    blockingQueue.clear();
                }
            }
        }
//        if (blockingQueue.size() > (MAX_SIZE >> 5)) {
//            synchronized (blockingQueue) {
//                if (blockingQueue.size() > (MAX_SIZE >> 5)) {
//                    blockingQueue.clear();
//                }
//            }
//        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CompareInsertToBlockingQueueWithMe.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
