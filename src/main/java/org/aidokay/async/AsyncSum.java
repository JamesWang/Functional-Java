package org.aidokay.async;

import org.aidokay.func.Funcs;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;

public class AsyncSum {

    public int sumInt(List<Integer> listOfInts) {
        List<Integer> tobeSummed = Objects.requireNonNullElseGet(listOfInts, List::of);
        return tobeSummed.stream().reduce(Integer::sum).orElse(0);
    }

    public final BiFunction<Integer, Integer, Integer> timesAnInt = (n, m) -> n * m;

    public int doubleAnInt(Integer number) {
        return Funcs.curry(timesAnInt).apply(2).apply(number);
    }

    public CompletableFuture<Integer> sumThenDoubleInt(List<Integer> listOfInts) {
        return sumThenDoubleInt(listOfInts, ForkJoinPool.commonPool());
    }

    public CompletableFuture<Integer> sumThenDoubleInt(List<Integer> listOfInts, Executor executor) {
        return CompletableFuture.supplyAsync(() -> sumInt(listOfInts), executor)
                .thenComposeAsync(n -> CompletableFuture.supplyAsync(() -> this.doubleAnInt(n), executor));
    }
}
