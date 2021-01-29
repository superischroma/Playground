package me.superischroma.playground.struct;

public class StructEnd implements Struct<Byte>
{
    public static final String NAME = "Struct_End";
    public static final byte TYPE = 0;

    @Override
    public byte getType()
    {
        return 0;
    }

    @Override
    public Byte getValue()
    {
        return TYPE;
    }

    /**
     * Unused.
     */
    @Override
    public void setValue(Byte value) {}

    @Override
    public int length()
    {
        return 0;
    }

    @Override
    public byte[] asByteArray()
    {
        return new byte[]{0};
    }
}
