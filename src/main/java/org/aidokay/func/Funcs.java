package org.aidokay.func;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Funcs {
    private Funcs() {
    }

    public static <A, B, C> Function<B, C> partailA(BiFunction<A, B, C> biFunction, A a) {
        return b -> biFunction.apply(a, b);
    }

    public static <A, B, C> Function<A, C> partailB(BiFunction<A, B, C> biFunction, B b) {
        return a -> biFunction.apply(a, b);
    }

    public static <A, B> Function<A, B> asFunc(Function<A, B> func) {
        return func;
    }


}
