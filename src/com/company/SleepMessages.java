package com.company;

public class SleepMessages {
    // Notice that main declares that it throws InterruptedException.
    // This is an exception that sleep throws when another thread interrupts the current thread while sleep is active.
    // Since this application has not defined another thread to cause the interrupt,
    // it doesn't bother to catch InterruptedException.

    public static void main(String args[])
            throws InterruptedException {
        String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
        };

        for (String s : importantInfo) {
            //Pause for 4 seconds
            // Note: you cannot assume that invoking sleep will suspend the thread for precisely the time period specified.
            // Why? 1. Interrupts 2. Underlying OS
            Thread.sleep(4000);
            //Print a message
            System.out.println(s);
        }
    }
}

