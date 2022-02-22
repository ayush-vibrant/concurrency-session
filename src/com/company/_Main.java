package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class _Main {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        // submit the tasks for execution
        // placeholder for the value that will arrive in sometime in the future
        Future<Integer> future = service.submit(new CallableTask());

        // perform some unrelated operations -> 1 seconds

        // get the result
        try {
            Integer returnVal = future.get();
            System.out.println(returnVal);
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<Future> allFutures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Future<Integer> future1 = service.submit(new CallableTask());
            allFutures.add(future1);
        }

        // 100 futures, with 100 placeholders

        // perform some unrelated operations

        // let's say after 100 seconds
        for (int i = 0; i < 100; i++) {
            Future fut = allFutures.get(i);
            try {
                if (fut.isDone()) {
                    Integer result = (Integer) fut.get(); // blocking call
                    System.out.println("Result of future #" + i + " = " + result);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        future.cancel(true);

        future.isCancelled();

        // returns true if the task is completed (successfully or otherwise)
        future.isDone();


        System.out.println("Thread Name: " + Thread.currentThread().getName());
    }

//    public void forAllOrders(){
//        ExecutorService service = Executors.newFixedThreadPool(12);
//        try{
//            Future<Order> future = service.submit(getOrderTask());
//            Order order = future.get(); //blocking
//
//            Future<Order> future1 = service.submit(enrichTask(order));
//            order = future1.get(); //blocking
//
//            Future<Order> future2 = service.submit(performPaymentTask(order));
//            order = future2.get(); //blocking
//
//            Future<Order> future3 = service.submit(dispatchTask(order));
//            order = future3.get(); //blocking
//
//            Future<Order> future4 = service.submit(sendEmailTask(order));
//            Order order = future4.get(); //blocking
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        ExecutorService cpuBound = Executors.newFixedThreadPool(4);
//        ExecutorService ioBound = Executors.newCachedThreadPool();
//
//        for (int i = 0; i < 100; i++) {
//            // the same thread will do all the async operations
//            CompletableFuture.supplyAsync(() -> getOrderTask())
//                    .thenApply(order -> enrichTask(order))
//                    .thenApply(o -> performPaymentTask(o))
//                    .thenApply(order -> dispatchTask(order))
//                    .thenAccept(order -> sendEmailTask(order));
//
//            // if I want other threads to do the operations?
//            CompletableFuture.supplyAsync(() -> getOrderTask(), ioBound)
//                    .thenApplyAsync(order -> enrichTask(order), cpuBound)
//                    .thenApplyAsync(o -> performPaymentTask(o), ioBound)
//                    .exceptionally(e -> new FailedOrder())
//                    .thenApplyAsync(order -> dispatchTask(order), cpuBound)
//                    .thenAccept(order -> sendEmailTask(order));
//        }
//    }
//
//    private Runnable dispatchTask(Object order) {
//        if(typeOf(order).equals("FailedOrder")){
//            return () -> System.out.println("Failed to dispatch order");
//        }
//        else{
//            // biz logic
//        }
//    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return new Random().nextInt();
        }
    }
}
