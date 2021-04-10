package me.superischroma.playground.school;

import java.util.ArrayList;

public class SparseArray<T> extends ArrayList<SparseArrayEntry<T>>
{
    private int numRows;
    private int numCols;

    public int getNumRows()
    {
        return numRows;
    }

    public int getNumCols()
    {
        return numCols;
    }

    public T getValueAt(int row, int col)
    {
        for (SparseArrayEntry<T> entry : this)
        {
            if (entry.getRow() == row && entry.getCol() == col)
                return entry.getValue();
        }
        return null;
    }

    public void removeColumn(int col)
    {
        this.removeIf(entry -> entry.getCol() == col);
    }
}
