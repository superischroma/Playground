package me.superischroma.playground.util;

import java.util.concurrent.CompletableFuture;

public class Range<T extends Number> implements Cloneable
{
    private T min;
    private T max;

    public Range(T min, T max)
    {
        this.min = min;
        this.max = max;
    }

    public T getMinimumValue()
    {
        return min;
    }

    public T getMaximumValue()
    {
        return max;
    }

    public Range<T> setMinimumValue(T min)
    {
        this.min = min;
        return this;
    }

    public Range<T> setMaximumValue(T max)
    {
        this.max = max;
        return this;
    }

    @Override
    public Range<T> clone()
    {
        return new Range<>(min, max);
    }

    public int within()
    {
        if (min instanceof Integer)
            return max.intValue() - min.intValue() + 1;
        throw new IllegalStateException("This method is only available for Ranges of type Integer");
    }

    public CompletableFuture<int[]> getValues(boolean inclusive)
    {
        if (!(min instanceof Integer))
            throw new IllegalStateException("This method is only available for Ranges of type Integer");
        return CompletableFuture.supplyAsync(() ->
        {
            int imin = min.intValue(), imax = max.intValue();
            int[] is = new int[imax - imin + 1];
            for (int i = (inclusive ? 0 : 1) + Math.min(imin, imax), j = 0;
                 i <= Math.max(imin, imax) - (inclusive ? 0 : 1); i++, j++)
            {
                is[j] = i;
            }
            return is;
        });
    }

    public CompletableFuture<int[]> getValues()
    {
        return getValues(true);
    }

    public int getRandomInteger()
    {
        return PlaygroundUtils.random(min.intValue(), max.intValue());
    }

    public double getRandomDouble()
    {
        return PlaygroundUtils.random(min.doubleValue(), max.doubleValue());
    }
}