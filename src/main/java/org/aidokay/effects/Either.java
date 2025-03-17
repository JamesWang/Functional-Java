package org.aidokay.effects;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record Either<L, R>(L left, R right) {
    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(null, right);
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(left, null);
    }

    public <U> Either<L, U> map(Function<? super R, ? extends U> func) {
        Objects.requireNonNull(func, "func cannot be null in map()");
        return isRight() ? Either.right(func.apply(right)) : Either.left(left);
    }

    public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> func) {
        Objects.requireNonNull(func, "func cannot be null in flatMap()");
        return isRight() ? func.apply(right) : Either.left(left);
    }

    public R getOrElse(Supplier<R> supplier) {
        Objects.requireNonNull(supplier, "supplier must be provided in getOrElse()");
        return isRight() ? this.right : supplier.get();
    }

    public R getOrElseGet(Function<? super L, ? extends R> func) {
        Objects.requireNonNull(func, "func cannot be null in getOrElseGet()");
        return isRight() ? this.right : func.apply(left);
    }

    public <E extends Throwable> R getOrElseThrow(Function<? super L, E> execFunc) throws E {
        Objects.requireNonNull(execFunc, "execFunc must be provided in getOrElseThrow()");
        if (isRight()) {
            return this.right();
        }
        throw execFunc.apply(this.left);
    }

    public Either<L, R> peek(Consumer<? super R> consumer) {
        Objects.requireNonNull(consumer, "consumer argument must be provided in peek()");
        if (isRight()) {
            consumer.accept(right);
        }
        return this;
    }

    public <T> T fold(Function<? super L, ? extends T> leftFunc, Function<? super R, ? extends T> rightFunc) {
        Objects.requireNonNull(leftFunc, "leftFunc must be provided in fold()");
        Objects.requireNonNull(rightFunc, "rightFunc must be provided in fold()");
        return isRight() ? rightFunc.apply(right) : leftFunc.apply(left);
    }

    public static <E, T> Either<E, T> fromOptional(Supplier<Optional<T>> result, Supplier<E> errorSupplier) {
        Objects.requireNonNull(result, "result argument must be provided");
        Objects.requireNonNull(errorSupplier, "errorSupplier should be provided");
        return result.get().<Either<E, T>>map(Either::right).orElseGet(() -> Either.left(errorSupplier.get()));
    }

    public boolean isRight() {
        return left == null && right != null;
    }
}
