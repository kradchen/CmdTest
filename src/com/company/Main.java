package com.company;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class Main {

    public static int  count=0;
    public static void main(String[] args) {
	// write your code here
        ThreadTest t=new ThreadTest();
        t.readWriteLockTest();

    }


}
