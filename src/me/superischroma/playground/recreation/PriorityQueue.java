package me.superischroma.playground.recreation;

public class PriorityQueue<T extends Comparable<T>>
{
    public static final boolean LEAST = false;
    public static final boolean GREATEST = true;

    private Comparable[] elements;
    private int size;
    private boolean priority;

    public PriorityQueue(int capacity, boolean priority)
    {
        this.elements = new Comparable[capacity];
        this.size = 0;
        this.priority = priority;
    }

    public PriorityQueue(int capacity)
    {
        this(capacity, false);
    }

    public PriorityQueue(boolean priority)
    {
        this(11, priority);
    }

    public PriorityQueue()
    {
        this(11, false);
    }

    @SafeVarargs
    public final void push(T... elements)
    {
        for (T element : elements)
            push(element);
    }

    public void push(T element)
    {
        if (size + 1 >= elements.length)
            grow(6);
        elements[++size] = element;
        for (int i = size; i >= 2; i /= 2)
        {
            int comparison = compare(elements[i], elements[i / 2]);
            if (priority == LEAST ? comparison < 0 : comparison > 0)
                swap(i, i / 2);
            else
                break;
        }
    }

    public T pop()
    {
        if (size == 0)
            return null;
        T element = (T) this.elements[1];
        this.elements[1] = this.elements[size];
        this.elements[size--] = null;
        for (int i = 1; i < size / 2 + 1;)
        {
            Comparable parent = elements[i];
            Comparable child1 = elements[i * 2];
            if ((i * 2) + 1 > size) // if there is no 2nd child
            {
                int comparison = compare(child1, parent);
                if (priority == LEAST ? comparison < 0 : comparison > 0)
                {
                    elements[i] = child1;
                    elements[i * 2] = parent;
                }
                break;
            }
            Comparable child2 = elements[(i * 2) + 1];
            int c1pc = compare(child1, parent);
            int c2pc = compare(child2, parent);
            if ((priority == LEAST ? c1pc > 0 : c1pc < 0) && (priority == LEAST ? c2pc > 0 : c2pc < 0))
                break;
            int c2c1c = compare(child2, child1);
            if (priority == LEAST ? c2c1c > 0 : c2c1c < 0)
            {
                elements[i] = child1;
                elements[i = i * 2] = parent;
            }
            else
            {
                elements[i] = child2;
                elements[i = (i * 2) + 1] = parent;
            }
        }
        return element;
    }

    private void swap(int i1, int i2)
    {
        Comparable temp = this.elements[i1];
        this.elements[i1] = this.elements[i2];
        this.elements[i2] = temp;
    }

    private void grow(int amount)
    {
        Object[] cache = elements;
        this.elements = new Comparable[cache.length + amount];
        System.arraycopy(cache, 0, this.elements, 0, cache.length);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 1; i <= size; i++)
            builder.append(i != 1 ? ", " : "").append(this.elements[i]);
        return builder.append(']').toString();
    }

    private static <T extends Comparable> int compare(T o1, T o2)
    {
        if (o1 == null && o2 == null)
            return 0;
        if (o1 != null && o2 == null)
            return 1;
        if (o1 == null)
            return -1;
        return o1.compareTo(o2);
    }
}