package com.company;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadTest {
    private List<Thread> threads = new ArrayList<>();
    private int count = 0;
    private void startThreads() {
        if (threads.size() > 0) {
            for (Thread t : threads) t.start();
        }
    }
    //普通重入锁控制的线程，用于模拟阻塞
    private Thread getRLockThread(Lock lock, int sleepTime, String name)
    {
        return new Thread(() -> {
            for (; ; ) {
                System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "begin!");
                lock.lock();
                try {
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "get lock!");
                    count++;
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " add count to " + count);
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

    //ReadLock的线程
    private Thread getReadLockThread(Lock lock, int sleepTime, String name)
    {
        return new Thread(() ->{
            for (; ; ) {
                System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "begin!");
                lock.lock();
                try {
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + " readlock!");
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " count:" + count);
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

    void reentrantLockTest()
    {
        ReentrantLock  lock =  new ReentrantLock();
        threads.add(getRLockThread(lock , 100, "First"));
        threads.add(getRLockThread(lock , 100, "Second"));
        threads.add(getRLockThread(lock , 100, "Third"));
        threads.add(getRLockThread(lock , 100, "Fouth"));
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
        threads.add(getRLockThread(lock.writeLock() , 100, "wFirst"));
        threads.add(getRLockThread(lock.writeLock() , 100, "wSecond"));
        startThreads();
        getLockStatus(lock).start();
    }
}
