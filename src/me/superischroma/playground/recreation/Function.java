package me.superischroma.playground.recreation;

public class Function<R>
{
    private final java.util.function.Function<Object[], R> c;

    public Function(java.util.function.Function<Object[], R> c)
    {
        this.c = c;
    }

    public R call(Object... params)
    {
        return c.apply(params);
    }
}