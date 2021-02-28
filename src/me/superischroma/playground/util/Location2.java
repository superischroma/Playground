package me.superischroma.playground.util;

public class Location2
{
    private int x;
    private int y;

    public Location2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public Location2 setX(int x)
    {
        this.x = x;
        return this;
    }

    public int getY()
    {
        return y;
    }

    public Location2 setY(int y)
    {
        this.y = y;
        return this;
    }

    public Location2 add(int deltaX, int deltaY)
    {
        this.x += deltaX;
        this.y += deltaY;
        return this;
    }

    public Location2 add(int delta)
    {
        this.x += delta;
        this.y += delta;
        return this;
    }

    public Location2 subtract(int deltaX, int deltaY)
    {
        this.x -= deltaX;
        this.y -= deltaY;
        return this;
    }

    public Location2 subtract(int delta)
    {
        this.x -= delta;
        this.y -= delta;
        return this;
    }

    public Location2 multiply(int deltaX, int deltaY)
    {
        this.x *= deltaX;
        this.y *= deltaY;
        return this;
    }

    public Location2 multiply(int delta)
    {
        this.x *= delta;
        this.y *= delta;
        return this;
    }

    public Location2 divide(int deltaX, int deltaY)
    {
        this.x /= deltaX;
        this.y /= deltaY;
        return this;
    }

    public Location2 divide(int delta)
    {
        this.x /= delta;
        this.y /= delta;
        return this;
    }
}