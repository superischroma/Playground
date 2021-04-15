package me.superischroma.playground.recreation;

import java.util.StringJoiner;

/**
 * Represents a queue that can scroll forward and backward.
 * @param <T>
 */
public class NavigableQueue<T> implements Queue<T>
{
    private Object[] items;
    private int size;
    private int cursor;

    @SafeVarargs
    public NavigableQueue(T... items)
    {
        this.items = new Object[Math.max(10, items.length)];
        System.arraycopy(items, 0, this.items, 0, items.length);
        this.size = items.length;
        this.cursor = 0;
    }

    @Override
    public boolean scroll()
    {
        return skip(1);
    }

    @Override
    public boolean skip(int amount)
    {
        if (cursor + amount >= size)
            return false;
        cursor += amount;
        return true;
    }

    @Override
    public T get()
    {
        if (size == 0)
            throw new IndexOutOfBoundsException(0);
        return (T) items[cursor];
    }

    @Override
    @SafeVarargs
    public final void append(T... items)
    {
        checkResize(items.length);
        System.arraycopy(items, 0, this.items, size, items.length);
        size += items.length;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        StringJoiner joiner = new StringJoiner(", ", "MemoryQueue{", "}");
        for (int i = 0; i < size; i++)
            joiner.add((cursor == i ? "[" : "") + (items[i] != null ? items[i].toString() : null) + (cursor == i ? "]" : ""));
        return joiner.toString();
    }

    /**
     * Scrolls this queue's cursor to the previous item
     * @return Whether the scroll succeeded or failed
     */
    public boolean track()
    {
        return fall(1);
    }

    /**
     * Scrolls this queue's cursor back the amount provided
     * @param amount The amount to scroll back
     * @return Whether the fall could be performed
     */
    public boolean fall(int amount)
    {
        if (cursor - amount < 0)
            return false;
        cursor -= amount;
        return true;
    }

    private void checkResize(int additionSize)
    {
        if (size + additionSize > items.length)
            System.arraycopy(items, 0, items, 0, size + additionSize);
    }
}