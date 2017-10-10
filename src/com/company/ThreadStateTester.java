package com.company;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadStateTester {

    private static ThreadStateTester t;

    private ThreadStateTester(){
    }

    /**
     * 程序主循环，控制输入、输出逻辑
     * @throws InterruptedException
     */
    private void threadStateCycle() throws InterruptedException
    {
        boolean flag=true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("ThreadStateCycle Start!");
        while (flag)//循环接受指令
        {
            System.out.println("Input Selection:");
            System.out.println("1.Sleep State Test!");
            System.out.println("2.Join State Test!");
            System.out.println("3.Park & interrupt State Test!");
            System.out.println("4.showCodeLockWait State Test!");
            System.out.println("5.Block State Test!");
            System.out.println("Input:");
            int i = scanner.nextInt();
            switch (i)
            {
                case 1:
                    showSleepState();break;
                case 2:
                    showJoinState();continue;
                case 3:
                    showParkState();continue;
                case 4:
                    showCodeLockWaitState();continue;
                case 5:
                    showBlockState();continue;
                default:
            }
        }
    }

    /**
     * 演示线程调用sleep之后线程的状态是为WAITING
     * @throws InterruptedException
     */
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
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 演示Join函数的作用，实际上Join函数的真正意思应该是插队，
     * 即但在A线程中调用了B.join()时，B就插队到A线程之前，A进入WAITING状态，B完成
     * 之后才轮到A线程运行。
     * @throws InterruptedException
     */
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

    /**
     * 演示了LockSupport中的Park方法后，线程实际为WAITING状态
     * @throws InterruptedException
     */
    private void showParkState()throws InterruptedException
    {
        Thread t4 = new Thread(()->{
            System.out.println("Test-thread start !");
            System.out.println("Park test-thread");
            LockSupport.park();//挂起线程，程序会停在这一步直到其他线程unpark本线程
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
    //~~~output


    /**
     * 需要区分使用ConCurrent.lock包中的各类代码锁时，实际上竞争锁资源时，
     * 代码运用LockSupport类的park方法将相关线程挂起了，所以在线程最后lock
     * 失败进入的是WAITING状态，而非BLOCKED状态
     * @throws InterruptedException
     */
    private void showCodeLockWaitState() throws InterruptedException
    {
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(()-> {
            System.out.println("Thread1 start !");
            System.out.println("Thread1 lock");
            lock.lock();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    //长期占有锁，除非被中断
                }
            } finally {
                lock.unlock();
                System.out.println("Thread1 unlock");
                System.out.println("Thread1 end");
            }
        });
        t.start();
        Thread t2 = new Thread(()->{
            try {
                System.out.println("Thread2 start !");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Thread2 try lock!");
                lock.lock();//t1不释放会一直阻塞
                System.out.println("Thread2 get lock!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread2 unlock");
                System.out.println("Thread2 end");
            }
        });
        t2.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Thread1 state is "+t.getState());
        System.out.println("Thread2 state is "+t2.getState());
        t.interrupt();
        TimeUnit.SECONDS.sleep(1);//等待Thread1的中断生效！
        System.out.println("Thread1 state is "+t.getState());
        System.out.println("Thread2 state is "+t2.getState());
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 运用synchronized进行锁定操作，线程会真正进入Blocked状态
     * output:
     * Thread1 start !
     * Thread1 lock
     * Thread2 start !
     * Thread2 try lock!
     * Thread1 state is RUNNABLE
     * Thread2 state is BLOCKED
     * Thread1 unlock
     * Thread1 end
     * Thread2 get lock!
     * Thread2 unlock
     * Thread2 end
     * Thread1 state is TERMINATED
     * Thread2 state is TERMINATED
     * @throws InterruptedException
     */
    private void showBlockState() throws InterruptedException
    {
        Object lockobject = new Object();
        Thread t = new Thread(()-> {
            System.out.println("Thread1 start !");
            System.out.println("Thread1 lock");
            synchronized (lockobject) {
                while (!Thread.currentThread().isInterrupted()) {
                    //长期占有锁，除非被中断
                }
            }
            System.out.println("Thread1 unlock");
            System.out.println("Thread1 end");
        });
        t.start();
        Thread t2 = new Thread(()-> {
            try {
                System.out.println("Thread2 start !");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Thread2 try lock!");
                synchronized (lockobject) {//在线程1占有锁时，阻塞
                    System.out.println("Thread2 get lock!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread2 unlock");
            System.out.println("Thread2 end");
        });
        t2.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Thread1 state is "+t.getState());
        System.out.println("Thread2 state is "+t2.getState());
        t.interrupt();
        TimeUnit.SECONDS.sleep(1);//等待Thread1的中断生效！
        System.out.println("Thread1 state is "+t.getState());
        System.out.println("Thread2 state is "+t2.getState());
        TimeUnit.SECONDS.sleep(2);
    }

    static void threadStateTest() throws InterruptedException {
        t = new ThreadStateTester();
        t.threadStateCycle();
    }
}
