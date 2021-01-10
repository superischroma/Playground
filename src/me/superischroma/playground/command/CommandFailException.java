package me.superischroma.playground.command;

public class CommandFailException extends RuntimeException
{
    public CommandFailException(String message)
    {
        super(message);
    }
}