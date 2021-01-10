package me.superischroma.playground.util;

import java.util.Arrays;
import java.util.List;

public final class ArraysX
{
    public static String join(List<?> list, String separator)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
            result.append(i != 0 ? separator : "").append(list.get(i));
        return result.toString();
    }

    public static String join(Object[] list, String separator)
    {
        return join(Arrays.asList(list), separator);
    }
}