package me.superischroma.playground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.superischroma.playground.command.AbstractCommand;
import me.superischroma.playground.command.CommandController;
import me.superischroma.playground.event.EventManager;
import me.superischroma.playground.http.HTTP;
import me.superischroma.playground.item.*;
import me.superischroma.playground.listener.TestListener;
import me.superischroma.playground.recreation.*;
import me.superischroma.playground.reflect.Reflections;
import me.superischroma.playground.storage.MappedTextFile;

import java.util.List;

import static me.superischroma.playground.recreation.JavaRequire.require;

public class Playground
{
    public static JavaRequire<HTTP> http = require(HTTP.class);

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

    public static void main(String[] args) throws Throwable
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

        PriorityQueue<Integer> queue = new PriorityQueue<>(PriorityQueue.GREATEST);
        queue.push(6, 18, 2, 19, 12, 102, 1029);
        System.out.println(queue);
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