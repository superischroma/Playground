package me.superischroma.playground.item;

public class Stick implements Item
{
    @Override
    public String getName()
    {
        return "Stick";
    }

    @Override
    public Rarity getDefaultRarity()
    {
        return Rarity.COMMON;
    }
}
