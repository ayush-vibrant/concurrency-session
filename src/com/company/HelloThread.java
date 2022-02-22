package com.company;

// The Thread class defines a number of methods useful for thread management.
// These include static methods, which provide information about, or affect the status of, the thread invoking the method.
// The other methods are invoked from other threads involved in managing the thread and Thread object
public class HelloThread extends Thread {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new HelloThread()).start();
    }

}

