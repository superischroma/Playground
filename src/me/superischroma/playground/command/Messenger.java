package me.superischroma.playground.command;

import me.superischroma.playground.event.EventManager;
import me.superischroma.playground.event.MessageReceivedEvent;

public final class Messenger
{
    public static void send(String message)
    {
        MessageReceivedEvent receivedEvent = new MessageReceivedEvent(message);
        EventManager.call(receivedEvent);
        if (receivedEvent.isCancelled())
            return;
        System.out.println(receivedEvent.getMessage());
    }

    public static void sendnl(String message)
    {
        MessageReceivedEvent receivedEvent = new MessageReceivedEvent(message);
        EventManager.call(receivedEvent);
        if (receivedEvent.isCancelled())
            return;
        System.out.print(receivedEvent.getMessage());
    }
}