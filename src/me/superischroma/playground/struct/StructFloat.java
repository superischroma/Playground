package me.superischroma.playground.struct;

import java.nio.ByteBuffer;

public class StructFloat implements Struct<Float>
{
    public static final String NAME = "Struct_Float";
    public static final byte TYPE = 5;

    private float value;

    public StructFloat(float value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Float value)
    {
        this.value = value;
    }

    @Override
    public Float getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return 4;
    }

    @Override
    public byte[] asByteArray()
    {
        return ByteBuffer.allocate(length()).putFloat(value).array();
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}