package me.superischroma.playground.ssd.array;

import me.superischroma.playground.ssd.SSD;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SSDStringArray implements SSD<String[]>
{
    public static final String NAME = "SSD_String_Array";
    public static final byte TYPE = 15;

    private String[] value;

    public SSDStringArray(String[] value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(String[] value)
    {
        this.value = value;
    }

    @Override
    public String[] getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        int l = 0;
        for (String s : value)
            l += s.length() + 2;
        return l;
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
        for (String s : value)
        {
            ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) s.length());
            bb.put(lbb.get(0));
            bb.put(lbb.get(1));
            bb.put(s.getBytes());
        }
        return bb.array();
    }

    public String toString()
    {
        return Arrays.toString(value);
    }
}