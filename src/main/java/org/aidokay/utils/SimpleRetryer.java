package org.aidokay.utils;

import org.aidokay.effects.Either;
import org.aidokay.func.TryWith;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class SimpleRetryer {
    private long currentSleepTime;
    private final int maxRetries;
    private final UnaryOperator<Long> nextSleepTimeGen;

    public SimpleRetryer(int maxRetries, UnaryOperator<Long> nextSleepTimeGen) {
        this.maxRetries = maxRetries;
        this.nextSleepTimeGen = nextSleepTimeGen;
    }

    void sleepAwhile() {
        try {
            Thread.sleep(nextSleepTime());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ;
        }
    }

    private long nextSleepTime() {
        currentSleepTime = currentSleepTime + nextSleepTimeGen.apply(currentSleepTime);
        return currentSleepTime;
    }

    public <T, R> Either<Throwable, Optional<R>> runWithRetry(TryWith.UncheckedFunc<T, R, Exception> task, T data) {
        int triedCount = 0;
        while (triedCount < maxRetries) {
            try {
                return Either.right(Optional.ofNullable(task.apply(data)));
            } catch (RuntimeException ex) {
                triedCount++;
                if (triedCount >= maxRetries) {
                    return Either.left(ex);
                }
                sleepAwhile();
            }
        }
        return Either.left(new IllegalArgumentException(String.format("Not tried at all, triedCount[%d] maxRetries=[%d]", triedCount, maxRetries)));
    }
}
