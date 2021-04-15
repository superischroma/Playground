package me.superischroma.playground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.superischroma.playground.command.AbstractCommand;
import me.superischroma.playground.command.CommandController;
import me.superischroma.playground.event.EventManager;
import me.superischroma.playground.item.*;
import me.superischroma.playground.listener.TestListener;
import me.superischroma.playground.recreation.Array;
import me.superischroma.playground.recreation.Prims;
import me.superischroma.playground.recreation.Queue;
import me.superischroma.playground.reflect.Reflections;
import me.superischroma.playground.storage.MappedTextFile;

import java.util.List;

public class Playground
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static MappedTextFile functionCache;
    public static MappedTextFile getFunctionCache()
    {
        return functionCache;
    }

    private static Inventory inventory;
    public static Inventory getInventory()
    {
        return inventory;
    }

    public static void main(String[] args)
    {
        inventory = new Inventory("Inventory", 36);
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
        System.out.println(Prims.itoa(1964869));
    }

    private static void printArray(Array<?> array)
    {
        System.out.println(array.toRawString());
        System.out.println(array.size());
    }

    private static void printQueue(Queue<?> queue)
    {
        System.out.println(queue.toString());
        System.out.println(queue.size());
    }
}