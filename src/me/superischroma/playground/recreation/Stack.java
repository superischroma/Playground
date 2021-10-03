package me.superischroma.playground.recreation;

public class Stack<T>
{
    private Node<T> top;

    public Stack()
    {
        this.top = null;
    }

    @SafeVarargs
    public Stack(T... elements)
    {
        push(elements);
    }

    @SafeVarargs
    public final void push(T... elements)
    {
        for (T element : elements)
            push(element);
    }

    public void push(T element)
    {
        top = new Node<>(top, element);
    }

    public T pop()
    {
        if (top == null)
            return null;
        T value = top.value;
        top = top.prev;
        return value;
    }

    public T peek()
    {
        if (top == null)
            return null;
        return top.value;
    }

    public boolean isEmpty()
    {
        return top == null;
    }

    private static class Node<T>
    {
        private Node<T> prev;
        private T value;

        public Node(Node<T> prev, T value)
        {
            this.prev = prev;
            this.value = value;
        }
    }
}