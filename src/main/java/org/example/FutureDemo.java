package org.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        method();
    }

    private static void method() throws ExecutionException, InterruptedException {
        /**
         * 异步线程一
         */
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f1";
        }).whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String result, Throwable throwable) {
                System.out.println("返回结果：" + result);
                System.out.println("异常信息：" + throwable);
            }
        }).exceptionally((e)->{
            System.out.println("异常进入");
            return "f4";
        });

        /**
         * 异步线程二
         */
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        }).whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String result, Throwable throwable) {
                System.out.println("返回结果：" + result);
                System.out.println("异常信息：" + throwable);
            }
        }).exceptionally((e)->{
            System.out.println("异常进入");
            return "f3";
        });

        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);

        //阻塞，直到所有任务结束。
        System.out.println(System.currentTimeMillis() + ":阻塞");
        all.join();
        System.out.println(System.currentTimeMillis() + ":阻塞结束");

        //一个需要耗时2秒，一个需要耗时3秒，只有当最长的耗时3秒的完成后，才会结束。
        System.out.println("任务均已完成。");
    }

}
