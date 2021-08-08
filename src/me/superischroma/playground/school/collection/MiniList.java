package me.superischroma.playground.school.collection;

public interface MiniList<E>
{
    void add(E element);
    void add(int index, E element);
    E remove(int index);
    boolean remove(E equivalent);
    E set(int index, E element);
    int size();
    E get(int index);
    boolean contains(E equivalent);
}