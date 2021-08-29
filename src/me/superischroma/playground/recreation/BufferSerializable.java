package me.superischroma.playground.recreation;

/**
 * Defines a class that can serialize into
 * binary data and deserialize back into
 * an object.
 *
 * Along with the method in this interface,
 * you must provide a static method named {@code #from(Buffer)}
 * and it must return an instance of the class or {@code null}.
 */
public interface BufferSerializable
{
    Buffer buffer();
}