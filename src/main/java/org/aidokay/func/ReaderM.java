package org.aidokay.func;

import java.util.function.BiFunction;
import java.util.function.Function;

public record ReaderM<A, B>(Function<A, B> run) {
    public <C> ReaderM<A, C> flatMap(Function<B, ReaderM<A, C>> func) {
        return lift(a -> run.andThen(func).apply(a).run.apply(a));
    }

    public <C> ReaderM<A, C> andThen(Function<B, ReaderM<A, C>> func) {
        return this.flatMap(func);
    }

    public ReaderM<A, B> combine(BiFunction<A, B, B> biFunc) {
        return lift(a -> biFunc.apply(a, run.apply(a)));
    }

    public static <F, T> ReaderM<F, T> lift(Function<F, T> func) {
        return new ReaderM<>(func);
    }

    public B apply(A a) {
        return run.apply(a);
    }
}
