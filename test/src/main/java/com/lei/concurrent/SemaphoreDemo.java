package com.lei.concurrent;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    private static Semaphore semaphore = new Semaphore(2);

    private static class TestThread implements Runnable{

        @Override
        public void run() {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":"+ LocalDateTime.now());
            try {
                Thread.sleep(1000);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new TestThread());
            thread.setName("thread"+i);
            thread.start();
        }

    }
}
