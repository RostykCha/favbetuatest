package automationframeworkexample.features.parallelisation;

import automationframeworkexample.clients.ui.utils.wrappers.WaiterWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureExample completableFutureExample = new CompletableFutureExample();
        completableFutureExample.parallelCompletableFuture();
    }

    public void plainCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
             WaiterWrapper.implicitWait(1);
            return "Hello, future1!";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
             WaiterWrapper.implicitWait(3);
            return "Hello, future2!";
        });


        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
             WaiterWrapper.implicitWait(5);
            return "Hello, future3!";
        });

        future1.thenAccept(result -> System.out.println(result));

        future2.thenAccept(result -> System.out.println(result));

        future3.thenAccept(result -> System.out.println(result));

        future3.get();
    }

    public void parallelCompletableFuture() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
             WaiterWrapper.implicitWait(1);
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
             WaiterWrapper.implicitWait(3);
            return "Result 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
             WaiterWrapper.implicitWait(5);
            return "Result 3";
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        allFutures.thenRun(() -> {
            // All futures completed
            String result1 = future1.join();
            String result2 = future2.join();
            String result3 = future3.join();
            System.out.println(result1 + ", " + result2 + ", " + result3);
        });

        try {
            allFutures.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
