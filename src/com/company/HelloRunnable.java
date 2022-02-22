package com.company;

// Which of these idioms should you use?
// The first idiom, which employs a Runnable object, is more general,
// because the Runnable object can subclass a class other than Thread

public class HelloRunnable implements Runnable {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new Thread(new HelloRunnable())).start();
    }

}
