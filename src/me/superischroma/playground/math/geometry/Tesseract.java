package me.superischroma.playground.math.geometry;

public class Tesseract
{
    private Cube[] cubes;

    public Tesseract()
    {
        this.cubes = new Cube[8];
    }

    public Tesseract(Cube[] cubes)
    {
        this.cubes = cubes;
    }

    public Cube[] getCubes()
    {
        return cubes;
    }

    public void setCube(int i, Cube cube)
    {
        this.cubes[i] = cube;
    }
}