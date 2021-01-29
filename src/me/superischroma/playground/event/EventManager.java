package me.superischroma.playground.event;

import me.superischroma.playground.util.PlaygroundUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public final class EventManager
{
    private static final List<Listener> LISTENERS = new ArrayList<>();
    public static List<Listener> getRegisteredListeners()
    {
        return LISTENERS;
    }

    public static void registerListener(Listener listener)
    {
        LISTENERS.add(listener);
    }

    public static void registerListener(Class<? extends Listener> listener)
    {
        PlaygroundUtils.addIfNotNull(LISTENERS, PlaygroundUtils.instance(listener));
    }

    public static void registerListeners(Listener... listeners)
    {
        LISTENERS.addAll(Arrays.asList(listeners));
    }

    @SafeVarargs
    public static void registerListeners(Class<? extends Listener>... listeners)
    {
        for (Class<? extends Listener> listener : listeners)
            PlaygroundUtils.addIfNotNull(LISTENERS, PlaygroundUtils.instance(listener));
    }

    public static void call(Event e)
    {
        for (Listener listener : LISTENERS)
        {
            List<Method> methods = Arrays.stream(listener.getClass().getMethods())
                    .filter(method ->
                    {
                        if (method.getAnnotation(EventHandler.class) == null)
                            return false;
                        List<Class<?>> params = Arrays.asList(method.getParameterTypes());
                        if (params.size() != 1)
                            return false;
                        return params.contains(e.getClass());
                    })
                    .sorted(Comparator.comparingInt(m ->
                            m.getAnnotation(EventHandler.class).priority().ordinal()))
                            .collect(Collectors.toList());
            Collections.reverse(methods);
            for (Method method : methods)
            {
                try
                {
                    method.invoke(listener, e);
                }
                catch (IllegalAccessException | InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}