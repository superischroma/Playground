package me.superischroma.playground.recreation;

/**
 * Represents a scrollable sequence of objects.
 * Implementations of this interface may or may not support backtracking.
 * Once an object has been put in a queue, it cannot be removed.
 * @param <T>
 */
public interface Queue<T>
{
    /**
     * Scrolls this queue's cursor to the next item
     * @return Whether the scroll succeeded or failed
     */
    boolean scroll();

    /**
     * Scrolls this queue's cursor for the amount provided
     * @param amount The amount to scroll
     * @return Whether the skip could be performed
     */
    boolean skip(int amount);

    /**
     * Retrieves the value at the current position
     * @return The value
     */
    T get();

    /**
     * Appends the provided items to the end of the queue
     * @param items Items to be appended
     */
    void append(T... items);

    /**
     * Retrieves the size of this queue
     * @return The size
     */
    int size();
}