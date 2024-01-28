package org.aidokay.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class AsyncSumTest {

    @Test
    public void testAsyncSum_then_double_it() throws ExecutionException, InterruptedException {
        AsyncSum asyncSum = new AsyncSum();
        CompletableFuture<Integer> resultFuture = asyncSum.sumThenDoubleInt(List.of(1, 2, 3, 4, 5, 6));
        Assertions.assertEquals(42, resultFuture.get());
    }

    @Test
    public void testAsyncSum_then_double_it_sync_way() {
        AsyncSum asyncSum = new AsyncSum();
        CompletableFuture<Integer> resultFuture = asyncSum.sumThenDoubleInt(List.of(1, 2, 3, 4, 5, 6), Runnable::run);
        resultFuture.thenAccept(r -> {
            System.out.println("result=" + r);
            Assertions.assertEquals(42, r);
        });
    }

    @Test
    public void testAsyncSum_using_override() {
        AsyncSum asyncSum = new AsyncSum() {
            @Override
            public CompletableFuture<Integer> sumThenDoubleInt(List<Integer> listOfInts, Executor executor) {
                return super.sumThenDoubleInt(listOfInts, Runnable::run);
            }
        };
        CompletableFuture<Integer> resultFuture = asyncSum.sumThenDoubleInt(List.of(1, 2, 3, 4, 5, 6));
        resultFuture.thenAccept(r -> {
            Assertions.assertEquals(42, r);
        });
    }
}
