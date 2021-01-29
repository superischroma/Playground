package me.superischroma.playground.item;

import me.superischroma.playground.Playground;

public class ItemMeta
{
    private String displayName;
    private Rarity rarity;

    public ItemMeta()
    {
        this.displayName = null;
        this.rarity = null;
    }

    public ItemMeta(String displayName, Rarity rarity)
    {
        this.displayName = displayName;
        this.rarity = rarity;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public Rarity getRarity()
    {
        return rarity;
    }

    public void setRarity(Rarity rarity)
    {
        this.rarity = rarity;
    }

    public String toJSON()
    {
        return Playground.GSON.toJson(this, ItemMeta.class);
    }

    public static ItemMeta of(String json)
    {
        return Playground.GSON.fromJson(json, ItemMeta.class);
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof ItemMeta)) return false;
        ItemMeta meta = (ItemMeta) o;
        return this.rarity == meta.rarity && this.displayName.equals(meta.displayName);
    }
}