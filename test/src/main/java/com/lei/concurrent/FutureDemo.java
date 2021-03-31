package com.lei.concurrent;

import com.google.common.util.concurrent.*;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.Future;

import java.util.concurrent.*;

public class FutureDemo {

    private static int i;

    static class MyCallable implements Callable{

        @Override
        public Object call() throws Exception {
            Thread.sleep(100);
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
        guavaFuture();

    }

    //guava的future
    public static void guavaFuture(){
        ListenableFutureTask futureTask = ListenableFutureTask.create(new MyCallable());
        futureTask.addListener(() -> {
            System.out.println("listen");
        }, MoreExecutors.sameThreadExecutor());
        Futures.addCallback(futureTask, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object o) {
                System.out.println("success:" + o);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
    }

    //netty的future
    public static void nettyFuture(){
    }

}
