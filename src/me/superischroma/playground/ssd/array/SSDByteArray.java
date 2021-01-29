package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.util.Arrays;

public class SSDByteArray implements SSD<byte[]>
{
    public static final String NAME = "SSD_Byte_Array";
    public static final byte TYPE = 9;

    private byte[] value;

    public SSDByteArray(byte[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(byte[] value)
    {
        this.value = value;
    }

    @Override
    public byte[] getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return value.length;
    }

    @Override
    public boolean usesSpecialLength()
    {
        return true;
    }

    @Override
    public byte[] asByteArray()
    {
        return value;
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}