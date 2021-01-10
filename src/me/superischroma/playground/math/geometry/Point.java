package me.superischroma.playground.math.geometry;

public class Point
{
    private int x;
    private int y;
    private int z;
    private int w;

    public Point(int x, int y, int z, int w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public int getW()
    {
        return w;
    }
}