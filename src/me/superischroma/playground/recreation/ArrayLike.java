package me.superischroma.playground.recreation;

public interface ArrayLike<T> extends Cloneable, Iterable<T>
{
    /**
     * Sets the value at the specified index and returns the old value
     * @param index The index to override
     * @param value The new value for this index
     * @return The value that was originally in this location
     * @throws ArrayIndexOutOfBoundsException When the index is outside of the range of the array-like object.
     */
    T set(int index, T value);

    /**
     * Retrieves the value stored at the specified index of this array-like object.
     * @param index The index to retrieve from
     * @return The value in that location
     * @throws ArrayIndexOutOfBoundsException When the index is outside of the range of the array-like object.
     */
    T get(int index);

    /**
     * Retrieves the size of this array-like object
     * @return The current size
     */
    int size();

    /**
     * Checks if this array-like object has no elements
     * @return Whether no elements exist
     */
    boolean isEmpty();
}