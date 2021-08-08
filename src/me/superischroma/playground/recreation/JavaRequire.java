package me.superischroma.playground.recreation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JavaRequire<T>
{
    private static final Map<String, JavaRequire<?>> CACHE = new HashMap<>();

    private final Class<T> clazz;
    private T instance;

    protected JavaRequire(Class<T> clazz, T instance)
    {
        this.clazz = clazz;
        this.instance = instance;
    }

    protected JavaRequire(Class<T> clazz)
    {
        this(clazz, null);
    }

    public JavaRequire<T> instance(T instance)
    {
        this.instance = instance;
        return this;
    }

    public Object var(String name)
    {
        try
        {
            Field field = clazz.getField(name);
            field.setAccessible(true);
            return field.get(this.instance);
        }
        catch (IllegalAccessException | NoSuchFieldException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public Object method(String name, Object... args)
    {
        try
        {
            Class<?>[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++)
                parameterTypes[i] = args[i].getClass();
            Method method = clazz.getMethod(name, parameterTypes);
            method.setAccessible(true);
            return method.invoke(this.instance, args);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static <T> JavaRequire<T> require(Class<T> clazz)
    {
        return new JavaRequire<>(clazz);
    }
}