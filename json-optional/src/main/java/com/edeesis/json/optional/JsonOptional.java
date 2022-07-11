package com.edeesis.json.optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Similar semantically to {@link java.util.Optional}, but holds a special distinction
 * between optional/undefined keys and keys with null values.
 *
 * Undefined is similar to {@link Optional#empty()}, but unlike {@link Optional},
 * the following will pass a nullable Object as a parameter:
 *
 * {@link JsonOptional#ifPresent}
 * {@link JsonOptional#ifPresentOrElse}
 * {@link JsonOptional#filter}
 * {@link JsonOptional#map}
 * {@link JsonOptional#flatMap}
 *
 * Type parameters:
 * <T> – the type of value
 */
@SuppressWarnings("unused")
public class JsonOptional<T> {

    /**
     * Common instance for {@code empty()}.
     */
    private static final JsonOptional<?> UNDEFINED = new JsonOptional<>(null);

    /**
     * Special value to distinguish between the lack of a JSON key and the presence of a JSON key with its value being null.
     */
    private static final Object NULL_VALUE = new Object();
    /**
     * Common instance for {@code nullValue()}
     */
    private static final JsonOptional<?> NULL = new JsonOptional<>(NULL_VALUE);


    /**
     * If non-null, the value; if null, indicates no value is present
     */
    @Nullable private final T value;

    /**
     * Returns an empty {@code JsonOptional} instance.  No value is present for this
     * {@code JsonOptional}.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} against instances returned by
     * {@code JsonOptional.empty()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> The type of the non-existent value
     * @return an absent (undefined) {@code JsonOptional}
     */
    public static<T> JsonOptional<T> empty() {
        @SuppressWarnings("unchecked")
        JsonOptional<T> t = (JsonOptional<T>) UNDEFINED;
        return t;
    }

    /**
     * Returns an empty {@code JsonOptional} instance.  No value is present for this
     * {@code JsonOptional}. Equivalent to {@link #empty()}.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} against instances returned by
     * {@code JsonOptional.empty()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> The type of the non-existent value
     * @return an absent (undefined) {@code JsonOptional}
     */
    public static <T> JsonOptional<T> absent() {
        return empty();
    }

    /**
     * Returns an empty {@code JsonOptional} instance.  No value is present for this
     * {@code JsonOptional}. Equivalent to {@link #empty()}.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} against instances returned by
     * {@code JsonOptional.empty()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> The type of the non-existent value
     * @return an absent (undefined) {@code JsonOptional}
     */
    public static <T> JsonOptional<T> undefined() {
        return empty();
    }

    /**
     * Returns a null-holding {@code JsonOptional} instance.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} against instances returned by
     * {@code JsonOptional.nullValue()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isNull()}.
     *
     * @param <T> The type of the non-existent value
     * @return a null-holding {@code JsonOptional}
     */
    public static <T> JsonOptional<T> nullValue() {
        //noinspection unchecked
        return (JsonOptional<T>) NULL;
    }

    /**
     * Constructs an instance with the described value.
     *
     * @param value the value to describe; it's the caller's responsibility to
     *        ensure the value is non-{@code null} unless creating the singleton
     *        instance returned by {@code empty()}.
     */
    private JsonOptional(@Nullable T value) {
        this.value = value;
    }

    /**
     * Returns an {@code JsonOptional} describing the given non-{@code null}
     * value.
     *
     * @param value the value to describe, which must be non-{@code null}
     * @param <T> the type of the value
     * @return an {@code JsonOptional} with the value present
     * @throws NullPointerException if value is {@code null}
     */
    public static <T> JsonOptional<T> of(@NotNull T value) {
        return new JsonOptional<>(Objects.requireNonNull(value));
    }

    /**
     * Returns an {@code JsonOptional} describing the given value, if
     * non-{@code null}, otherwise returns a null-holding {@code JsonOptional}.
     *
     * @param value the possibly-{@code null} value to describe
     * @param <T> the type of the value
     * @return an {@code JsonOptional} with a present value if the specified value
     *         is non-{@code null}, otherwise an empty {@code JsonOptional}
     */
    public static <T> JsonOptional<T> ofNullable(@Nullable T value) {
        return value != null ? new JsonOptional<>(value) : nullValue();
    }

    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @apiNote
     * The preferred alternative to this method is {@link #orElseThrow()}.
     *
     * @return the non-{@code null} value described by this {@code JsonOptional}
     * @throws NoSuchElementException if no value is present
     */
    public @Nullable T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value == NULL_VALUE ? null : value;
    }

    /**
     * If a value is present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * If this {@code JsonOptional} is holding a null value, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if this {@code JsonOptional} holds a null value, otherwise {@code false}
     */
    public boolean isNull() {
        return value == NULL_VALUE;
    }

    /**
     * If this {@code JsonOptional} is holding an undefined value, returns {@code true}, otherwise {@code false}.
     * Equivalent to {@link #isEmpty()}
     *
     * @return {@code true} if value is null (representing undefined), otherwise {@code false}
     */
    public boolean isUndefined() {
        return value == null;
    }

    /**
     * If a value is  not present, returns {@code true}, otherwise
     * {@code false}.
     *
     * @return  {@code true} if a value is not present, otherwise {@code false}
     * @since   11
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param action the action to be performed, if a value is present
     * @throws NullPointerException if value is present and the given action is
     *         {@code null}
     */
    public void ifPresent(@NotNull Consumer<? super T> action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the given empty-based action.
     *
     * @param action the action to be performed, if a value is present
     * @param emptyAction the empty-based action to be performed, if no value is
     *        present
     * @throws NullPointerException if a value is present and the given action
     *         is {@code null}, or no value is present and the given empty-based
     *         action is {@code null}.
     * @since 9
     */
    public void ifPresentOrElse(@NotNull Consumer<? super T> action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    /**
     * If a value is present, and the value matches the given predicate,
     * returns an {@code JsonOptional} describing the value, otherwise returns an
     * empty {@code JsonOptional}.
     *
     * @param predicate the predicate to apply to a value, if present
     * @return an {@code JsonOptional} describing the value of this
     *         {@code JsonOptional}, if a value is present and the value matches the
     *         given predicate, otherwise an empty {@code JsonOptional}
     * @throws NullPointerException if the predicate is {@code null}
     */
    public JsonOptional<T> filter(@NotNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(value) ? this : absent();
        }
    }

    /**
     * Like {@link #filter}, except will return a null JsonOptional if {@code mapper} returns null.
     * @param predicate the predicate to apply to a value, if present
     * @return an {@code JsonOptional} describing the value of this
     *         {@code JsonOptional}, if a value is present and the value matches the
     *         given predicate, otherwise a null {@code JsonOptional}
     * @throws NullPointerException if the predicate is {@code null}
     */
    public JsonOptional<T> filterToNull(@NotNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(value) ? this : nullValue();
        }
    }

    /**
     * If a value is present, returns an {@code JsonOptional} describing
     * the result of applying the given mapping function to
     * the value, otherwise returns an empty {@code JsonOptional}.
     *
     * <p>If the mapping function returns a {@code null} result then this method
     * returns an empty {@code JsonOptional}.
     *
     * @apiNote
     * This method supports post-processing on {@code JsonOptional} values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of URIs, selects one that has not
     * yet been processed, and creates a path from that URI, returning
     * an {@code JsonOptional<Path>}:
     *
     * <pre>{@code
     *     JsonOptional<Path> p =
     *         uris.stream().filter(uri -> !isProcessedYet(uri))
     *                       .findFirst()
     *                       .map(Paths::get);
     * }</pre>
     *
     * Here, {@code findFirst} returns an {@code JsonOptional<URI>}, and then
     * {@code map} returns an {@code JsonOptional<Path>} for the desired
     * URI if one exists.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code JsonOptional} describing the result of applying a mapping
     *         function to the value of this {@code JsonOptional}, if a value is
     *         present, otherwise an empty {@code JsonOptional}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    public <U> JsonOptional<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            U result = mapper.apply(value);
            return result != null ? JsonOptional.of(result) : JsonOptional.empty();
        }
    }

    /**
     * Like {@link #map}, except will return a null JsonOptional if {@code mapper} returns null.
     * @param mapper  the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code JsonOptional} describing the result of applying a mapping
     *         function to the value of this {@code JsonOptional}, if a value is
     *         present, otherwise a null-holding {@code JsonOptional}
     * @throws NullPointerException if the predicate is {@code null}
     *
     */
    public <U> JsonOptional<U> mapToNull(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return JsonOptional.ofNullable(mapper.apply(value));
        }
    }

    /**
     * If a value is present, returns the result of applying the given
     * {@code JsonOptional}-bearing mapping function to the value, otherwise returns
     * an empty {@code JsonOptional}.
     *
     * <p>This method is similar to {@link #map(Function)}, but the mapping
     * function is one whose result is already an {@code JsonOptional}, and if
     * invoked, {@code flatMap} does not wrap it within an additional
     * {@code JsonOptional}.
     *
     * @param <U> The type of value of the {@code JsonOptional} returned by the
     *            mapping function
     * @param mapper the mapping function to apply to a value, if present
     * @return the result of applying an {@code JsonOptional}-bearing mapping
     *         function to the value of this {@code JsonOptional}, if a value is
     *         present, otherwise an empty {@code JsonOptional}
     * @throws NullPointerException if the mapping function is {@code null} or
     *         returns a {@code null} result
     */
    public <U> JsonOptional<U> flatMap(@NotNull Function<? super T, ? extends JsonOptional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            @SuppressWarnings("unchecked")
            JsonOptional<U> r = (JsonOptional<U>) mapper.apply(value);
            return Objects.requireNonNull(r);
        }
    }

    /**
     * If a value is present, returns an {@code JsonOptional} describing the value,
     * otherwise returns an {@code JsonOptional} produced by the supplying function.
     *
     * @param supplier the supplying function that produces an {@code JsonOptional}
     *        to be returned
     * @return returns an {@code JsonOptional} describing the value of this
     *         {@code JsonOptional}, if a value is present, otherwise an
     *         {@code JsonOptional} produced by the supplying function.
     * @throws NullPointerException if the supplying function is {@code null} or
     *         produces a {@code null} result
     * @since 9
     */
    public JsonOptional<T> or(Supplier<? extends JsonOptional<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            JsonOptional<T> r = (JsonOptional<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    /**
     * If a value is present, returns a sequential {@link Stream} containing
     * only that value, otherwise returns an empty {@code Stream}.
     *
     * @apiNote
     * This method can be used to transform a {@code Stream} of optional
     * elements to a {@code Stream} of present value elements:
     * <pre>{@code
     *     Stream<JsonOptional<T>> os = ..
     *     Stream<T> s = os.flatMap(JsonOptional::stream)
     * }</pre>
     *
     * @return the optional value as a {@code Stream}
     * @since 9
     */
    public Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    /**
     * If a value is present, returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if no value is present.
     *        May be {@code null}.
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     * If a value is present, returns the value, otherwise returns the result
     * produced by the supplying function.
     *
     * @param supplier the supplying function that produces a value to be returned
     * @return the value, if present, otherwise the result produced by the
     *         supplying function
     * @throws NullPointerException if no value is present and the supplying
     *         function is {@code null}
     */
    public T orElseGet(Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @return the non-{@code null} value described by this {@code JsonOptional}
     * @throws NoSuchElementException if no value is present
     * @since 10
     */
    public T orElseThrow() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * If a value is present, returns the value, otherwise throws an exception
     * produced by the exception supplying function.
     *
     * @apiNote
     * A method reference to the exception constructor with an empty argument
     * list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier the supplying function that produces an
     *        exception to be thrown
     * @return the value, if present
     * @throws X if no value is present
     * @throws NullPointerException if no value is present and the exception
     *          supplying function is {@code null}
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this {@code JsonOptional}.
     * The other object is considered equal if:
     * <ul>
     * <li>it is also an {@code JsonOptional} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {@code true} if the other object is "equal to" this object
     *         otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof JsonOptional)) {
            return false;
        }

        JsonOptional<?> other = (JsonOptional<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * Returns the hash code of the value, if present, otherwise {@code 0}
     * (zero) if no value is present.
     *
     * @return hash code value of the present value or {@code 0} if no value is
     *         present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Returns a non-empty string representation of this {@code JsonOptional}
     * suitable for debugging.  The exact presentation format is unspecified and
     * may vary between implementations and versions.
     *
     * @implSpec
     * If a value is present the result must include its string representation
     * in the result.  Empty and present {@code JsonOptional}s must be unambiguously
     * differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        if (value == null) {
            return "JsonOptional.undefined";
        } else if (value == NULL_VALUE) {
            return "JsonOptional.null";
        } else {
            return String.format("JsonOptional[%s]", value);
        }
    }
}
