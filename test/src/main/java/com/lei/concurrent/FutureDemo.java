package com.lei.concurrent;

import java.util.concurrent.*;

public class FutureDemo {

    private static int i;

    static class MyCallable implements Callable{

        @Override
        public Object call() throws Exception {
            Thread.sleep(500);
            return i++;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        long time;
        time = System.currentTimeMillis();
        FutureTask futureTask = new FutureTask(new MyCallable());
        Thread thread = new Thread(futureTask);
        thread.start();
        futureTask.get(200, TimeUnit.MILLISECONDS);
        time -= System.currentTimeMillis();
        System.out.println(time + "ms");

    }

}
