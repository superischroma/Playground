package me.superischroma.playground.math.geometry;

public class Line
{
    private Point[] points;

    public Line()
    {
        this.points = new Point[2];
    }

    public Line(Point[] points)
    {
        this.points = points;
    }

    public Point[] getPoints()
    {
        return points;
    }

    public void setPoint(int i, Point point)
    {
        this.points[i] = point;
    }
}