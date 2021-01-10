package me.superischroma.playground.command;

import java.util.Scanner;

public abstract class AbstractCommand
{
    /**
     * A standard scanner for System.in to be used to gather input in commands.
     */
    protected final Scanner scanner = new Scanner(System.in);

    /**
     * Runs the command this interface was implemented in.
     * @param args Arguments appended to the command.
     */
    public abstract void run(String[] args) throws Exception;

    /**
     * Uses System.out to send a message.
     * @param message The message to be sent.
     */
    public void send(String message)
    {
        Messenger.send(message);
    }

    /**
     * Uses System.out to send a message with no newline following it.
     * @param message The message to be sent.
     */
    public void sendnl(String message)
    {
        Messenger.sendnl(message);
    }

    /**
     * Gets the Command for this ICommand.
     * @return The Command for this ICommand.
     */
    public Command getCommand()
    {
        return this.getClass().getAnnotation(Command.class);
    }

    /**
     * Gets the name of this command.
     * @return The name of this command.
     */
    public String getName()
    {
        String name = getCommand().name();
        if (name.equals(""))
            return this.getClass().getSimpleName().toLowerCase().replaceFirst("command", "");
        return name;
    }
}