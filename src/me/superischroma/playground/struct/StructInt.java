package me.superischroma.playground.struct;

import java.nio.ByteBuffer;

public class StructInt implements Struct<Integer>
{
    public static final String NAME = "Struct_Int";
    public static final byte TYPE = 3;

    private int value;

    public StructInt(int value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Integer value)
    {
        this.value = value;
    }

    @Override
    public Integer getValue()
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
        return ByteBuffer.allocate(length()).putInt(value).array();
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}