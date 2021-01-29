package me.superischroma.playground.struct;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class StructLongArray implements Struct<long[]>
{
    public static final String NAME = "Struct_Long_Array";
    public static final byte TYPE = 11;

    private long[] value;

    public StructLongArray(long[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(long[] value)
    {
        this.value = value;
    }

    @Override
    public long[] getValue()
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
        for (long l : value)
            bb.putLong(l);
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}