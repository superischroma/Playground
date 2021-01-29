package me.superischroma.playground.ssd;

public class SSDEnd implements SSD<Byte>
{
    public static final String NAME = "SSD_End";
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
