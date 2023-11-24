package scau.yhq.test.blocking;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 审计日记调度器
 * <p>
 * 1.提供审计日志的周期性插入
 * 2.异步多线程将审计日志实体插入准备集合，单线程实现插入数据库逻辑
 * </p>
 */
//@Slf4j
public class Scheduler<T> {

    private static final String PREFIX_LOG = "审计日志调度器 - ";

    private static final int INITIAL_SIZE_OF_SET = CompareInsertToBlockingQueueWithMe.MAX_SIZE;

    private static final int CORE_SIZE_OF_POOL = Runtime.getRuntime().availableProcessors();

    private static final int MAX_SIZE_OF_POOL = CORE_SIZE_OF_POOL > 0 ? CORE_SIZE_OF_POOL * 2 : 32;

    private static final int BLOCKING_LIST_SIZE = 1024;

    private final ExecutorService pool = new ThreadPoolExecutor(CORE_SIZE_OF_POOL, MAX_SIZE_OF_POOL, 10L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(BLOCKING_LIST_SIZE), r -> {
        Thread t = new Thread(r);
        t.setName("调度器调度线程");
        return t;
    });

    /**
     * 准备的集合
     */
    private final List<T> baseList = new ArrayList<>(INITIAL_SIZE_OF_SET);

    /**
     * 待插入的集合，实现插入时，会将BASE_SET的数据转到这里来
     */
    private final List<T> insertList = new ArrayList<>(INITIAL_SIZE_OF_SET);

    /**
     * 审计日志插入到{@code BASE_SET}的锁， COW适用于读多写少的场景
     */
    private final Object addLock = new Object();

    /**
     * 是否正在调度
     */
    private volatile boolean scheduling = false;

    private final Object schedulingLock = new Object();

    /**
     * 添加审计日志
     *
     * @param object
     */
    public void addObject(T object) {
        if (object == null) {
            return;
        }

        // 插入调度集合
//        pool.execute(() -> {
//            synchronized (addLock) {
//                // 大费周章就只为插入到List，但如果不另外开一个线程池去搞，理论上来说，所有线程在此刻都被我搞成串行的了。
//                baseList.add(object);
//            }
//        });

        synchronized (addLock) {
            // 大费周章就只为插入到List，但如果不另外开一个线程池去搞，理论上来说，所有线程在此刻都被我搞成串行的了。
            baseList.add(object);
            if (baseList.size() > (INITIAL_SIZE_OF_SET >> 1)) {
                baseList.clear();
            }
        }

        // 如果有审计进来，就去开启一个线程去实现插入数据库逻辑（多次调用保证只会启动一个线程，保证幂等性）
//        schedule();
    }

    /**
     * 调度线程去插入
     * <p>
     * 1.双重校验锁保证只会有一个线程去插入数据库
     * </p>
     */
    private void schedule() {
        if (!scheduling) {
            synchronized (schedulingLock) {
                if (!scheduling) {
                    scheduling = true;
                    tryInsert();
                }
            }
        }
    }

    /**
     * 尝试插入数据 - 给数据库插入逻辑新增了一个周期性调度的框架
     */
    private void tryInsert() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    insert();
                } catch (Exception e) {
                }
            }
        });
        t.start();
    }

    /**
     * 插入数据库，
     * <p>
     * 1.注意，治理必须是单线程调度这个方法，如果是多线程调度，那么还必须再加一个锁
     * </p>
     */
    private void insert() {
        if (!baseList.isEmpty()) {
            // 再清理一下，预防不可知的操作
            insertList.clear();
            synchronized (addLock) {
                // 数据从准备队列迁移到插入队列
                insertList.addAll(baseList);
                baseList.clear();
            }
            // todo 插入数据库

            insertList.clear();
        }
    }


}
