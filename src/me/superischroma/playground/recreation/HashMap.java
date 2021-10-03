package me.superischroma.playground.recreation;

import java.util.concurrent.atomic.AtomicBoolean;

public class HashMap<K extends Comparable<K>, V>
{
    private BinaryTree<Node<K, V>>[] buckets;
    private BinaryTree<Integer> filled;

    public HashMap(int buckets)
    {
        this.buckets = new BinaryTree[buckets];
        this.filled = new BinaryTree<>();
    }

    public HashMap()
    {
        this(50);
    }

    public V get(K key)
    {
        BinaryTree<Node<K, V>> bucket = this.buckets[key.hashCode() % buckets.length];
        if (bucket == null)
            return null;
        Node<K, V> node = bucket.search(new Node<>(key, null));
        return node != null ? node.value : null;
    }

    public V put(K key, V value)
    {
        V prev = get(key);
        if (prev != null)
            remove(key);
        BinaryTree<Node<K, V>> bucket = this.buckets[key.hashCode() % buckets.length];
        if (bucket == null)
        {
            bucket = new BinaryTree<>();
            this.buckets[key.hashCode() % buckets.length] = bucket;
        }
        Node<K, V> node = new Node<>(key, value);
        bucket.push(node);
        filled.push(key.hashCode() % buckets.length);
        return prev;
    }

    public V remove(K key)
    {
        BinaryTree<Node<K, V>> bucket = this.buckets[key.hashCode() % buckets.length];
        if (bucket == null)
            return null;
        Node<K, V> node = bucket.remove(new Node<>(key, null));
        if (bucket.size() <= 0)
        {
            this.buckets[key.hashCode() % buckets.length] = null; // deallocate memory for binary tree
            filled.remove(key.hashCode() % buckets.length);
        }
        return node != null ? node.value : null;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("{");
        filled.forEach(index ->
        {
            BinaryTree<Node<K, V>> bucket = this.buckets[index];
            final AtomicBoolean first = new AtomicBoolean(true);
            bucket.forEach(node ->
            {
                if (!first.get())
                    builder.append(", ");
                else
                    first.set(false);
                builder.append(node.key).append("=").append(node.value);
            });
        });
        return builder.append("}").toString();
    }

    private static class Node<K extends Comparable<K>, V> implements Comparable<Node<K, V>>
    {
        private K key;
        private V value;

        public Node(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Node<K, V> o)
        {
            return key.compareTo(o.key);
        }

        public String toString()
        {
            return "{" + key + ", " + value + "}";
        }
    }
}
