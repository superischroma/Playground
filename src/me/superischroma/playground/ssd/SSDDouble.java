package me.superischroma.playground.ssd;

import java.nio.ByteBuffer;

public class SSDDouble implements SSD<Double>
{
    public static final String NAME = "SSD_Double";
    public static final byte TYPE = 6;

    private double value;

    public SSDDouble(double value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Double value)
    {
        this.value = value;
    }

    @Override
    public Double getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return 8;
    }

    @Override
    public byte[] asByteArray()
    {
        return ByteBuffer.allocate(length()).putDouble(value).array();
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}