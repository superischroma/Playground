package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SSDShortArray implements SSD<short[]>
{
    public static final String NAME = "SSD_Short_Array";
    public static final byte TYPE = 10;

    private short[] value;

    public SSDShortArray(short[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(short[] value)
    {
        this.value = value;
    }

    @Override
    public short[] getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return value.length * 2;
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
        for (short s : value)
            bb.putShort(s);
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}