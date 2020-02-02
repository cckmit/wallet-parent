package org.wallet.dap.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 * @author zengfucheng
 */
@Slf4j
public class ThreadPool {
    private final static ThreadPool SELF = new ThreadPool();
    private ExecutorService threadPool;
    private ThreadPool(){
        threadPool = newRetractedThreadPool();
    }
    public static ThreadPool getInstance(){
        return SELF;
    }

    /**
     * 线程池维护线程的最少数量
     */
	private int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 线程池维护线程的最大数量
     */
	private int maximumPoolSize = 500;

    /**
     * 线程池维护线程所允许的空闲时间，单位MS，超时将会终止该线程
     */
	private int keepAliveTime = 1000;

    /**
     * 线程池队列大小
     */
	private int queueSize = 10000;

	private ThreadPoolExecutor newRetractedThreadPool() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new DefaultThreadFactory("thread"),
                (r, executor) -> log.info("线程池状态已满,"
                        +  "线程池中计划被执行的任务总数：" + executor.getTaskCount() + ","
                        +  "执行完毕的任务数：" + executor.getCompletedTaskCount() + ","
                        +  "线程池中同时存在最大线程数：" + executor.getLargestPoolSize() + ","
                        +  "当前正在执行的任务数：" + executor.getActiveCount() + ","
                        +  "线程池中当前线程数：" + executor.getPoolSize()
                )
        );
	}


    /**
     * 执行单个任务
     * @param command
     */
    public void exe(Runnable command)
    {
        threadPool.execute(command);
    }

    /**
     * 执行单个任务,有返回结果的
     * @param command
     */
    public Future<?> submit(Runnable command)
    {
        return threadPool.submit(command);
    }

    /**
     * 执行多个任务
     * @param commands
     */
    public void exe(List<Runnable> commands) {
        for(int i = 0; i < commands.size(); i ++){
            threadPool.execute(commands.get(i));
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        DefaultThreadFactory(String threadName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + threadName + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(group, r, namePrefix + threadNumber.getAndIncrement(),0);
        }
    }
}
