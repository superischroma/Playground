package me.superischroma.playground.item;

import java.lang.reflect.InvocationTargetException;

public enum Material
{
    STICK(Stick.class),
    ;

    private final Class<? extends Item> clazz;

    Material(Class<? extends Item> clazz)
    {
        this.clazz = clazz;
    }

    public static Material getByID(int id)
    {
        if (id < 0 || id >= values().length)
            return null;
        return values()[id];
    }

    public Item getItem()
    {
        try
        {
            return clazz.getDeclaredConstructor().newInstance();
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ignored) {}
        throw new NullPointerException();
    }
}