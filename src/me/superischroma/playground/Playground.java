package me.superischroma.playground;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.superischroma.playground.command.AbstractCommand;
import me.superischroma.playground.command.CommandController;
import me.superischroma.playground.event.EventManager;
import me.superischroma.playground.http.HTTP;
import me.superischroma.playground.http.RequestMethod;
import me.superischroma.playground.item.*;
import me.superischroma.playground.listener.TestListener;
import me.superischroma.playground.recreation.Array;
import me.superischroma.playground.recreation.JavaRequire;
import me.superischroma.playground.recreation.Queue;
import me.superischroma.playground.reflect.Reflections;
import me.superischroma.playground.storage.MappedTextFile;
import me.superischroma.playground.util.PlaygroundUtils;
import me.superischroma.playground.util.Promise;

import java.io.File;
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

        Promise<String> promise = new Promise<>(() ->
        {
            System.out.println("starting");
            try { Thread.sleep(5000); } catch (Exception ignored) {}
            return "yes";
        });

        promise.then(System.out::println);
        promise.on("end", as ->
        {
            Promise.Status status = (Promise.Status) as[0];
            System.out.println(status.name());
        });
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