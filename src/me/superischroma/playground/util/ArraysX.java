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

    public static void insertionSort(int[] elements)
    {
        int i = 0;
        for (int j = 1; j < elements.length; j++)
        {
            int temp = elements[j];
            int possibleIndex = j;
            while (possibleIndex > 0 && temp < elements[possibleIndex - 1])
            {
                elements[possibleIndex] = elements[possibleIndex - 1];
                possibleIndex--;    // Line 10
                i++;
            }
            elements[possibleIndex] = temp;
        }
        System.out.println(i);
    }

    public static void selectionSort(int[] elements)
    {
        int b = 0;
        for (int j = 0; j < elements.length - 1; j++)
        {
            int minIndex = j;
            for (int k = j + 1; k < elements.length; k++)
            {
                if (elements[k] < elements[minIndex])
                {
                    minIndex = k;
                    b++;
                }
            }
            if (j != minIndex)
            {
                int temp = elements[j];
                elements[j] = elements[minIndex];
                elements[minIndex] = temp;    // Line 19
            }
        }
        System.out.println(b);
    }
}