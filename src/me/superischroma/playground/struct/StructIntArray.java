package me.superischroma.playground.struct;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class StructIntArray implements Struct<int[]>
{
    public static final String NAME = "Struct_Int_Array";
    public static final byte TYPE = 10;

    private int[] value;

    public StructIntArray(int[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(int[] value)
    {
        this.value = value;
    }

    @Override
    public int[] getValue()
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
        for (int i : value)
            bb.putInt(i);
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}