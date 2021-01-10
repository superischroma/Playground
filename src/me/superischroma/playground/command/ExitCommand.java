package me.superischroma.playground.command;

@Command(description = "exit the program", aliases = "leave, ex, escape")
public class ExitCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        send("adios");
        System.exit(0);
    }
}