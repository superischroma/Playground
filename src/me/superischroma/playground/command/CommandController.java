package me.superischroma.playground.command;

import me.superischroma.playground.event.CommandSendEvent;
import me.superischroma.playground.event.EventManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public final class CommandController
{
    private static boolean QUERYING = false;

    private static final List<AbstractCommand> ATTACHED = new ArrayList<>();

    public static void attach(Class<? extends AbstractCommand> command)
    {
        try
        {
            ATTACHED.add(command.newInstance());
        }
        catch (IllegalAccessException | InstantiationException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void detach(Class<? extends AbstractCommand> command)
    {
        ATTACHED.removeIf(c -> c.getClass().getName().equals(command.getName()));
    }

    public static void detach(String command)
    {
        ATTACHED.removeIf(ac ->
        {
            Command c = ac.getClass().getAnnotation(Command.class);
            if (c == null) return true;
            if (c.name().equals(""))
            {
                return ac.getClass().getSimpleName()
                        .toLowerCase().replaceFirst("command", "").equals(command);
            }
            return c.name().equals(command);
        });
    }

    public static List<AbstractCommand> getAttachedCommands()
    {
        return ATTACHED;
    }

    private static void runCommand(String c, boolean querying)
    {
        if (c.isEmpty())
        {
            if (querying)
                QUERYING = false;
            return;
        }
        CommandSendEvent sendEvent = new CommandSendEvent(c);
        EventManager.call(sendEvent);
        if (sendEvent.isCancelled())
        {
            QUERYING = false;
            return;
        }
        c = sendEvent.getCommand();
        String[] commandArray = c.split(" ");
        List<AbstractCommand> search = ATTACHED.stream().filter(ac ->
        {
            Command command = ac.getClass().getAnnotation(Command.class);
            if (command == null) return false;
            String name = ac.getName();
            for (String alias : command.aliases().split(", "))
            {
                if (commandArray[0].toLowerCase().equals(alias))
                    return true;
            }
            return commandArray[0].toLowerCase().equals(name);
        }).collect(Collectors.toList());
        if (search.size() == 0)
        {
            Messenger.send("unknown command");
            if (querying)
                QUERYING = false;
            return;
        }
        AbstractCommand selected = search.get(0);
        try
        {
            String[] args = new String[commandArray.length - 1];
            System.arraycopy(commandArray, 1, args, 0, commandArray.length - 1);
            selected.run(args);
        }
        catch (CommandFailException | FileNotFoundException ex)
        {
            Messenger.send(ex.getMessage());
        }
        catch (CommandArgumentException ex)
        {
            Messenger.send("incorrect arguments for " + selected.getName() + "! usage: " +
                    selected.getCommand().usage());
        }
        catch (NumberFormatException ex)
        {
            Messenger.send("number expected, but non-number was provided");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void runCommand(String c)
    {
        runCommand(c, false);
    }

    /**
     * Queries {@link System#in} for a command ONCE.
     * This query runs asynchronously.
     * You can find out when the query has been fulfilled by
     * calling {@link CompletableFuture#get()} on the return value of this method.
     * @return The {@link CompletableFuture} of the query that was requested.
     */
    public static CompletableFuture<Void> query()
    {
        if (QUERYING)
            throw new QueryException();
        return CompletableFuture.runAsync(() ->
        {
            QUERYING = true;
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String c = scanner.nextLine();
            runCommand(c, true);
            QUERYING = false;
        });
    }

    /**
     * Queries System.in for commands until the program is terminated.
     * Query loop runs asynchronously, along with every query ran.
     */
    public static void queryLoop()
    {
        new Thread(() ->
        {
            while (true)
            {
                try
                {
                    query().get();
                }
                catch (InterruptedException | ExecutionException ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Runs a function from the desired file.
     * Commands are ran synchronously.
     * @param file The function file.
     * @throws FileNotFoundException
     */
    public static void runFunction(File file) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            runCommand(scanner.nextLine());
    }

    public static class QueryException extends RuntimeException
    {
        public QueryException()
        {
            super("Tried to query while a query was already occurring");
        }
    }
}