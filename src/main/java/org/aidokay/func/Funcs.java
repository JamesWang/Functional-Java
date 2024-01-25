package org.aidokay.func;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Funcs {
    private Funcs() {
    }

    //Kestrel
    public static <A, B> A selectFirst(A a, B b) {
        return a;
    }

    //Kite
    public static <A, B> B selectSecond(A a, B b) {
        return b;
    }

    public static <A, B, C> BiFunction<B, A, C> flip(BiFunction<A, B, C> biFunction) {
        return (b, a) -> biFunction.apply(a, b);
    }

    //Flip
    public static <A, B, C> Function<B, Function<A, C>> curry(BiFunction<A, B, C> biFunc) {
        return b -> a -> biFunc.apply(a, b);
    }

    //chain
    public static <A, B, C> Function<B, C> chain(BiFunction<A, B, C> biFunction, Function<B, A> func) {
        return b -> biFunction.apply(func.apply(b), b);
    }

    //substitution $
    public static <A, B, C> Function<A, C> ap(BiFunction<A, B, C> biFunction, Function<A, B> func) {
        return a -> biFunction.apply(a, func.apply(a));
    }

    public static <V> V tap(Consumer<V> consumer, V value) {
        consumer.accept(value);
        return value;
    }

    //Y combinator
    //Y = λg.(λx.g(x x) (λx.g(x x))
    //Y = g -> (x -> g(x(x))(x -> g(x(x))

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
