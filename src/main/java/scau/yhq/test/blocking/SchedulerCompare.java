package scau.yhq.test.blocking;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SchedulerCompare {

    public static final int MAX_SIZE = 2 << 25;

    public static final long INSERT_PER_TIME = 2 << 10;

    public static final int THREAD_COUNT = 16;


    private static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    private static LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(MAX_SIZE);

    private static Scheduler<Integer> scheduler = new Scheduler<>();

    private static SchedulerV2<Integer> schedulerv2 = new SchedulerV2<>();


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
    public void testSchedulerV2() {
//        for (int i = 0; i < INSERT_PER_TIME; i++) {
//            scheduler.addObject(1);
//        }

        schedulerv2.addObject(1);

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

    @Benchmark
    @Threads(THREAD_COUNT)
//    @Measurement()
    public void testLinkedBlockingQueue() {
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
        linkedBlockingQueue.add(1);
        if (linkedBlockingQueue.size() > (MAX_SIZE >> 1)) {
            synchronized (linkedBlockingQueue) {
                if (linkedBlockingQueue.size() > (MAX_SIZE >> 1)) {
                    linkedBlockingQueue.clear();
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
                .include(SchedulerCompare.class.getSimpleName())
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(5))
                .timeUnit(TimeUnit.SECONDS)
                .warmupIterations(3)
                .warmupTime(TimeValue.seconds(5))
                .build();
        new Runner(opt).run();
    }
}
