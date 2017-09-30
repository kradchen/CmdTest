package com.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    List<Thread> threadArrayList = new ArrayList<>();
    int count = 0;
    void  tryReentrantLockTest()
    {
        clearThreadList();
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = getSimpleLockThread(lock,200,"First");
        Thread thread2 = getSimpleLockThread(lock,200,"Second");
        threadArrayList.add(thread1);
        threadArrayList.add(thread2);
        runThreadList();
    }
    void  tryReentrantTryLockTest()
    {
        clearThreadList();
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = getTryLockThread(lock,200,"First");
        Thread thread2 = getTryLockThread(lock,200,"Second");
        threadArrayList.add(thread1);
        threadArrayList.add(thread2);
        runThreadList();
    }

    private Thread getSimpleLockThread(ReentrantLock lock, int sleepTime, String name) {
        return new Thread(() -> {
            for (; ; ) {
                System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " Begin!");
                lock.lock();
                try {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " get lock!");
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " add count to " + count);
                } finally {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " release lock!");
                    lock.unlock();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },name);
    }
    
    private Thread getTryLockThread(ReentrantLock lock, int sleepTime, String name) {
        return new Thread(() -> {
            for (; ; ) {
                System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " Begin!");
                try {
                    while (!lock.tryLock()) {
                        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " try lock failed!");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " try lock sucessed!");
                    count++;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finally {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " release  lock!");
                    lock.unlock();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        },name);
    }
    private void clearThreadList() {
        if (threadArrayList.size()>0)
            threadArrayList.clear();
    }
    private void runThreadList()
    {
        for (Thread t : threadArrayList)
            t.start();
    }
}
