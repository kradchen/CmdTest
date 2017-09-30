package com.company;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class Main {

    public static int  count=0;
    public static void main(String[] args) {
	// write your code here
//        List<String> aryList = new ArrayList<>(Country.names(20));
//        List<String> linList =  new LinkedList<>(Country.names(20));
//        System.out.printf("ArrayList Printing...");
//        System.out.printf(aryList.toString());
//        System.out.println();
//        Collections.sort(aryList,Collections.reverseOrder());
//        System.out.printf("Sorted ArrayList Printing...");
//        System.out.printf(aryList.toString());
//        System.out.println();
//        System.out.printf("LinkList Printing...");
//        System.out.printf(linList.toString());
//        System.out.println();
//        Collections.shuffle(linList);
//        System.out.printf("Shuffle LinkList Printing...");
//        System.out.printf(linList.toString());
//        System.out.println();
//        Map<String,String> conMap = Country.map;
//        System.out.printf("Map Printing...");
//        System.out.printf(conMap.toString());
//        System.out.println();
//        Map<String,String> newMap = new HashMap<>();
//        for(String name: conMap.keySet())
//        {
//            if(name.startsWith("A")){
//                newMap.put(name,conMap.get(name));
//            }
//        }
//        System.out.printf("Selected Map Printing...");
//        System.out.printf(newMap.toString());
//        System.out.println();
//
//
//        List<String> txtList = new LinkedList<>();
//        TextFileGenerator tfg = new TextFileGenerator();
//        while (tfg.hasNext()) {
//            txtList.add(tfg.next());
//        }
//        System.out.printf("Text file content Printing...");
//        System.out.printf(txtList.toString());
//        System.out.println();
          ReentrantLock lock= new ReentrantLock();
          Thread thread1 = new Thread(new Runnable() {
              @Override
              public void run() {
                  for (;;)
                  {
                      lock.lock();
                      try {
                            count++;
                            System.out.println(Thread.currentThread().getName()+" "+ count);
                      }
                      finally {
                          lock.unlock();
                          try {
                              Thread.sleep(50);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              }
          });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    lock.lock();
                    try {
                        count++;
                        System.out.println(Thread.currentThread().getName()+" "+count);
                    }
                    finally {
                        lock.unlock();
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                while(true);

            }
        });
        thread1.start();
        thread2.start();

    }


}
