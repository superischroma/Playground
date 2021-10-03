package me.superischroma.playground.recreation;

import java.util.Iterator;
import java.util.Objects;

public class LinkedArray<T> implements Iterable<T>
{
    private Node<T> head;
    private Node<T> tail;
    private int size;

    @SafeVarargs
    public final void unshift(T... elements)
    {
        for (T element : elements)
            unshift(element);
    }

    public void unshift(T element)
    {
        head = new Node<>(element, head);
        if (tail == null)
            tail = head;
        size++;
    }

    @SafeVarargs
    public final void push(T... elements)
    {
        for (T element : elements)
            push(element);
    }

    public void push(T element)
    {
        if (tail != null)
        {
            tail.next = new Node<>(element, null);
            tail = tail.next;
        }
        else
        {
            head = new Node<>(element, null);
            tail = head;
        }
        size++;
    }

    public T shift()
    {
        if (head == null)
            return null;
        T element = head.value;
        head = head.next;
        if (head == null)
            tail = null;
        size--;
        return element;
    }

    public T pop()
    {
        return remove(size - 1);
    }

    public boolean remove(T equivalent)
    {
        if (head == null)
            return false;
        if (Objects.equals(head.value, equivalent))
        {
            shift();
            return true;
        }
        for (Node<T> current = head; current.next != null; current = current.next)
        {
            if (Objects.equals(equivalent, current.next.value))
            {
                if (current.next == tail)
                    tail = current;
                current.next = current.next.next;
                size--;
                return true;
            }
        }
        return false;
    }

    public T remove(int index)
    {
        if (head == null)
            return null;
        if (index == 0)
            return shift();
        Node<T> current = head;
        for (int i = 0; current.next != null && i < index - 1; current = current.next, i++);
        if (current.next == null)
            return null;
        T element = current.next.value;
        if (current.next == tail)
            tail = current;
        current.next = current.next.next;
        size--;
        return element;
    }

    public T head()
    {
        return head != null ? head.value : null;
    }

    public T tail()
    {
        return tail != null ? tail.value : null;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("[");
        for (Node<T> current = head; current != null; current = current.next)
            builder.append(current != head ? ", " : "").append(current.value);
        return builder.append("]").toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<>()
        {
            Node<T> current = new Node<>(null, head);

            @Override
            public boolean hasNext()
            {
                return current != tail;
            }

            @Override
            public T next()
            {
                current = current.next;
                return current != null ? current.value : null;
            }
        };
    }

    private static class Node<T>
    {
        private T value;
        private Node<T> next;

        public Node(T value, Node<T> next)
        {
            this.value = value;
            this.next = next;
        }
    }
}