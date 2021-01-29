package me.superischroma.playground.struct;

public class StructString implements Struct<String>
{
    public static final String NAME = "Struct_String";
    public static final byte TYPE = 8;

    private String value;

    public StructString(String value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return value.length();
    }

    @Override
    public boolean usesSpecialLength()
    {
        return true;
    }

    @Override
    public byte[] asByteArray()
    {
        return value.getBytes();
    }

    public String toString()
    {
        return value;
    }
}