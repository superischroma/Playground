package me.superischroma.playground.ssd;

import java.nio.ByteBuffer;

public class SSDShort implements SSD<Short>
{
    public static final String NAME = "SSD_Short";
    public static final byte TYPE = 2;

    private short value;

    public SSDShort(short value)
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