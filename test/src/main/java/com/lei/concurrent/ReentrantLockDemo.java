package com.lei.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static int i = 0;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Thread thread1 = new Thread(()->{
            for (int j = 0; j < 10000; j++) {
                lock.lock();
                try {
                    i++;
                    condition2.signal();
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+":"+ i);
                if(i == 20){
                    condition2.signal();
                }
                lock.unlock();
            }
        });
        Thread thread2 = new Thread(()->{
            for (int j = 0; j < 10000; j++) {
                lock.lock();
                try {
                    i++;
                    condition1.signal();
                    condition2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+":"+ i);
                lock.unlock();
                String a = "123";
            }
        });
        thread1.setName("t1");
        thread2.setName("t2");
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
            System.out.println("final i="+i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
