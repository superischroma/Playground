package me.superischroma.playground.struct;

public class StructByte implements Struct<Byte>
{
    public static final String NAME = "Struct_Byte";
    public static final byte TYPE = 1;

    private byte value;

    public StructByte(byte value)
    {
        this.value = value;
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    @Override
    public void setValue(Byte value)
    {
        this.value = value;
    }

    @Override
    public Byte getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return 1;
    }

    @Override
    public byte[] asByteArray()
    {
        return new byte[]{value};
    }

    public String toString()
    {
        return String.valueOf(value);
    }
}