package me.superischroma.playground.recreation;

/**
 * A utility to map keys to values.
 * This map is considered weak because of
 * its speed at retrieving values.
 * The general speed for retrieving map values
 * is around O(n) for this map implementation.
 *
 * @param <K>
 * @param <V>
 */
public class WeakMap<K, V>
{
    private Array<Entry<K, V>> entries;

    public WeakMap()
    {
        this.entries = new Array<>();
    }

    public V get(K key)
    {
        Entry<K, V> entry = getEntry(key);
        return entry != null ? entry.getValue() : null;
    }

    public V put(K key, V value)
    {
        Entry<K, V> previous = getEntry(key);
        if (previous != null)
            entries.remove(previous);
        entries.push(new Entry<>(key, value));
        return previous != null ? previous.getValue() : null;
    }

    public boolean remove(K key)
    {
        return entries.removeIf(entry -> key.equals(entry.key)) > 0;
    }

    private Entry<K, V> getEntry(K key)
    {
        for (Entry entry : entries)
        {
            if (key.equals(entry.getKey()))
                return entry;
        }
        return null;
    }

    public int size()
    {
        return entries.size();
    }

    public static class Entry<K, V>
    {
        private K key;
        private V value;

        public Entry(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        public K getKey()
        {
            return key;
        }

        public void setKey(K key)
        {
            this.key = key;
        }

        public V getValue()
        {
            return value;
        }

        public void setValue(V value)
        {
            this.value = value;
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof Entry))
                return false;
            Entry entry = (Entry) o;
            return key.equals(entry.key) && value.equals(entry.value);
        }
    }
}