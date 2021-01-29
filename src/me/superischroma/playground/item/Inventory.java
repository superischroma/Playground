package me.superischroma.playground.item;

import me.superischroma.playground.Playground;

import java.util.Arrays;
import java.util.Iterator;

public class Inventory implements Iterable<ItemStack>
{
    private final String name;
    private final ItemStack[] items;
    private final int size;

    public Inventory(String name, int size)
    {
        this.name = name;
        this.items = new ItemStack[size];
        this.size = size;
    }

    public String getName()
    {
        return name;
    }

    public ItemStack getItem(int slot)
    {
        return items[slot];
    }

    public void setItem(int slot, ItemStack stack)
    {
        items[slot] = stack;
    }

    public boolean contains(Material material)
    {
        for (ItemStack stack : items)
        {
            if (stack.getMaterial() == material)
                return true;
        }
        return false;
    }

    public boolean contains(ItemStack stack)
    {
        for (ItemStack s : items)
        {
            if (stack.equals(s))
                return true;
        }
        return false;
    }

    private int getStackSlot(ItemStack stack)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].equals(stack))
                return i;
        }
        return -1;
    }

    public boolean isFull()
    {
        for (ItemStack stack : items)
        {
            if (stack == null)
                return false;
        }
        return true;
    }

    public boolean isEmpty()
    {
        for (ItemStack stack : items)
        {
            if (stack != null)
                return false;
        }
        return true;
    }

    public int nextEmpty()
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] == null)
                return i;
        }
        return -1;
    }

    public int size()
    {
        return size;
    }

    @Override
    public Iterator<ItemStack> iterator()
    {
        return Arrays.asList(items).iterator();
    }

    public String toJSON()
    {
        return Playground.GSON.toJson(this, Inventory.class);
    }

    public static Inventory of(String json)
    {
        return Playground.GSON.fromJson(json, Inventory.class);
    }
}