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
        Objects.requireNonNull(func);
        return isRight() ? Either.right(func.apply(right)) : Either.left(left);
    }

    public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> func) {
        Objects.requireNonNull(func);
        return isRight() ? func.apply(right) : Either.left(left);
    }

    public R getOrElse(Supplier<R> supplier) {
        Objects.requireNonNull(supplier);
        return isRight() ? this.right : supplier.get();
    }

    public R getOrElseGet(Function<? super L, ? extends R> func) {
        Objects.requireNonNull(func);
        return isRight() ? this.right : func.apply(left);
    }

    public <E extends Throwable> R getOrElseThrow(Function<? super L, E> execFunc) throws E {
        Objects.requireNonNull(execFunc);
        if (isRight()) {
            return this.right();
        }
        throw execFunc.apply(this.left);
    }

    public Either<L, R> peek(Consumer<? super R> action) {
        Objects.requireNonNull(action);
        if (isRight()) {
            action.accept(right);
        }
        return this;
    }

    public static <E, T> Either<E, T> fromOptional(Supplier<Optional<T>> result, Supplier<E> errorSupplier) {
        return result.get().<Either<E, T>>map(Either::right).orElseGet(() -> Either.left(errorSupplier.get()));
    }

    public boolean isRight() {
        return left == null && right != null;
    }
}
