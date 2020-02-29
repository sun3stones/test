package com.lei.concurrent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Foo {

    ReentrantLock lock = new ReentrantLock(true);
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    private static volatile int flag = 0;

    public Foo() {
        
    }

    public void first(Runnable printFirst) throws InterruptedException {
        try {
            lock.lock();
            // printFirst.run() outputs "first". Do not change or remove this line.
            while (flag != 0){
                c1.await();
            }
            flag = 1;
            printFirst.run();
            c2.signal();
        }finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        try {
            lock.lock();
            while (flag != 1){
                c1.await();
            }
            flag = 2;
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            c3.signal();
        }finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        try {
            lock.lock();
            while (flag != 2){
                c3.await();
            }
            flag = 0;
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
            c1.signal();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        Thread t1 = new Thread(()->{
            System.out.println("first"+flag);
        });
        Thread t2 = new Thread(()->{
            System.out.println("second"+flag);
        });
        Thread t3 = new Thread(()->{
            System.out.println("third"+flag);
        });
        Foo foo = new Foo();

        Thread tt1 = new Thread(() -> {
            try {
                foo.third(t3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread tt2 = new Thread(() -> {
            try {
                foo.first(t1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread tt3 = new Thread(() -> {
            try {
                foo.second(t2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ExecutorService es = Executors.newFixedThreadPool(3);
        Set set = new HashSet();
        String num = "";
        while (true){
            set.clear();
            MyRunnable r1 = new MyRunnable(1,tt1);
            MyRunnable r2 = new MyRunnable(2,tt2);
            MyRunnable r3 = new MyRunnable(3,tt3);
            set.add(r1);
            set.add(r2);
            set.add(r3);
            Iterator<MyRunnable> iterator = set.iterator();
            while (iterator.hasNext()){
                MyRunnable runnable = iterator.next();
                num += runnable.getId();
                es.execute(runnable.getRunnable());
            }
           // Thread.sleep(5000);
            //System.out.println("提交序号:"+num);
            num ="";
            //Thread.sleep(2000);
        }
    }

    static class MyRunnable{
        private int id;
        private Runnable runnable;

        public MyRunnable() {
        }

        public MyRunnable(int id, Runnable runnable) {
            this.id = id;
            this.runnable = runnable;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}