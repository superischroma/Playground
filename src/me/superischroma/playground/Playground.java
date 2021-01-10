package me.superischroma.playground;

import me.superischroma.playground.command.*;
import me.superischroma.playground.event.EventManager;
import me.superischroma.playground.listener.TestListener;
import me.superischroma.playground.reflect.Reflections;
import me.superischroma.playground.storage.MappedTextFile;

import java.util.List;

public class Playground
{
    private static MappedTextFile functionCache;
    public static MappedTextFile getFunctionCache()
    {
        return functionCache;
    }

    public static void main(String[] args)
    {
        functionCache = new MappedTextFile("./functions/cache.txt");
        List<Class<? extends AbstractCommand>> commands =
                Reflections.getSubclassesInPackage("me.superischroma.playground.command",
                        AbstractCommand.class);
        EventManager.registerListener(TestListener.class);
        if (commands != null)
        {
            for (Class<? extends AbstractCommand> command : commands)
                CommandController.attach(command);
            CommandController.queryLoop();
        }
    }
}