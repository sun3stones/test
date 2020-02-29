package com.lei.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsDemo {

    abstract class Message{
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public abstract void sendMessage();
    }

    class Thread1 extends Message implements Runnable{

        @Override
        public void run() {
            sendMessage();
        }

        @Override
        public void sendMessage() {
            System.out.println("send message1");
        }
    }
    class Thread2 extends Message implements Runnable{
        @Override
        public void run() {
            sendMessage();
        }

        @Override
        public void sendMessage() {
            System.out.println("send message2");
        }
    }
    class Thread3 extends Message implements Runnable{
        @Override
        public void run() {
            sendMessage();
        }

        @Override
        public void sendMessage() {
            System.out.println("send message3");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorsDemo demo = new ExecutorsDemo();
        ExecutorService es = Executors.newFixedThreadPool(3);
        Thread1 thread1 = demo.new Thread1();
        Thread2 thread2 = demo.new Thread2();
        Thread3 thread3 = demo.new Thread3();
        while (true){
            es.execute(thread1);
            es.execute(thread2);
            es.execute(thread3);
            Thread.sleep(1000);
        }
    }
}
