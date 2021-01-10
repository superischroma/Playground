package me.superischroma.playground.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class PlaygroundUtils
{
    public static <T> T instance(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (IllegalAccessException | InstantiationException ex)
        {
            return null;
        }
    }

    public static int random(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double random(double min, double max)
    {
        return min + (ThreadLocalRandom.current().nextDouble() * (max - min));
    }

    public static <T> void addIfNotNull(List<T> list, T t)
    {
        if (t != null)
            list.add(t);
    }

    public static <T> Collection<T> asCollection(List<T> list)
    {
        return list;
    }
}