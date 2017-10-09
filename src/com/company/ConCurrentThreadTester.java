package com.company;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ConCurrentThreadTester {
    protected List<Thread> threads = new ArrayList<>();
    protected int count = 0;
    protected void startThreads() {
        if (threads.size() > 0) {
            for (Thread t : threads) t.start();
        }
    }

    protected void doAndPrintWriteThread() {
        System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "get lock!");
        count++;
        System.out.println(LocalTime.now() + Thread.currentThread().getName() + " add count to " + count);
    }

    protected void printReadThreadSatus() {
        System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + " readlock!");
        System.out.println(LocalTime.now() + Thread.currentThread().getName() + " count:" + count);
    }

    abstract void lockTest();
}
