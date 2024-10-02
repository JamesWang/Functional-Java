package org.aidokay.func;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ExtFunc<T, O> extends Function<T, O> {

    default <C> ExtFunc<T, C> transform(Function<O, C> transform) {
        return (T t) -> transform.apply(this.apply(t));
    }

    default <M> BiConsumer<T, M> pipe(BiConsumer<M, O> setter) {
        return (t, m) -> setter.accept(m, this.apply(t));
    }

    default <M> BiFunction<T,M, O> pipeThenGetValue(BiConsumer<M, O> setter) {
        return Funcs.bridgeWithReturning(this, Function.identity(), setter);
    }

    static <T, O> ExtFunc<T, O> asFunc(ExtFunc<T, O> func) {
        return func;
    }
}
