package me.superischroma.playground.recreation;

import java.util.Iterator;
import java.util.function.Consumer;

public class BiIterator<E>
{
    private final Iterator<E> first;
    private final Iterator<E> second;

    public BiIterator(Iterator<E> first, Iterator<E> second)
    {
        this.first = first;
        this.second = second;
    }

    public DoubleVal<E> next()
    {
        return new DoubleVal<>(first.next(), second.next());
    }

    public boolean hasNext()
    {
        return first.hasNext() && second.hasNext();
    }

    public void remove()
    {
        first.remove();
        second.remove();
    }

    public void removeFirst()
    {
        first.remove();
    }

    public void removeSecond()
    {
        second.remove();
    }

    public void forEachRemaining(Consumer<? super E> action)
    {
        first.forEachRemaining(action);
        second.forEachRemaining(action);
    }

    private static class DoubleVal<T>
    {
        private final T first;
        private final T second;

        private DoubleVal(T first, T second)
        {
            this.first = first;
            this.second = second;
        }

        public T first()
        {
            return first;
        }

        public T second()
        {
            return second;
        }
    }
}