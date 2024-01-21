package at.fhtechnikumwien.ode.common;

import java.util.function.Consumer;
import java.util.function.Function;

public class Result<T, E> {

    private final T value;
    private final E error;

    private Result(T value, E error){
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> err(E error) {
        if (error == null) {
            throw new IllegalArgumentException("Error object must be provided for err.");
        }
        return new Result<>(null, error);
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return error != null;
    }

    public T unwrap() {
        if (isOk()) {
            return value;
        }
        throw new IllegalStateException("Cannot unwrap an err result.");
    }

    public E getErr() {
        if (isErr()) {
            return error;
        }
        throw new IllegalStateException("No error object available for an ok result.");
    }

    public <U> Result<U, E> map(Function<T, U> mapper) {
        if (isOk()) {
            return Result.ok(mapper.apply(value));
        }
        return Result.err(error);
    }

    public Result<T,E> ifOk(Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(value);
        }
        return this;
    }

    public Result<T, E> ifErr(Consumer<E> consumer) {
        if (isErr()) {
            consumer.accept(error);
        }
        return this;
    }
}

