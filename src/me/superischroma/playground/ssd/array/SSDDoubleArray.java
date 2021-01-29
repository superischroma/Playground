package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SSDDoubleArray implements SSD<double[]>
{
    public static final String NAME = "SSD_Double_Array";
    public static final byte TYPE = 14;

    private double[] value;

    public SSDDoubleArray(double[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(double[] value)
    {
        this.value = value;
    }

    @Override
    public double[] getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return value.length * 8;
    }

    @Override
    public boolean usesSpecialLength()
    {
        return true;
    }

    @Override
    public byte[] asByteArray()
    {
        ByteBuffer bb = ByteBuffer.allocate(length());
        for (double d : value)
            bb.putDouble(d);
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}