package com.xyzwps.lib.dollar;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a value of one of two possible types (E for errors, A for successes).
 * This is a functional programming concept used as an alternative to throwing exceptions
 * or returning null, providing a more expressive way to handle computations that may fail.
 *
 * <p>The {@code Either<E, A>} type defines a static factory method {@link #left(Object)} to
 * create an instance representing a failure (left side), and {@link #right(Object)} for a
 * successful result (right side).
 * It encourages the use of Railway Oriented Programming, where the flow of data is directed based on
 * whether operations succeed or fail, allowing for explicit error handling without interrupting
 * the control flow of the program with exceptions.
 *
 * @param <E> the type of the error value (usually a subclass of {@link Exception} or a specific error type)
 * @param <A> the type of the success value
 */
public sealed interface Either<E, A> {

    /**
     * Creates an instance of `Either` representing a failure or error condition.
     * This method is used to wrap an error value of type `E`, placing it on the 'left' side of the Either,
     * indicating that a computation did not succeed.
     *
     * @param left The error value to be encapsulated in the Left instance.
     * @param <E>  The type of the error value.
     * @param <A>  The placeholder type for the potential success value (unused in Left instances).
     * @return An instance of `Either.Left<E, A>`.
     */
    static <E, A> Either<E, A> left(E left) {
        return new Left<>(left);
    }

    /**
     * Creates an instance of `Either` representing a successful computation.
     * This method is used to wrap a success value of type `A`, placing it on the 'right' side of the Either,
     * signifying that the computation was completed without errors.
     *
     * @param right The success value to be encapsulated in the Right instance.
     * @param <E>   The placeholder type for the potential error value (unused in Right instances).
     * @param <A>   The type of the success value.
     * @return An instance of `Either.Right<E, A>`.
     */
    static <E, A> Either<E, A> right(A right) {
        return new Right<>(right);
    }


    /**
     * Returns true if this is a Left instance, indicating an error or failure.
     *
     * @return true if this is a Left, false otherwise
     */
    boolean isLeft();

    /**
     * Returns true if this is a Right instance, indicating a successful computation.
     *
     * @return true if this is a Right, false otherwise
     */
    boolean isRight();

    /**
     * Retrieves the left value if it is a Left; or else throws a {@link NoSuchElementException}.
     *
     * @return the left value if it is a Left
     */
    E left();

    /**
     * Retrieves the right value if it is a Right; or else throws a {@link NoSuchElementException}
     *
     * @return the right value if it is a Right
     */
    A right();

    /**
     * Retrieves the right value if it is a Right; or else it converts the left value to an exception
     * which would be thrown.
     *
     * @param errorCreator the function that maps the left value to a exception.
     * @param <X>          The type of the exception to be thrown.
     * @return The right-side value if this is a Right instance.
     * @throws X The exception type would be thrown
     */
    <X extends Throwable> A rightOrThrow(Function<E, X> errorCreator) throws X;

    /**
     * Alias for {@link #mapRight}.
     *
     * @param mapper the function to apply to the right-side value if present
     * @param <A2>   the new type of the right-side value after applying the function
     * @return a new Either with the right side mapped if present, or the original Left
     */
    default <A2> Either<E, A2> map(Function<A, A2> mapper) {
        return this.mapRight(mapper);
    }

    /**
     * Applies the provided function if this is a Left, mapping the error value.
     * This is useful for changing the type of the error or transforming it in some way.
     *
     * @param mapper the function to apply to the left-side value if present
     * @param <E2>   the new type of the left-side value after applying the function
     * @return a new Either with the left side mapped if present, or the original Right
     */
    <E2> Either<E2, A> mapLeft(Function<E, E2> mapper);

    /**
     * Maps the right-side value of this Either, leaving the left side untouched.
     * This can be used to transform the success value while preserving the error structure.
     *
     * @param mapper the function to apply to the right-side value if present
     * @param <A2>   the new type of the right-side value after applying the function
     * @return a new Either with the right side mapped if present, or the original Left
     */
    <A2> Either<E, A2> mapRight(Function<A, A2> mapper);


    default Either<E, A> peek(Consumer<A> consumer) {
        return peekRight(consumer);
    }

    Either<E, A> peekLeft(Consumer<E> consumer);

    Either<E, A> peekRight(Consumer<A> consumer);

    /**
     * Either left representation.
     *
     * @param left left value
     * @param <E>  type of left
     * @param <A>  type of right
     */
    record Left<E, A>(E left) implements Either<E, A> {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public A right() {
            throw new NoSuchElementException();
        }

        @Override
        public <X extends Throwable> A rightOrThrow(Function<E, X> errorCreator) throws X {
            Objects.requireNonNull(errorCreator);
            throw errorCreator.apply(left);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <A2> Either<E, A2> mapRight(Function<A, A2> mapper) {
            Objects.requireNonNull(mapper);
            return (Either<E, A2>) this;
        }

        @Override
        public Either<E, A> peekLeft(Consumer<E> consumer) {
            Objects.requireNonNull(consumer).accept(left);
            return this;
        }

        @Override
        public Either<E, A> peekRight(Consumer<A> consumer) {
            Objects.requireNonNull(consumer);
            return this;
        }

        @Override
        public <E2> Either<E2, A> mapLeft(Function<E, E2> mapper) {
            Objects.requireNonNull(mapper);
            return new Left<>(mapper.apply(this.left));
        }
    }

    /**
     * Either right representation.
     *
     * @param right right value
     * @param <E>   type of left
     * @param <A>   type of right
     */
    record Right<E, A>(A right) implements Either<E, A> {
        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public E left() {
            throw new NoSuchElementException();
        }

        @Override
        public <X extends Throwable> A rightOrThrow(Function<E, X> errorCreator) throws X {
            Objects.requireNonNull(errorCreator);
            return right;
        }

        @Override
        public <A2> Either<E, A2> mapRight(Function<A, A2> mapper) {
            Objects.requireNonNull(mapper);
            return new Right<>(mapper.apply(this.right));
        }

        @Override
        public Either<E, A> peekLeft(Consumer<E> consumer) {
            Objects.requireNonNull(consumer);
            return this;
        }

        @Override
        public Either<E, A> peekRight(Consumer<A> consumer) {
            Objects.requireNonNull(consumer).accept(right);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <E2> Either<E2, A> mapLeft(Function<E, E2> mapper) {
            Objects.requireNonNull(mapper);
            return (Either<E2, A>) this;
        }
    }
}
