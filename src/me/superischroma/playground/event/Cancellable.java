package me.superischroma.playground.event;

/**
 * Marks the implementing event as cancellable.
 */
public interface Cancellable
{
    void setCancelled(boolean cancelled);
    boolean isCancelled();
}