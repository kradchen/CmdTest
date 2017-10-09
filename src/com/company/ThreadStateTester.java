package com.company;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadStateTester {
    private ThreadStateTester(){
    }
    private void threadStateCycle() throws InterruptedException
    {
        boolean flag=true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("ThreadStateCycle Start!");
        while (flag)
        {
            System.out.println("Input Selection:");
            System.out.println("1.Sleep State Test!");
            System.out.println("2.Join State Test!");
            System.out.println("3.Park & interrupt State Test!");
            System.out.println("Input:");
            switch (scanner.nextInt())
            {
                case 1:
                    showSleepState();
                case 2:
                    showJoinState();
                case 3:
                    showParkState();
                default:
            }
        }
    }
    private void showSleepState() throws InterruptedException {
        Thread t1= new Thread(()->{
                try {
                    System.out.println("thread1 start!");
                    System.out.println("thread1 begin Sleep for 20s!");
                    Thread.sleep(20000);
                    System.out.println("thread1 wakeup!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        },"thread1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Thread1 state is "+t1.getState());
        System.out.println("interrupt thread1 !");
        t1.interrupt();//中断sleep的线程之后会抛出错误
    }
    private void showJoinState()throws InterruptedException
    {
        Thread t3;
        Thread t2 = new Thread(()-> {
            System.out.println("Join-thread start ");
            System.out.println("Join-thread begin Sleep for 5s!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Join-thread state is "+Thread.currentThread().getState());
            System.out.println("Join-thread end!");
        },"th2-joinstarter");
        t3 = new Thread(()->{
            try {
                System.out.println("Start-thread start!");
                t2.start();
                System.out.println("Join-thread state is "+t2.getState());
                System.out.println("Start-thread state is "+Thread.currentThread().getState());
                System.out.println("Join-thread join!");
                t2.join();
                System.out.println("Start-thread end!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"th3-joinend");
        t3.start();
        TimeUnit.SECONDS.sleep(2);//估算一个时间获取t3的
        System.out.println("Thread3 state is "+t3.getState());
        TimeUnit.SECONDS.sleep(3);
    }
    private void showParkState()throws InterruptedException
    {
        Thread t4 = new Thread(()->{
            System.out.println("Test-thread start !");
            System.out.println("Park test-thread");
            LockSupport.park();//挂起线程，程序会听在这一步
            System.out.println("Test-thread begin working！");
            int i=0;
            while (true)
            {
                if(Thread.currentThread().isInterrupted() && i<3)
                {
                    System.out.println("Test-thread is interrupted！");
                    System.out.println("Test-thread state is "+Thread.currentThread().getState());
                    i++;
                }
                if (i>=3) break;
            }

        });
        t4.start();
        TimeUnit.SECONDS.sleep(1);//等一等线程挂起
        System.out.println("Thread4 state is "+t4.getState());
        System.out.println("Unpark test-thread");
        LockSupport.unpark(t4);
        TimeUnit.SECONDS.sleep(1);//等一等线程复苏
        System.out.println("Thread4 state is "+t4.getState());
        System.out.println("Interrupt test-thread");
        t4.interrupt();
        TimeUnit.SECONDS.sleep(1);//等待一下线程中断后完成！
        System.out.println("Thread4 state is "+t4.getState());
    }
    static void threadStateTest() throws InterruptedException {
        ThreadStateTester t= new ThreadStateTester();
        t.threadStateCycle();
    }




}
