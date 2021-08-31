package me.superischroma.playground.recreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A dynamic array in the style of JavaScript.
 * Capable of performing the majority of operations performed by Collection framework objects.
 * Provides more methods to interact with the array directly.
 *
 * This dynamic array implementation uses a concept called capacity.
 * This concept is applied in some Collection implementations.
 * The capacity is modifiable using {@link #setCapacity(int) setCapacity(capacity)} and can be viewed at any time using {@link #capacity capacity}.
 *
 * This dynamic array uses varargs when possible in order to maximize efficiency.
 * Methods such as {@link #push(Object[]) push(elements...)} and {@link #insert(int, Object[])} insert(start, elements...)}
 * include varargs so multiple insertions can be made to the array in a concise format.
 *
 * Iterators are not fail-fast and the array can be structurally modified during iteration.
 *
 * @param <T>
 */
public class Array<T> implements ArrayLike<T>
{
    private Object[] elements;
    private int size;

    /**
     * Creates a new dynamic array with the specified capacity.
     * @param capacity The starting capacity
     */
    public Array(int capacity)
    {
        this.elements = new Object[capacity];
    }

    /**
     * Creates a new dynamic array with a capacity of 10.
     */
    public Array()
    {
        this(10);
    }

    /**
     * Creates a new dynamic array from a normal array.
     * @param array The original array
     */
    public Array(T[] array)
    {
        this.elements = copyOf(array, array.length);
        this.size = array.length;
    }

    /**
     * Creates a new dynamic array from another dynamic array.
     * @param copy Another dynamic array
     */
    public Array(Array<? extends T> copy)
    {
        this.elements = copyOf(copy.elements, copy.elements.length);
        this.size = copy.size;
    }

    /**
     * Creates a new dynamic array from a Collection-based group of objects.
     * @param collection The base for the new dynamic array
     */
    public Array(Collection<? extends T> collection)
    {
        this.elements = collection.toArray();
        this.size = collection.size();
    }

    /**
     * Append elements to the end of the array
     * @param elements Elements that should be pushed on
     */
    @SafeVarargs
    public final void push(T... elements)
    {
        checkResize(elements.length);
        System.arraycopy(elements, 0, this.elements, size, elements.length);
        size += elements.length;
    }

    /**
     * Insert elements at a specific point in the array
     * @param startIndex The index in which elements should start to be placed
     * @param elements The elements to insert
     */
    @SafeVarargs
    public final void insert(int startIndex, T... elements)
    {
        checkResize(elements.length);
        System.arraycopy(this.elements, startIndex, this.elements, startIndex + elements.length, size - (startIndex));
        System.arraycopy(elements, 0, this.elements, startIndex, elements.length);
        size += elements.length;
    }

    /**
     * Concatenates an entire Array to the end of this Array.
     * @param array Another Array
     */
    public void concat(Array<? extends T> array)
    {
        checkResize(array.size);
        System.arraycopy(array.elements, 0, this.elements, size, array.size);
        size += array.size;
    }

    /**
     * Concatenates a Collection to the end of this Array.
     * @param collection The Collection to append
     */
    public void concat(Collection<? extends T> collection)
    {
        checkResize(collection.size());
        System.arraycopy(collection.toArray(), 0, this.elements, size, collection.size());
        size += collection.size();
    }

    /**
     * Retrieves the value stored at the specified index of this Array.
     * @param index The index to retrieve from
     * @return The value in that location
     * @throws ArrayIndexOutOfBoundsException When the index is outside of the range of the Array.
     */
    @Override
    public T get(int index)
    {
        checkBounds(index);
        return (T) elements[index];
    }

    /**
     * Removes the value from the Array at the specified index
     * @param index The index to remove from
     * @return The value that was just removed
     * @throws ArrayIndexOutOfBoundsException When the index is outside of the range of the Array.
     */
    public T remove(int index)
    {
        checkBounds(index);
        T cache = (T) elements[index];
        if (size - 1 - index >= 0) System.arraycopy(elements, index + 1, elements, index, size - 1 - index);
        elements[size - 1] = null;
        size--;
        return cache;
    }

    /**
     * Removes the first value which equals the equivalent
     * @param equivalent The value to match
     * @return If a value was found and removed
     */
    public boolean remove(T equivalent)
    {
        int index = indexOf(equivalent);
        if (index < 0)
            return false;
        remove(index);
        return true;
    }

    /**
     * Removes the first element of the array and returns it
     * @return The element just removed
     * @throws ArrayIndexOutOfBoundsException When there is no first element
     */
    public T shift()
    {
        checkBounds(0);
        return remove(0);
    }

    /**
     * Removes the last element of the array and returns it
     * @return The element just removed
     * @throws ArrayIndexOutOfBoundsException When there is no last element
     */
    public T pop()
    {
        checkBounds(size - 1);
        return remove(size - 1);
    }

    /**
     * Inserts elements at the start of the array
     * @param elements The elements to insert
     * @return The new length of the array
     */
    @SafeVarargs
    public final int unshift(T... elements)
    {
        insert(0, elements);
        return size;
    }

    /**
     * Checks to see if a value can be found within this Array
     * This method uses a linear search and has a
     * time complexity range of O(1) - O(n) depending
     * on how fast the method is able to find the
     * equivalent value.
     *
     * @param test The test value
     * @return Whether or not an equivalent was found
     */
    public boolean contains(T test)
    {
        return indexOf(test) >= 0;
    }

    /**
     * Clones this array
     * @return A clone of this array
     */
    public Array<T> clone()
    {
        try
        {
            return (Array<T>) super.clone();
        }
        catch (CloneNotSupportedException ignored) {}
        return null;
    }

    /**
     * Sets the capacity of this array
     * @param capacity The new capacity
     */
    public void setCapacity(int capacity)
    {
        this.elements = copyOf(this.elements, capacity);
    }

    /**
     * Swaps the values in the specified indices
     * @param firstIndex The first index
     * @param secondIndex The second index
     */
    public void swap(int firstIndex, int secondIndex)
    {
        checkBounds(firstIndex);
        checkBounds(secondIndex);
        Object c = elements[firstIndex];
        elements[firstIndex] = elements[secondIndex];
        elements[secondIndex] = c;
    }

    /**
     * Attempts to find a value within this dynamic array
     * This method uses a linear search and has a
     * time complexity range of O(1) - O(n) depending
     * on how fast the method is able to find a
     * value that fulfills the Predicate's test.
     *
     * @param filter The condition to find the value
     * @return The value found, null otherwise
     */
    public T find(Predicate<? super T> filter)
    {
        for (int i = 0; i < size; i++)
        {
            T val = (T) elements[i];
            if (filter.test(val))
                return val;
        }
        return null;
    }

    /**
     * Finds the index of the test element
     * This method uses a linear search and has a
     * time complexity range of O(1) - O(n) depending
     * on how fast the method is able to find the
     * equivalent value.
     *
     * @param test The element to test with
     * @return The index of the element found, -1 otherwise
     */
    public int indexOf(T test)
    {
        if (test == null)
        {
            for (int i = 0; i < size; i++)
            {
                if (elements[i] == null)
                    return i;
            }
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                if (elements[i].equals(test))
                    return i;
            }
        }
        return -1;
    }

    /**
     * Checks if this dynamic array has no elements
     * @return Whether no elements exist
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Retrieves the size of this dynamic array
     * @return The current size
     */
    @Override
    public int size()
    {
        return size;
    }

    /**
     * Retrieves the capacity of this dynamic array
     * @return The current capacity
     */
    public int capacity()
    {
        return elements.length;
    }

    /**
     * Sets the value at the specified index and returns the old value
     * @param index The index to override
     * @param value The new value for this index
     * @return The value that was originally in this location
     * @throws ArrayIndexOutOfBoundsException When the index is outside of the range of the Array.
     */
    @Override
    public T set(int index, T value)
    {
        checkBounds(index);
        T current = (T) elements[index];
        elements[index] = value;
        return current;
    }

    /**
     * Removes all values that meet the condition specified in the Predicate.
     * This method uses a linear search and has a
     * time complexity range of O(1) - O(n) depending
     * on how quickly the method is able to find values
     * that meet the condition and what the limit for
     * removing is.
     *
     * @param condition The test for values to get removed
     * @param limit The limit for the amount of elements to be removed
     * @return The amount of elements removed
     */
    public int removeIf(Predicate<? super T> condition, int limit)
    {
        int removed = 0;
        for (int i = 0; i < size; i++)
        {
            if (condition.test((T) elements[i]))
            {
                this.remove(i);
                i--;
                removed++;
            }
            if (limit > 0 && removed == limit)
                break;
        }
        return removed;
    }

    /**
     * Removes all values that meet the condition specified in the Predicate.
     * Limitless removals
     * This method uses a linear search and has a
     * time complexity of O(n).
     *
     * @param condition The test for values to get removed
     * @return The amount of elements removed
     */
    public int removeIf(Predicate<? super T> condition)
    {
        return removeIf(condition, 0);
    }

    /**
     * Joins the elements of this array into a String separated by another String.
     * @param separator What to separate the elements by
     * @return A String of joined elements
     */
    public String join(String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++)
            builder.append(i != 0 ? separator : "").append(elements[i] != null ? elements[i].toString() : null);
        return builder.toString();
    }

    /**
     * Joins the elements of this array into a String separated by commas.
     * @return A String of joined elements
     */
    public String join()
    {
        return join(", ");
    }

    /**
     * Removes all elements from this dynamic array and sets
     * the capacity to 10.
     */
    public void clear()
    {
        this.elements = new Object[10];
        this.size = 0;
    }

    /**
     * Retrieves this dynamic array as an Object array.
     * @return An Object array with all elements from this dynamic array
     */
    public Object[] toArray()
    {
        return copyOf(elements, size);
    }

    /**
     * Retrieves this dynamic array as an array.
     * @return An array with all elements from this dynamic array
     */
    public T[] toArray(Class<T> clazz)
    {
        return copyOf(elements, size, clazz);
    }

    /**
     * Retrieves this dynamic array as a String.
     * @return A String formatted Array
     */
    public String toString()
    {
        return toSpecificString(size);
    }

    /**
     * Retrieves this dynamic array as a String. (including all capacity values)
     * @return A String formatted Array
     */
    public String toRawString()
    {
        return toSpecificString(elements.length);
    }

    /**
     * Retrieves this dynamic array as a List Iterator.
     * @return A created List Iterator for this dynamic array
     */
    public ListIterator<T> listIterator()
    {
        return new ListIterator<>()
        {
            final Array<T> copy = Array.this.clone();
            int position = -1;

            @Override
            public boolean hasNext()
            {
                return position + 1 < copy.size;
            }

            @Override
            public T next()
            {
                return copy.get(++position);
            }

            @Override
            public boolean hasPrevious()
            {
                return position != 0;
            }

            @Override
            public T previous()
            {
                return copy.get(--position);
            }

            @Override
            public int nextIndex()
            {
                return position + 1;
            }

            @Override
            public int previousIndex()
            {
                return position - 1;
            }

            @Override
            public void remove()
            {
                Array.this.remove(position);
                copy.remove(position);
                position--;
            }

            @Override
            public void set(T t)
            {
                Array.this.set(position, t);
                copy.set(position, t);
            }

            @Override
            public void add(T t)
            {
                insert(position, t);
                copy.insert(position, t);
            }
        };
    }

    /**
     * Retrieves this dynamic array as an Iterator.
     * @return A created Iterator for this dynamic array
     */
    @Override
    public Iterator<T> iterator()
    {
        return listIterator();
    }

    /**
     * Iterate through all the elements of this dynamic array
     * @param action What to be performed with every element
     */
    @Override
    public void forEach(Consumer<? super T> action)
    {
        for (int i = 0; i < size; i++)
            action.accept((T) elements[i]);
    }

    /**
     * Check if this dynamic array is equivalent to another dynamic array
     * @param o Another dynamic array
     * @return Whether the two are equivalent
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Array))
            return false;
        Array<?> array = (Array<?>) o;
        for (int i = 0; i < size; i++)
        {
            if (array.elements[i] == elements[i])
                continue;
            if (!array.elements[i].equals(elements[i]))
                return false;
        }
        return true;
    }

    public List<T> toList(Class<T> clazz)
    {
        T[] a = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        System.arraycopy(this.elements, 0, a, 0, size);
        return new ArrayList<>(Arrays.asList(a));
    }

    /**
     * Create a new dynamic array off of vararg elements
     * @param elements Vararg elements to be inserted in to the dynamic array
     * @param <T> The type of the dynamic array
     * @return A newly created dynamic array with all the elements provided in the parameters
     */
    @SafeVarargs
    public static <T> Array<T> of(T... elements)
    {
        return new Array<>(elements);
    }

    /**
     * Splits a String into a dynamic array by the delimiter provided
     * @param source The original String to split
     * @param delimiter What to separate the source by
     * @return A dynamic array of separated Strings
     */
    public static Array<String> split(String source, String delimiter)
    {
        return new Array<>(source.split(delimiter));
    }

    private String toSpecificString(int less)
    {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < less; i++)
            builder.append(i != 0 ? ", " : "").append(elements[i] != null ? elements[i].toString() : null);
        builder.append("]");
        return builder.toString();
    }

    private void checkBounds(int index)
    {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException(index);
    }

    private void checkResize(int additionSize)
    {
        if (size + additionSize > elements.length)
            this.elements = copyOf(this.elements, size + additionSize);
    }

    private static <T, U> U[] copyOf(T[] original, int length, Class<U> clazz)
    {
        U[] copy = (U[]) java.lang.reflect.Array.newInstance(clazz, length);
        System.arraycopy(original, 0, copy, 0, Math.min(length, original.length));
        return copy;
    }

    private static <T> T[] copyOf(T[] original, int length)
    {
        T[] copy = (T[]) java.lang.reflect.Array.newInstance(original.getClass().getComponentType(), length);
        System.arraycopy(original, 0, copy, 0, Math.min(length, original.length));
        return copy;
    }
}