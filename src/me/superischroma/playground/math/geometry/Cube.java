package me.superischroma.playground.math.geometry;

public class Cube
{
    private Square[] squares;

    public Cube()
    {
        this.squares = new Square[6];
    }

    public Cube(Square[] squares)
    {
        this.squares = squares;
    }

    public Square[] getSquares()
    {
        return squares;
    }

    public void setSquare(int i, Square square)
    {
        this.squares[i] = square;
    }
}