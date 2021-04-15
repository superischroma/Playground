package me.superischroma.playground.recreation;

import java.util.StringJoiner;

/**
 * Represents a queue that can only be scrolled forward.
 * @param <T>
 */
public class LinearQueue<T> implements Queue<T>
{
    private Object[] items;
    private int size;

    @SafeVarargs
    public LinearQueue(T... items)
    {
        this.items = new Object[Math.max(10, items.length)];
        System.arraycopy(items, 0, this.items, 0, items.length);
        this.size = items.length;
    }

    @Override
    public boolean scroll()
    {
        return skip(1);
    }

    @Override
    public boolean skip(int amount)
    {
        if (amount >= size)
            return false;
        Object[] repl = new Object[size - amount];
        System.arraycopy(items, amount, repl, 0, size - amount);
        this.items = repl;
        size -= amount;
        return true;
    }

    @Override
    public T get()
    {
        if (size == 0)
            throw new IndexOutOfBoundsException(0);
        return (T) items[0];
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
        StringJoiner joiner = new StringJoiner(", ", "LinearQueue{", "}");
        for (int i = 0; i < size; i++)
            joiner.add((i == 0 ? "[" : "") + (items[i] != null ? items[i].toString() : null) + (i == 0 ? "]" : ""));
        return joiner.toString();
    }

    private void checkResize(int additionSize)
    {
        if (size + additionSize > items.length)
            System.arraycopy(items, 0, items, 0, size + additionSize);
    }
}