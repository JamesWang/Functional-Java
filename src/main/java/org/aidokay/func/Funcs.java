package org.aidokay.func;

import java.util.function.*;

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

    public static <T, R> Function<T, R> yCombinator(UnaryOperator<Function<T, R>> f) {
        return t -> {
            final Function<T, R>[] result = new Function[1];
            result[0] = f.apply(x -> result[0].apply(x));
            return result[0].apply(t);
        };
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

    /**
     * This method is used for cases like, pick values from one object's getter and set to another object using its setters
     *
     * @param getter - Object A's getter method used to get the value
     * @param setter - Object B's setter method used to set the getter returned value to object of B
     * @param <A>    - Source Object type used to pick up values from
     * @param <B>    - Target Object type used to set values to
     * @param <C>    - intermediate type of the value returned by getter
     * @return - A new BiConsumer which accept Object A and Object B
     */
    public static <A, B, C> BiConsumer<A, B> bridge(Function<A, C> getter, BiConsumer<B, C> setter) {
        return bridge(getter, Function.identity(), setter);
    }

    public static <A, B, C, D> BiConsumer<A, B> bridge(Function<A, C> getter, Function<C, D> transformer, BiConsumer<B, D> setter) {
        return (a, b) -> setter.accept(b, getter.andThen(transformer).apply(a));
    }

    public static <A, B, C, D> BiFunction<A, B, D> bridgeWithReturning(Function<A, C> getter, Function<C, D> transformer, BiConsumer<B, D> setter) {
        return (a, b) -> {
            var value = getter.andThen(transformer).apply(a);
            setter.accept(b, value);
            return value;
        };
    }
}
