package com.company;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleLockThreadTester extends ConCurrentThreadTester {
    private Thread getLockThread(Lock lock, int sleepTime, String name)
    {
        return getLockThread(lock,sleepTime,name,false);
    }
    private Thread getReadLockThread(Lock lock, int sleepTime, String name)
    {
        return getLockThread(lock,sleepTime,name,false);
    }
    //普通重入锁控制的线程，用于模拟阻塞
    private Thread getLockThread(Lock lock, int sleepTime, String name, boolean isWrite )
    {
        return new Thread(() -> {
            for (; ; ) {
                System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "begin!");
                lock.lock();
                try {
                    if (isWrite) doAndPrintWriteThread();
                    else  printReadThreadSatus();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " unlock!");
                }
            }
        }, name);
    }

    private Thread getLockStatus(ReentrantReadWriteLock lock)
    {
        return  new Thread(()->{
            for (;;){
                System.out.println("++++++++++++++++++++++++++++++++");
                System.out.println(LocalTime.now() + lock.toString());
                System.out.println("++++++++++++++++++++++++++++++++");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    void lockTest()
    {
        ReentrantLock  lock =  new ReentrantLock();
        threads.add(getLockThread(lock , 100, "First"));
        threads.add(getLockThread(lock , 100, "Second"));
        threads.add(getLockThread(lock , 100, "Third"));
        threads.add(getLockThread(lock , 100, "Fouth"));
        startThreads();
        //GetLockedThreadSatus(lock).start();
    }
    void readWriteLockTest()
    {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read1"));
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read2"));
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read3"));
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read4"));
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read5"));
        threads.add(getReadLockThread(lock.readLock()  , 1000, "Read6"));
        threads.add(getLockThread(lock.writeLock() , 100, "wFirst"));
        threads.add(getLockThread(lock.writeLock() , 100, "wSecond"));
        startThreads();
        getLockStatus(lock).start();
    }

}
