package com.lkc.tool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutor {
    private static MyThreadPoolExecutor myThreadPoolExecutor = new MyThreadPoolExecutor();
    private ThreadPoolExecutor pool = null;

    //构造函数
    private MyThreadPoolExecutor() {
        init();
    }

    //获取单例
    public static MyThreadPoolExecutor getInstance() {
        return myThreadPoolExecutor;
    }

    //获取线程池
    public ThreadPoolExecutor getMyThreadPoolExecutor() {
        return this.pool;
    }

    //初始化线程池  构造函数中调用
    private void init() {
        this.pool = new ThreadPoolExecutor(
                10, 20, 600, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(10),
                new CustomThreadFactory(),
                new CustomRejectedExecutionHandler()
        );
    }

    //自定义线程创建方式（自定义线程名）
    private class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            String threadName = "lkc-Thread-" + count.addAndGet(1);
            thread.setName(threadName);
            return thread;
        }
    }

    //自定义拒绝策略 （阻塞添加）
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
