package me.superischroma.playground.command;

import me.superischroma.playground.util.ArraysX;

@Command(description = "say something", usage = "<command> <message>")
public class SayCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        if (args.length == 0)
            throw new CommandFailException("enter something to say");
        send(ArraysX.join(args, " "));
    }
}