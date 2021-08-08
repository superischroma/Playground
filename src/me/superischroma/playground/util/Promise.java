package me.superischroma.playground.util;

import me.superischroma.playground.event.instance.InstanceEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A simplified CompletableFuture in the style of ES6's Promise
 * @param <T> Type of the result of the Promise
 */
public class Promise<T> extends InstanceEvent
{
    private Status status;
    private final CompletableFuture<T> future;

    /**
     * Creates a Promise that will produce a result
     * @param producer What to run in order to produce the result
     */
    public Promise(Supplier<T> producer)
    {
        this.status = Status.INCOMPLETE;
        this.future = CompletableFuture.supplyAsync(() ->
        {
            T result = producer.get();
            this.status = Status.RESOLVED;
            this.emit("finish", result);
            this.emit("end", status());
            return result;
        });
        this.future.exceptionally(throwable ->
        {
            this.status = Status.REJECTED;
            return null;
        });
    }

    /**
     * Creates a Promise that will simply run whatever is provided to it
     * @param toRun What to run
     */
    public Promise(Runnable toRun)
    {
        this.status = Status.INCOMPLETE;
        this.future = (CompletableFuture<T>) CompletableFuture.runAsync(() ->
        {
            toRun.run();
            this.status = Status.RESOLVED;
            this.emit("finish");
            this.emit("end", status());
        });
        this.future.exceptionally(throwable ->
        {
            this.status = Status.REJECTED;
            return null;
        });
    }

    /**
     * Completes the Promise immediately with the value provided
     * @param value Value to complete the Promise with
     * @return This Promise for chaining method calls
     */
    public Promise<T> resolve(T value)
    {
        this.future.complete(value);
        this.status = Status.RESOLVED;
        this.emit("finish", value);
        this.emit("end", status());
        return this;
    }

    /**
     * Immediately cancels the Promise and any unfinished asynchronous work
     */
    public void cancel()
    {
        this.future.cancel(true);
        this.status = Status.CANCELLED;
        this.emit("cancel");
        this.emit("end", status());
    }

    /**
     * @return The current status of the promise
     */
    public Status status()
    {
        return status;
    }

    /**
     * Rejects the Promise immediately with the cause provided
     * @param cause The cause of the rejection of this Promise
     * @return This Promise for chaining method calls
     */
    public Promise<T> reject(Throwable cause)
    {
        this.future.completeExceptionally(cause);
        this.status = Status.REJECTED;
        this.emit("reject", cause);
        this.emit("end", status());
        return this;
    }

    /**
     * Halts the current thread until the Promise is resolved or rejected
     * @return The result of the Promise (null if rejected)
     */
    public T await()
    {
        try
        {
            T result = this.future.get();
            this.status = Status.RESOLVED;
            this.emit("finish", result);
            this.emit("end", status());
            return result;
        }
        catch (InterruptedException | ExecutionException ex)
        {
            throw new RuntimeException(ex.getCause());
        }
    }

    /**
     * Appends actions to do once the Promise is resolved
     * @param action What to run once the Promise is resolved
     * @return This Promise for chaining method calls
     */
    public Promise<T> then(Consumer<? super T> action)
    {
        this.future.thenAccept(action);
        return this;
    }

    /**
     * Appends actions to do if the Promise is rejected
     * @param action What to run if the Promise is rejected
     * @return This Promise for chaining method calls
     */
    public Promise<T> cat(Consumer<Throwable> action)
    {
        this.future.exceptionally(throwable ->
        {
            action.accept(throwable);
            return null;
        });
        return this;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("Promise [").append(status.name().toLowerCase()).append("]");
        if (status == Status.RESOLVED)
            builder.append(" ").append(await());
        return builder.toString();
    }

    public enum Status
    {
        INCOMPLETE,
        RESOLVED,
        REJECTED,
        CANCELLED
    }
}
