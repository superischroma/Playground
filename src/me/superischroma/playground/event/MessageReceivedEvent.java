package me.superischroma.playground.event;

public class MessageReceivedEvent extends Event implements Cancellable
{
    private String message;
    private boolean cancelled;

    public MessageReceivedEvent(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }
}