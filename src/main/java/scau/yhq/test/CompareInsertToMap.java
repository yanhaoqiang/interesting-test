package scau.yhq.test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CompareInsertToMap {

    public static final int MAX_SIZE = 2 << 31;

    public static final long INSERT_PER_TIME = 2 << 10;

    public static final int THREAD_COUNT = 10;

    //
    private static Map<Integer, Integer> testMap = new HashMap<>();

    private static ConcurrentHashMap<Integer, Integer> cowMap = new ConcurrentHashMap<>();

    private static Cache<Integer, Integer> caffeine = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(MAX_SIZE)
            .build();

//    @Setup
//    public void init() {
//        testMap = new HashMap<Integer, Integer>();
//        cowMap = new ConcurrentHashMap<Integer, Integer>(MAX_SIZE);
//        caffeine = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(MAX_SIZE)
//                .build();
//    }

    @Benchmark
    @Threads(THREAD_COUNT)
    public void testHashMap() {
        for (int i = 0; i < INSERT_PER_TIME; i++) {
            testMap.put(i, i);
        }
    }

    @Benchmark
    @Threads(THREAD_COUNT)
    public void testCowMap() {
        for (int i = 0; i < INSERT_PER_TIME; i++) {
            cowMap.put(i, i);
        }
    }

    @Benchmark
    @Threads(THREAD_COUNT)
    public void testCaffeine() {
        for (int i = 0; i < INSERT_PER_TIME; i++) {
            caffeine.put(i, i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CompareInsertToMap.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
