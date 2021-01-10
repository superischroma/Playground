package me.superischroma.playground.util;

public class DelayedRunnable
{
    private final Runnable runnable;
    private long delay;

    /**
     * Creates a new Delayed Runnable.
     * @param runnable The base runnable.
     */
    public DelayedRunnable(Runnable runnable)
    {
        this.runnable = runnable;
    }

    /**
     * Creates a new Delayed Runnable.
     * @param runnable The base runnable.
     * @param delay How long the delay is if no delay is provided when it runs. (in milliseconds)
     */
    public DelayedRunnable(Runnable runnable, long delay)
    {
        this.runnable = runnable;
        this.delay = delay;
    }

    public void run(long delay)
    {
        new Thread(() ->
        {
            try
            {
                Thread.sleep(delay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            runnable.run();
        }).start();
    }

    public void run()
    {
        run(delay);
    }
}