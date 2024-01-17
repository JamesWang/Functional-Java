package org.aidokay.func;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record SimpleWriter<A>(List<String> info, A value) {

    public <B> SimpleWriter<B> flatMap(Function<A, SimpleWriter<B>> func) {
        var r = func.apply(value);
        return new SimpleWriter<>(Stream.concat(info.stream(), r.info.stream()).toList(), r.value);
    }

    public <B> SimpleWriter<B> map(Function<A, B> func) {
        return new SimpleWriter<>(info, func.apply(value));
    }

    public static <A> SimpleWriter<A> lift(A value) {
        return new SimpleWriter<>(List.of(), value);
    }

    public static <T> SimpleWriter<T> log(String message) {
        return new SimpleWriter<>(List.of(message), null);
    }

    public static <T> SimpleWriter<T> identity() {
        return new SimpleWriter<>(List.of(), null);
    }
}
