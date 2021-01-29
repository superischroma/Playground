package me.superischroma.playground.struct;

import java.nio.ByteBuffer;

public class StructDouble implements Struct<Double>
{
    public static final String NAME = "Struct_Double";
    public static final byte TYPE = 6;

    private double value;

    public StructDouble(double value)
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