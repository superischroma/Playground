package me.superischroma.playground.school.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Consumer;

// NOTE: i can assure all methods that are REQUIRED function properly. i have not tested some of my own.
public class MiniArrayList<E> implements MiniList<E>, Cloneable, Iterable<E>
{
    private Object[] elements;
    private int size;

    public MiniArrayList(int capacity)
    {
        this.elements = new Object[capacity];
        this.size = 0;
    }

    public MiniArrayList()
    {
        this(10);
    }

    public MiniArrayList(Collection<? extends E> collection)
    {
        this.elements = collection.toArray();
        this.size = elements.length;
    }

    @Override
    public void add(E element)
    {
        elements[size] = element;
        size++;
        checkUpscale();
    }

    @Override
    public void add(int index, E element)
    {
        checkBounds(index);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
        checkUpscale();
    }

    @Override
    public E remove(int index)
    {
        checkBounds(index);
        E prev = (E) elements[index];
        if (size - 1 - index >= 0) System.arraycopy(elements, index + 1, elements, index, size - 1 - index);
        elements[size - 1] = null;
        size--;
        checkDownscale();
        return prev;
    }

    @Override
    public boolean remove(E equivalent)
    {
        int index = indexOf(equivalent);
        if (index == -1)
            return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element)
    {
        checkBounds(index);
        E prev = (E) elements[index];
        elements[index] = element;
        return prev;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public E get(int index)
    {
        checkBounds(index);
        return (E) elements[index];
    }

    @Override
    public boolean contains(E equivalent)
    {
        return indexOf(equivalent) >= 0;
    }

    public ListIterator<E> listIterator()
    {
        return new ListIterator<>()
        {
            final MiniArrayList<E> copy = MiniArrayList.this.clone();
            int position = -1;

            @Override
            public boolean hasNext()
            {
                return position + 1 < copy.size;
            }

            @Override
            public E next()
            {
                return copy.get(++position);
            }

            @Override
            public boolean hasPrevious()
            {
                return position != 0;
            }

            @Override
            public E previous()
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
                MiniArrayList.this.remove(position);
                copy.remove(position);
                position--;
            }

            @Override
            public void set(E e)
            {
                MiniArrayList.this.set(position, e);
                copy.set(position, e);
            }

            @Override
            public void add(E e)
            {
                MiniArrayList.this.add(position, e);
                copy.add(position, e);
            }
        };
    }

    @Override
    public String toString()
    {
        return toLenString(size);
    }

    public String toRawString()
    {
        return toLenString(elements.length);
    }

    @Override
    public MiniArrayList<E> clone()
    {
        MiniArrayList<E> n = new MiniArrayList<>(elements.length);
        System.arraycopy(elements, 0, n.elements, 0, elements.length);
        n.size = size;
        return n;
    }

    @Override
    public Iterator<E> iterator()
    {
        return listIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action)
    {
        for (int i = 0; i < size; i++)
            action.accept((E) elements[i]);
    }

    private String toLenString(int len)
    {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < len; i++)
            builder.append(i != 0 ? ", " : "").append(elements[i] != null ? elements[i].toString() : null);
        builder.append("]");
        return builder.toString();
    }

    private int indexOf(E equivalent)
    {
        if (equivalent == null)
        {
            for (int i = 0; i < size; i++)
            {
                if (elements[i] == null)
                    return i;
            }
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                if (elements[i] == null)
                    continue;
                if (equivalent.equals(elements[i]))
                    return i;
            }
        }
        return -1;
    }

    private void checkBounds(int index)
    {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException(index);
    }

    private void checkUpscale()
    {
        if (size == elements.length)
            this.elements = copyOf(this.elements, elements.length * 2);
    }

    private void checkDownscale()
    {
        if (size <= elements.length / 3)
            this.elements = copyOf(this.elements, elements.length / 2);
    }

    private static <T> T[] copyOf(T[] array, int length)
    {
        T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, 0, copy, 0, Math.min(length, array.length));
        return copy;
    }
}