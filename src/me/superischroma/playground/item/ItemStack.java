package me.superischroma.playground.item;

import me.superischroma.playground.Playground;

import java.util.Objects;

public class ItemStack
{
    private Material material;
    private int amount;
    private ItemMeta meta;

    public ItemStack(Material material, int amount)
    {
        this.material = material;
        this.amount = amount;
        this.meta = new ItemMeta();
    }

    public ItemStack(int id, int amount)
    {
        this(Objects.requireNonNull(Material.getByID(id)), amount);
    }

    public ItemStack(Material material)
    {
        this(material, 1);
    }

    public ItemStack(int id)
    {
        this(id, 1);
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public ItemMeta getItemMeta()
    {
        return meta;
    }

    public void setItemMeta(ItemMeta meta)
    {
        this.meta = meta;
    }

    public String toJSON()
    {
        return Playground.GSON.toJson(this, ItemStack.class);
    }

    public static ItemStack of(String json)
    {
        return Playground.GSON.fromJson(json, ItemStack.class);
    }

    public boolean isSimilar(ItemStack stack)
    {
        return stack.material == this.material && stack.amount == this.amount;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof ItemStack)) return false;
        ItemStack stack = (ItemStack) o;
        return stack.material == this.material && stack.amount == this.amount && this.meta.equals(stack.meta);
    }
}