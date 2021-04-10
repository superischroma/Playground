package me.superischroma.playground.school;

public class SparseArrayEntry<T>
{
    private final int row;
    private final int col;

    private final T value;

    public SparseArrayEntry(int row, int col, T value)
    {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public T getValue()
    {
        return value;
    }
}