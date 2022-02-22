package com.company;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class VotingApp {
    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(10);

////        Method-1
//        Map<String, Integer> votes = new HashMap<>();
//
//        List<Future<?>> futures = new ArrayList<>(10_000);
//
//        for (int i = 0; i < 10_000; i++) {
//            futures.add(
//                    executor.submit(() -> {
//                        Integer count = votes.get("Larry");
//                        if (count == null) {
//                            votes.put("Larry", 1);
//                        } else {
//                            votes.put("Larry", count + 1);
//                        }
//                    })
//            );
//        }
//
//        executor.shutdown();
//
//        System.out.println(votes);

//        Method-2
//        Map<String, Integer> votes = new ConcurrentHashMap<>();
//
//        List<Future<?>> futures = new ArrayList<>(10_000);
//
//        for(int i = 0; i < 10_000; i++) {
//            futures.add(
//                    executor.submit(() -> {
//                        // Although ConcurrentHashMap but operations aren't atomic here
//                        Integer count = votes.get("Larry");
//                        if(count == null) {
//                            votes.put("Larry", 1);
//                        } else {
//                            votes.put("Larry", count + 1);
//                        }
//                    })
//            );
//        }
//
//        executor.shutdown();
//
//        System.out.println(votes);


//        Method-3 :: Correct way

        Map<String, Integer> votes = new ConcurrentHashMap<>();
//         This can also be used.
//        Map<String, Integer> votes = Collections.synchronizedMap(new HashMap<>());

        List<Future<?>> futures = new ArrayList<>(10_000);
        for (int i = 0; i < 10_000; i++) {
            futures.add(
                    executor.submit(() -> {
                        votes.compute("Larry", (k, v) -> (v == null) ? 1 : v + 1);
                    }));
        }
        for (Future<?> future : futures) {
            future.get();
        }
        executor.shutdown();

        System.out.println(votes);
    }

    public void castVote(String performer, Map<String, Integer> votes) {
        // synchronized keyword is the lock object.
        // When a thread enters the code block, it takes ownership of the lock.
        // Only one thread at a time can own the lock at a time.
        synchronized (this) {
            Integer count = votes.get(performer);
            if (count == null) {
                votes.put(performer, 1);
            } else {
                votes.put(performer, count + 1);
            }
        }

        // Lock objects
        // Any object can be used as the lock.
        synchronized (votes) {
            Integer count = votes.get(performer);
            if (count == null) {
                votes.put(performer, 1);
            } else {
                votes.put(performer, count + 1);
            }
        }

        // Or, you could create a completely new object just to serve the purpose of the lock:
        final Object lock = "SpecialLock";
        synchronized (lock) {
            // synchronized code block
        }
    }

    public synchronized void castVoteAnotherWay(String performer, Map<String, Integer> votes) {
        Integer count = votes.get(performer);
        if (count == null) {
            votes.put(performer, 1);
        } else {
            votes.put(performer, count + 1);
        }
    }

    private final Lock lock = new ReentrantLock();

    public void advancedCastVote(String performer, Map<String, Integer> votes) {
        lock.lock();
        try {
            votes.compute(
                    performer, (k, v) -> (v == null) ? 1 : v + 1);
        } finally {
            lock.unlock();
        }
    }
}

