package me.superischroma.playground.school;

public class GrayImage
{
    public static final short WHITE = 0xFF;
    public static final short BLACK = 0x00;

    private final int[][] pixelValues;

    public GrayImage()
    {
        this.pixelValues = new int[5][4];
    }

    public int countWhitePixels()
    {
        int counter = 0;
        for (int[] is : pixelValues)
        {
            for (int i : is)
            {
                if (i == WHITE)
                    counter++;
            }
        }
        return counter;
    }
}