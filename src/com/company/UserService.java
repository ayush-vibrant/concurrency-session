package com.company;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserService {
    private static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int id = i;
            service.submit(() -> {
                // every task will have its own internal SimpleDateFormat object.
                // TODO: What if we make SDF global? Synchronization Issues? Locks? Impact on performance?
                String birthDate = new UserService().birthDate(id);
                System.out.println(birthDate);
            });
        }
        Thread.sleep(1000);
        service.shutdown();
    }

    private String birthDate(int userId) {
        Date birthDate = birthDateFromDB(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(birthDate);
    }

    private String birthDateOptimised(int userId) {
        Date birthDate = birthDateFromDB(userId);
        // Each thread will get its own copy
        SimpleDateFormat simpleDateFormat = ThreadSafeFormatter.dateFormatter.get();
        return simpleDateFormat.format(birthDate);
    }

    private Date birthDateFromDB(int userId) {
        return new Date();
    }

}
