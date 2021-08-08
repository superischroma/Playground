package me.superischroma.playground.event.instance;

public class ActionOptions
{
    private boolean once;

    public ActionOptions(boolean once)
    {
        this.once = once;
    }

    public boolean isOnce()
    {
        return once;
    }

    public ActionOptions once(boolean once)
    {
        this.once = once;
        return this;
    }

    public static ActionOptions build()
    {
        return new ActionOptions(false);
    }
}