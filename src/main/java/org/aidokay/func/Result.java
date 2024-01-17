package org.aidokay.func;

public sealed interface Result<T> permits Result.Failure, Result.Success {

    SimpleWriter<T> bind();

    static <A> Result<A> failure(String message) {
        return new Failure<>(message);
    }

    record Success<T>(T value) implements Result<T> {
        @Override
        public SimpleWriter<T> bind() {
            return SimpleWriter.lift(value);
        }
    }

    record Failure<T>(String message) implements Result<T> {
        @Override
        public SimpleWriter<T> bind() {
            return SimpleWriter.log(message);
        }
    }
}
