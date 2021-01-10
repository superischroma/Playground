package me.superischroma.playground.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Collector
{
    private final InputStream stream;
    private CollectorState state;
    private Predicate<String> endCondition;
    private List<String> result;
    private Thread collectorThread;

    public Collector(InputStream stream)
    {
        this.stream = stream;
        this.state = CollectorState.IDLE;
        this.result = new ArrayList<>();
    }

    public void start(Predicate<String> endCondition, Runnable onceEnded)
    {
        this.endCondition = endCondition;
        if (this.state == CollectorState.ACTIVE)
            throw new CollectorAlreadyStartedException();
        this.state = CollectorState.ACTIVE;
        this.collectorThread = new Thread(() ->
        {
            Scanner scanner = new Scanner(stream);
            String line;
            do
            {
                line = scanner.nextLine();
                result.add(line);
            }
            while (!endCondition.test(line) && this.state == CollectorState.ACTIVE);
            if (onceEnded != null) onceEnded.run();
            this.state = CollectorState.IDLE;
            this.collectorThread = null;
        });
        this.collectorThread.start();
    }

    public void start(Predicate<String> endCondition)
    {
        start(endCondition, null);
    }

    public CollectorState getState()
    {
        return state;
    }

    public List<String> getResult()
    {
        return result;
    }

    public enum CollectorState
    {
        ACTIVE,
        IDLE
    }

    public static class CollectorAlreadyStartedException extends RuntimeException
    {
        public CollectorAlreadyStartedException()
        {
            super("This collector has already started collecting");
        }
    }
}