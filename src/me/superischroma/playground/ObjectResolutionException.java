package me.superischroma.playground;

public class ObjectResolutionException extends RuntimeException
{
    public ObjectResolutionException()
    {
        super();
    }

    public ObjectResolutionException(Class<?> from, Class<?> to)
    {
        super(to.getName() + " could not be resolved from " + from.getName());
    }

    public ObjectResolutionException(String message)
    {
        super(message);
    }
}