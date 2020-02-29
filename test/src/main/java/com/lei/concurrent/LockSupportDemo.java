package com.lei.concurrent;

import java.time.LocalDateTime;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while (true){
                System.out.println(LocalDateTime.now());
                LockSupport.park();
            }
        });
        thread1.start();
        Thread.sleep(1000);
        LockSupport.unpark(thread1);
    }
}