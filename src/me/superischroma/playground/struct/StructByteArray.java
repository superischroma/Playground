package me.superischroma.playground.struct;

import java.util.Arrays;

public class StructByteArray implements Struct<byte[]>
{
    public static final String NAME = "Struct_Byte_Array";
    public static final byte TYPE = 9;

    private byte[] value;

    public StructByteArray(byte[] value)
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