package me.superischroma.playground.event.instance;

import me.superischroma.playground.recreation.Array;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InstanceEvent
{
    private final Map<String, Array<Action>> actions;

    protected InstanceEvent()
    {
        this.actions = new HashMap<>();
    }

    public void emit(String identifier, Object... args)
    {
        if (!actions.containsKey(identifier))
            return;
        Array<Action> as = actions.get(identifier);
        for (Action a : as)
            a.action.accept(args);
        as.removeIf(a -> a.options.isOnce());
    }

    public void on(String identifier, Consumer<Object[]> action, ActionOptions options)
    {
        Action a = new Action(action, options);
        if (this.actions.containsKey(identifier))
            this.actions.get(identifier).push(a);
        else
            this.actions.put(identifier, Array.of(a));
    }

    public void on(String identifier, Consumer<Object[]> action)
    {
        on(identifier, action, ActionOptions.build());
    }

    private static class Action
    {
        private final Consumer<Object[]> action;
        private final ActionOptions options;

        public Action(Consumer<Object[]> action, ActionOptions options)
        {
            this.action = action;
            this.options = options;
        }
    }
}
