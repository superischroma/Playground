package me.superischroma.playground.event;

public class CommandSendEvent extends Event implements Cancellable
{
    private String command;
    private boolean cancelled;

    public CommandSendEvent(String command)
    {
        this.command = command;
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

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }
}