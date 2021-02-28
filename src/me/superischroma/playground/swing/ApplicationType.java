package me.superischroma.playground.swing;

import java.lang.reflect.InvocationTargetException;

public enum ApplicationType
{
    SSD_EDITOR(SSDEditorApplication.class);

    private final Class<? extends Application> clazz;

    ApplicationType(Class<? extends Application> clazz)
    {
        this.clazz = clazz;
    }

    public void open()
    {
        try
        {
            clazz.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }
}