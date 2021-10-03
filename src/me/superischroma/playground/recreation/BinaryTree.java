package me.superischroma.playground.recreation;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * A simple implementation of a binary tree.
 * If complexity is not specified in the documentation
 * of a method, then the complexity is O(1).
 * @param <T> Data stored in the binary tree
 */
public class BinaryTree<T extends Comparable<T>> implements Cloneable, Iterable<T>
{
    private Node<T> root;
    private int size;

    private BinaryTree(Node<T> root, int size)
    {
        this.root = root;
        this.size = size;
    }

    /**
     * Creates an empty binary tree.
     */
    public BinaryTree()
    {
        this(null, 0);
    }

    /**
     * Creates a binary tree from an existing array.<br>
     * Elements of the array will be sorted automatically.
     * Complexity: O(n log n)<br>
     *  - n: amount of nodes being added<br>
     *  - log n: sorting<br>
     * @param array Elements to be inserted into the binary tree
     */
    public BinaryTree(T[] array)
    {
        this();
        push(array);
    }

    /**
     * Creates a binary tree from an existing Collection.<br>
     * Elements of the Collection will be sorted automatically.
     * Complexity: O(n log n)<br>
     *  - n: amount of nodes being added<br>
     *  - log n: sorting<br>
     * @param collection Elements to be inserted into the binary tree
     */
    public BinaryTree(Collection<T> collection)
    {
        this();
        for (T item : collection)
            push(item);
    }

    /**
     * Adds nodes to this binary tree.<br>
     * Complexity: O(n log n)<br>
     *  - n: amount of nodes being added<br>
     *  - log n: sorting<br>
     * @param nodes Nodes to add to binary tree
     */
    @SafeVarargs
    public final void push(T... nodes)
    {
        for (T node : nodes)
            push(node);
    }

    /**
     * Adds a node to this binary tree.<br>
     * Complexity: O(log n)<br>
     *  - log n: sorting<br>
     * @param node Node to add to binary tree
     */
    public void push(T node)
    {
        pushRecursively(node, root);
    }

    private void pushRecursively(T node, Node<T> root)
    {
        if (this.root == null)
        {
            this.root = new Node<>(node, null, null);
            size++;
            return;
        }
        int comparison = compare(node, root.value);
        if (comparison < 0)
        {
            if (root.left == null)
            {
                root.left = new Node<>(node, null, null);
                size++;
                return;
            }
            pushRecursively(node, root.left);
        }
        else
        {
            if (root.right == null)
            {
                root.right = new Node<>(node, null, null);
                size++;
                return;
            }
            pushRecursively(node, root.right);
        }
    }

    /**
     * Removes nodes from this binary tree.<br>
     * Complexity: O(n log n)<br>
     *  - n: amount of nodes being removed<br>
     *  - log n: traversing<br>
     * @param nodes Nodes to remove from binary tree
     * @return The amount of nodes removed successfully
     */
    @SafeVarargs
    public final int remove(T... nodes)
    {
        int successes = 0;
        for (T node : nodes)
        {
            if (remove(node) != null)
                successes++;
        }
        return successes;
    }

    /**
     * Removes a node from this binary tree.<br>
     * Complexity: O(log n)<br>
     *  - log n: traversing<br>
     * @param node Node to remove from binary tree
     * @return Whether this node was found and removed or not
     */
    public T remove(T node)
    {
        if (root == null)
            return null;
        AtomicBoolean state = new AtomicBoolean();
        Node<T> parent = parentNode(node, root, state);
        Node<T> value = parent != null ? (state.get() ? parent.right : parent.left) : root;
        if (value.isLeaf())
        {
            if (parent == null)
                root = null;
            else if (state.get())
                parent.right = null;
            else
                parent.left = null;
        }
        else if (value.hasOneChild())
        {
            if (parent == null)
                root = value.getOnlyChild();
            else if (state.get())
                parent.right = value.getOnlyChild();
            else
                parent.left = value.getOnlyChild();
        }
        else // 2 children
        {
            AtomicBoolean farState = new AtomicBoolean();
            Node<T> farParent = parentFarthestRight(value.left, farState);
            if (farParent == null)
                return null;
            Node<T> farNode = farState.get() ? farParent.right : farParent.left;
            if (farState.get())
                farParent.right = null;
            else
                farParent.left = null;
            value.value = farNode.value;
        }
        size--;
        return value.value;
    }

    /**
     * Searches the binary tree for the value specified.<br>
     * Complexity: O(log n)<br>
     *  - log n: traversing<br>
     * @param value What to look for
     * @return The value contained in the tree (Comparable elements could be implemented in a key-value way)
     */
    public T search(T value)
    {
        if (root != null && compare(value, root.value) == 0)
            return root.value;
        AtomicBoolean state = new AtomicBoolean();
        Node<T> parent = parentNode(value, root, state);
        return parent != null ? (state.get() ? parent.right : parent.left).value : null;
    }

