package me.superischroma.playground.recreation;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;

public class RecArray<T> implements Iterable<T>
{
    public Object[] array;

    private int size;

    public RecArray(int capacity)
    {
        this.array = new Object[capacity];
    }

    public RecArray()
    {
        this(5);
    }

    public RecArray(T[] array)
    {
        this.array = new Object[array.length];
        for (int i = 0; i < array.length; i++)
            this.array[i] = array[i];
    }

    public RecArray(RecArray<T> copy)
    {
        this.array = copyOf(copy.array, copy.array.length);
        this.size = copy.size;
    }

    @SafeVarargs
    public final void push(T... elements)
    {
        if (size + elements.length > array.length)
            this.array = copyOf(this.array, size + elements.length);
        for (T element : elements)
            array[size++] = element;
    }

    public void concat(RecArray<T> array)
    {
        if (size + array.size > this.array.length)
            this.array = copyOf(this.array, size + array.size);
        for (T element : array)
            this.array[size++] = element;
    }

    public T get(int index)
    {
        checkBounds(index);
        return (T) array[index];
    }

    public T remove(int index)
    {
        checkBounds(index);
        T cache = (T) array[index];
        for (int i = index; i < size - 1; i++)
            array[i] = array[i + 1];
        array[size - 1] = null;
        size--;
        return cache;
    }

    public boolean remove(T equivalent)
    {
        Integer found = null;
        for (int i = 0; i < size; i++)
        {
            if (array[i].equals(equivalent))
                found = i;
            if (found != null)
            {
                if (i + 1 >= size)
                {
                    array[i] = null;
                    continue;
                }
                array[i] = array[i + 1];
            }
        }
        array[array.length - 1] = null;
        if (found != null)
            size--;
        return found != null;
    }

    public int size()
    {
        return size;
    }

    public T set(int index, T value)
    {
        checkBounds(index);
        T current = (T) array[index];
        array[index] = value;
        return current;
    }

    public int removeIf(Predicate<? super T> condition, int limit)
    {
        int removed = 0;
        for (int i = 0; i < size; i++)
        {
            if (condition.test((T) array[i]))
            {
                this.remove(i);
                i--;
                removed++;
            }
            if (limit > 0 && removed == limit)
                break;
        }
        return removed;
    }

    public int removeIf(Predicate<? super T> condition)
    {
        return removeIf(condition, 0);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size; i++)
            builder.append(i != 0 ? ", " : "").append(array[i] != null ? array[i].toString() : null);
        builder.append("]");
        return builder.toString();
    }

    public void checkBounds(int index)
    {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    private static <T> T[] copyOfRange(T[] original, int start, int end)
    {
        T[] copy = (T[]) Array.newInstance(original.getClass().getComponentType(), (end - start) + 1);
        System.arraycopy(original, start, copy, 0, copy.length);
        return copy;
    }

    private static <T> T[] copyOf(T[] original, int length)
    {
        T[] copy = (T[]) Array.newInstance(original.getClass().getComponentType(), length);
        System.arraycopy(original, 0, copy, 0, length);
        return copy;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new ListIterator<>()
        {
            RecArray<T> copy = new RecArray<>(RecArray.this);
            int position = 0;

            @Override
            public boolean hasNext()
            {
                return position < copy.size;
            }

            @Override
            public T next()
            {
                return copy.get(++position);
            }

            @Override
            public boolean hasPrevious()
            {
                return position != 0;
            }

            @Override
            public T previous()
            {
                return copy.get(--position);
            }

            @Override
            public int nextIndex()
            {
                return position + 1;
            }

            @Override
            public int previousIndex()
            {
                return position - 1;
            }

            @Override
            public void remove()
            {
                RecArray.this.remove(position);
                copy.remove(position);
            }

            @Override
            public void set(T t)
            {
                RecArray.this.set(position, t);
                copy.set(position, t);
            }

            @Override
            public void add(T t)
            {
                push(t);
            }
        };
    }
}