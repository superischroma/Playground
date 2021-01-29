package me.superischroma.playground.ssd;

public class SSDByte implements SSD<Byte>
{
    public static final String NAME = "SSD_Byte";
    public static final byte TYPE = 1;

    private byte value;

    public SSDByte(byte value)
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