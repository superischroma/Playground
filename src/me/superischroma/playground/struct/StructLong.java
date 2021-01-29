package me.superischroma.playground.struct;

import java.nio.ByteBuffer;

public class StructLong implements Struct<Long>
{
    public static final String NAME = "Struct_Long";
    public static final byte TYPE = 4;

    private long value;

    public StructLong(long value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Long value)
    {
        this.value = value;
    }

    @Override
    public Long getValue()
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
        return ByteBuffer.allocate(length()).putLong(value).array();
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}