package org.aidokay.func;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TryWith {
    class UncheckedException extends RuntimeException {
        public UncheckedException(String message) {
            super(message);
        }

        public UncheckedException(Throwable cause) {
            super(cause);
        }
    }

    @FunctionalInterface
    interface UncheckedFunc<I, O, E extends Exception> extends Function<I, O> {
        O applyThrows(I input) throws E;

        default O apply(I input) {
            try {
                return applyThrows(input);
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception ex) {
                throw new UncheckedException(ex);
            }
        }

        default Optional<O> withDefaultValue(I input, O defaultValue, Consumer<Exception> logger) {
            try {
                return Optional.ofNullable(applyThrows(input));
            } catch (Exception ex) {
                logger.accept(ex);
                return Optional.ofNullable(defaultValue);
            }
        }
    }

    @FunctionalInterface
    interface RunWithException<E extends Exception> extends Runnable {
        void runThrows() throws E;

        default void run() {
            try {
                runThrows();
            } catch (Exception ex) {
                throw new UncheckedException(ex);
            }
        }
    }

    @FunctionalInterface
    interface CallWithException<T, E extends Exception> extends Callable<T> {
        T callThrows() throws E;

        default T call() {
            try {
                return callThrows();
            } catch (Exception ex) {
                throw new UncheckedException(ex);
            }
        }
    }

    static <E extends Exception> void catchRunException(RunWithException<E> runnable, Consumer<Exception> logger) {
        try {
            runnable.run();
        } catch (Exception ex) {
            logger.accept(ex);
        }
    }

    static <T, E extends Exception> Optional<T> catchCallException(CallWithException<T, E> runnable, Consumer<Exception> logger) {
        try {
            return Optional.ofNullable(runnable.call());
        } catch (Exception ex) {
            logger.accept(ex);
            return Optional.empty();
        }
    }

    static <R extends AutoCloseable, T> Optional<T> useResource(Supplier<R> resource, Function<R, T> task, Consumer<Exception> logger) {
        try (R res = resource.get()) {
            return Optional.ofNullable(task.apply(res));
        } catch (Exception ex) {
            logger.accept(ex);
            return Optional.empty();
        }
    }
}
