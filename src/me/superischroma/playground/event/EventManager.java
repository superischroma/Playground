package me.superischroma.playground.event;

import me.superischroma.playground.util.PlaygroundUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
                    .sorted((m1, m2) ->
                    {
                        EventHandler h1 = m1.getAnnotation(EventHandler.class);
                        EventHandler h2 = m2.getAnnotation(EventHandler.class);
                        return h1.priority().ordinal() - h2.priority().ordinal();
                    }).collect(Collectors.toList());
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