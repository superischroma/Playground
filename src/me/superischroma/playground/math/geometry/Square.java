package me.superischroma.playground.math.geometry;

public class Square
{
    private Line[] lines;

    public Square()
    {
        this.lines = new Line[4];
    }

    public Square(Line[] lines)
    {
        this.lines = lines;
    }

    public Line[] getLines()
    {
        return lines;
    }

    public void setLine(int i, Line line)
    {
        this.lines[i] = line;
    }
}