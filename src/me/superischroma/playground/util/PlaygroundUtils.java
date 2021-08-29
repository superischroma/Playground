package me.superischroma.playground.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class PlaygroundUtils
{
    public static char[] ALPHANUMERIC = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static char[] NUMERIC = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static char[] ALPHA_LOWER = new char[]{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static char[] ALPHA_UPPER = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static char[] ALPHANUMERIC_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static char[] ALPHANUMERIC_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

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

    public static <T> T getRandom(List<T> list)
    {
        if (list.isEmpty()) return null;
        return list.get(random(0, list.size() - 1));
    }

    public static <T> int removeIf(List<T> list, Predicate<? super T> condition, int limit)
    {
        int i = 0;
        for (Iterator<T> iter = list.iterator(); iter.hasNext();)
        {
            if (limit <= i && limit > 0)
                break;
            T next = iter.next();
            if (condition.test(next))
            {
                iter.remove();
                i++;
            }
        }
        return i;
    }

    public static <T> int removeIf(List<T> list, Predicate<? super T> condition)
    {
        return removeIf(list, condition, 0);
    }

    public static boolean writeImage(Image src, File dest, String format) throws IOException
    {
        BufferedImage bi = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        if (!dest.exists())
            dest.createNewFile();
        return ImageIO.write(bi, format, dest);
    }

    public static boolean writeImage(Image src, File dest) throws IOException
    {
        return writeImage(src, dest, "png");
    }

    public static String randomString(int length, char[] characters)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(characters[(int) Math.floor(Math.random() * characters.length)]);
        return builder.toString();
    }
}