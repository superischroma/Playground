package me.superischroma.playground.ssd;

import java.nio.ByteBuffer;

public class SSDInt implements SSD<Integer>
{
    public static final String NAME = "SSD_Int";
    public static final byte TYPE = 3;

    private int value;

    public SSDInt(int value)
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