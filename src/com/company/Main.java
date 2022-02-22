package com.company;

import javax.sql.rowset.CachedRowSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Default thread Implementation
        defaultThread();
        int tasks = 10_000;




        // Core Thread Pool
        // get count of available cores
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Task());
        }
        System.out.println("Thread Name: " + Thread.currentThread().getName());

        // IO task Thread Pool
        // 1.  FixedThreadPool
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            service.execute(new IOTask());
        }
        System.out.println("Thread Name: " + Thread.currentThread().getName());


        // 2.  CachedThreadPool
        // for a lot of short-lived tasks
        ExecutorService cachedThreadPoolService = Executors.newCachedThreadPool();
        // submit the tasks for execution
        for (int i = 0; i < 100; i++) {
            cachedThreadPoolService.execute(new IOTask());
        }
        System.out.println("Thread Name: " + Thread.currentThread().getName());

        // for scheduling of tasks
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        //tasks to run after 10 seconds delay
        scheduledExecutorService.schedule(new Task(), 10, TimeUnit.SECONDS);

        //tasks to run after 10 seconds delay and repeat every 10 seconds
        scheduledExecutorService.scheduleAtFixedRate(new Task(), 15, 10, TimeUnit.SECONDS);

        //tasks to run repeatedly 15 seconds after previous task finishes
        scheduledExecutorService.scheduleWithFixedDelay(new Task(), 15, 10, TimeUnit.SECONDS);



        // Custom Thread Pool
        ExecutorService customService = new ThreadPoolExecutor(
                10,
                100,
                120, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(300));

        try {
            customService.execute(new Task());
        } catch (RejectedExecutionException e){
            System.err.println("Rejected" + e.getMessage());
        }


        // Custom Thread Pool with handler
        ExecutorService es = new ThreadPoolExecutor(
                10,
                100,
                120, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(300),
                new CustomRejectionHandler());

        try {
            es.execute(new Task());
        } catch (RejectedExecutionException e){
            System.err.println("Rejected" + e.getMessage());
        }


        // initiate shutdown. It won't accept any new task but will run whatever are pending.
        service.shutdown();

        // will throw RejectedExecutionException
        // service.execute(new Task());

        // will return true since shutdown has begun
        service.isShutdown();

        // will return true if all tasks are completed
        // including queued tasks
        service.isTerminated();

        // block until all tasks are completed or if timeout occurs
        service.awaitTermination(10, TimeUnit.SECONDS);

        // will initiate shutdown and return all queued tasks
        List<Runnable> runnables = service.shutdownNow();


    }

    // https://docs.oracle.com/javase/tutorial/essential/concurrency/threads.html
    public static void defaultThread() throws InterruptedException {
//        Thread thread = new Thread(() -> System.out.println("Ayush"));
        Thread thread = new Thread(new Task());
        System.out.println("Hello");
        thread.start();
        thread.join();
    }

    static class CustomRejectionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // logging or operations to perform on execution
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread Name: " + Thread.currentThread().getName());
        }
    }



    static class IOTask implements Runnable {
        @Override
        public void run() {
            // Some IO operations which will cause the thread to block/wait
        }
    }
}

