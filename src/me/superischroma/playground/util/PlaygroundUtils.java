package me.superischroma.playground.util;

import me.superischroma.playground.http.HTTP;

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
}