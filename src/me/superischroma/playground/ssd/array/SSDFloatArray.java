package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SSDFloatArray implements SSD<float[]>
{
    public static final String NAME = "SSD_Float_Array";
    public static final byte TYPE = 13;

    private float[] value;

    public SSDFloatArray(float[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(float[] value)
    {
        this.value = value;
    }

    @Override
    public float[] getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return value.length * 4;
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
        for (float f : value)
            bb.putFloat(f);
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}