    private Node<T> parentNode(T value, Node<T> root, AtomicBoolean state)
    {
        if (root == null)
        {
            state.set(false);
            return null;
        }
        int comparison = compare(value, root.value);
        if (root.left != null)
        {
            int iComparison = compare(value, root.left.value);
            if (iComparison == 0)
            {
                state.set(false);
                return root;
            }
            if (comparison < 0)
                return parentNode(value, root.left, state);
        }
        if (root.right != null)
        {
            int iComparison = compare(value, root.right.value);
            if (iComparison == 0)
            {
                state.set(true);
                return root;
            }
            if (comparison > 0)
                return parentNode(value, root.right, state);
        }
        state.set(true);
        return null;
    }

    private Node<T> parentFarthestRight(Node<T> root, AtomicBoolean state)
    {
        if (root == null)
        {
            state.set(false);
            return null;
        }
        if (root.right != null && !root.right.isLeaf())
            return parentFarthestRight(root.right, state);
        if (root.left != null && !root.left.isLeaf())
            return parentFarthestRight(root.left, state);

        state.set(root.right != null);
        return root;
    }

    /**
     * @return The depth of the binary tree
     */
    public int depth()
    {
        return (int) (Math.log(size) / Math.log(2));
    }

    /**
     * @return The amount of nodes in this binary tree
     */
    public int size()
    {
        return size;
    }

    private int sizeRecursively(Node<T> root)
    {
        if (root == null)
            return 0;
        int c = 1;
        if (root.left != null)
            c += sizeRecursively(root.left);
        if (root.right != null)
            c += sizeRecursively(root.right);
        return c;
    }

    /**
     * Creates a subtree of this binary tree.
     * @param node Root of the subtree
     * @return A subtree of the current binary tree
     */
    public BinaryTree<T> subTree(T node)
    {
        AtomicBoolean state = new AtomicBoolean();
        Node<T> found = parentNode(node, root, state);
        if (found == null)
            return null;
        Node<T> child = state.get() ? found.right : found.left;
        return new BinaryTree<>(child, sizeRecursively(child));
    }

    /**
     * Inverts this binary tree
     * @return This binary tree
     */
    public BinaryTree<T> invert()
    {
        invertRecursively(root);
        return this;
    }

    private void invertRecursively(Node<T> root)
    {
        if (root == null)
            return;
        Node<T> left = root.left;
        root.left = root.right;
        root.right = left;
        invertRecursively(root.left);
        invertRecursively(root.right);
    }

    public BinaryTree<T> clone()
    {
        return new BinaryTree<>(root.clone(), size);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder().append("[");
        if (root != null)
            toStringRecursively(builder, root);
        builder.append("]");
        return builder.toString();
    }

    private void toStringRecursively(StringBuilder builder, Node<T> root)
    {
        if (this.root != root)
            builder.append(", ");
        builder.append(root);
        if (root.left != null)
            toStringRecursively(builder, root.left);
        if (root.right != null)
            toStringRecursively(builder, root.right);
    }

    private static <T extends Comparable<T>> int compare(T o1, T o2)
    {
        if (o1 == null && o2 == null)
            return 0;
        if (o1 != null && o2 == null)
            return 1;
        if (o1 == null)
            return -1;
        return o1.compareTo(o2);
    }

    @Override
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException("No direct iteration allowed for a binary tree yet; use BinaryTree#forEach(Consumer)");
    }

    public void forEach(Consumer<? super T> action)
    {
        forEachRecursively(root, action);
    }

    private void forEachRecursively(Node<T> root, Consumer<? super T> action)
    {
        if (root == null)
            return;
        action.accept(root.value);
        forEachRecursively(root.left, action);
        forEachRecursively(root.right, action);
    }

    private static class Node<T extends Comparable<T>> implements Cloneable
    {
        private T value;
        private Node<T> left;
        private Node<T> right;

        public Node(T value, Node<T> left, Node<T> right)
        {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString()
        {
            return this.value != null ? this.value.toString() : null;
        }

        public Node<T> clone()
        {
            return new Node<>(value, left != null ? left.clone() : null, right != null ? right.clone() : null);
        }

        public boolean isLeaf()
        {
            return left == null && right == null;
        }

        public boolean hasOneChild()
        {
            return hasLeftOnly() || hasRightOnly();
        }

        public Node<T> getOnlyChild()
        {
            return left != null ? left : right;
        }

        public boolean isComplete()
        {
            return left != null && right != null;
        }

        public boolean hasLeftOnly()
        {
            return left != null && right == null;
        }

        public boolean hasRightOnly()
        {
            return left == null && right != null;
        }
    }
}