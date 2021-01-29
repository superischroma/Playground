package me.superischroma.playground.item;

public enum Rarity
{
    COMMON(0xFFFFFF),
    UNCOMMON(0x55FF55),
    RARE(0x5555FF),
    EPIC(0xAA00AA),
    LEGENDARY(0xFFAA00);

    private final int rgb;

    Rarity(int rgb)
    {
        this.rgb = rgb;
    }

    public int getRGB()
    {
        return rgb;
    }
}