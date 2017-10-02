package com.company;

import java.time.LocalTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

public  class StampedLockTester extends ConCurrentThreadTester {


    StampedLock lock =  new StampedLock();
    private Thread getLockThread( String name, int mode )
    {
        return new Thread(() -> {
            for (; ; ) {
                System.out.println(LocalTime.now() + Thread.currentThread().getName() + " " + "begin!");
                long stamp = 0;
                boolean tryLockflag = true;
                try {
                    if (mode == 0)
                    {
                        stamp = lock.tryOptimisticRead();//乐观读!
                        printReadThreadSatus();//执行操作
                        if (tryLockflag=(!lock.validate(stamp))) {//测试是否成功
                            stamp = lock.readLock();//失败，有write锁，使用Readlock，重新尝试
                            printReadThreadSatus();
                        }
                    } else {
                        stamp = lock.writeLock();
                        doAndPrintWriteThread();
                        System.out.println(LocalTime.now() + lock.toString());
                    }
                    Thread.sleep(2500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (tryLockflag) lock.unlock(stamp);
                    if (mode == 1) try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(LocalTime.now() + Thread.currentThread().getName() + " unlock!");
                }
            }
        }, name);
    }

    @Override
    void lockTest() {
        for (int i = 0 ; i<10;i++)
        {
            threads.add(getLockThread("thread-read-"+i,0));
        }
        threads.add(getLockThread("thread-Write",1));
        this.startThreads();
    }
}
