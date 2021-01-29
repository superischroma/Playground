package me.superischroma.playground.struct;

import java.nio.ByteBuffer;

public class StructShort implements Struct<Short>
{
    public static final String NAME = "Struct_Short";
    public static final byte TYPE = 2;

    private short value;

    public StructShort(short value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Short value)
    {
        this.value = value;
    }

    @Override
    public Short getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return 2;
    }

    @Override
    public byte[] asByteArray()
    {
        return ByteBuffer.allocate(length()).putShort(value).array();
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}