package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SSDLongArray implements SSD<long[]>
{
    public static final String NAME = "SSD_Long_Array";
    public static final byte TYPE = 12;

    private long[] value;

    public SSDLongArray(long[] value)
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