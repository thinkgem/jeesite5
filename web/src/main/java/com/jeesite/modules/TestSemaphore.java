package com.jeesite.modules;

import java.util.concurrent.*;

/**
 * @auther: 高希阳
 * @Date: 2018/11/8 15:38
 * @Description:参考链接
 * https://segmentfault.com/a/1190000011527245
 * https://blog.csdn.net/ntk1986/article/details/53231273
 */
public class TestSemaphore {

    public static void main(String[] args) {

        //设置核心池大小,当线程数 < corePoolSize ，会创建线程执行 runnable
        int corePoolSize = 10;

        //设置线程池最大能接受多少线程, 当线程数 >= corePoolSize的时候，会把 runnable 放入 workQueue中
        int maximumPoolSize = 12;

        //当前线程数大于corePoolSize、小于maximumPoolSize时，超出corePoolSize的线程数的生命周期
        long keepActiveTime = 200;

        //设置时间单位，秒
        TimeUnit timeUnit = TimeUnit.SECONDS;

        //设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为5,保存任务的阻塞队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);

        //创建ThreadPoolExecutor线程池对象，并初始化该对象的各种参数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit,workQueue);

        // 线程池
//        ExecutorService exec = Executors.newCachedThreadPool();
        // 只能3个线程同时访问
        final Semaphore semp = new Semaphore(3);
        // 模拟10个客户端访问
        for (int index = 0; index < 10; index++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 获取许可
                        semp.acquire();

                        System.out.println("线程"+Thread.currentThread().getName()+"进入,当前已有"+(3-semp.availablePermits())+"并发");
                        Thread.sleep((long) (Math.random() * 10000));
                        // 访问完后，释放

                        System.out.println("线程"+Thread.currentThread().getName()+"即将离开");
                        semp.release();

                        System.out.println("线程"+Thread.currentThread().getName()+"已离开,当前已有"+(3-semp.availablePermits())+"并发");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(run);
        }
        // 退出线程池
        executor.shutdown();
    }
}
