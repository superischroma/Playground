package me.superischroma.playground.item;

import java.util.List;

public interface Item
{
    String getName();
    Rarity getDefaultRarity();
    default boolean isStackable()
    {
        return true;
    }
    default List<String> getLore()
    {
        return null;
    }
}