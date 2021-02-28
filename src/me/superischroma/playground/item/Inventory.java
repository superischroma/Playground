package me.superischroma.playground.item;

import me.superischroma.playground.ssd.SSD;
import me.superischroma.playground.ssd.SSDCollection;
import me.superischroma.playground.ssd.SSDIO;

import java.io.File;
import java.io.IOException;
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

    public void save(File file)
    {
        try
        {
            file.createNewFile();
            SSDCollection collection = new SSDCollection("Inventory");
            collection.setString("Name", name);
            collection.setInt("Size", size);
            for (int i = 0; i < items.length; i++)
            {
                ItemStack stack = items[i];
                ItemMeta meta = stack.getItemMeta();
                SSDCollection slot = new SSDCollection(String.valueOf(i));
                slot.setString("Type", stack.getMaterial().name());
                slot.setInt("Amount", stack.getAmount());
                SSDCollection metaCollection = new SSDCollection("Meta");
                metaCollection.setString("DisplayName", meta.getDisplayName());
                if (meta.getRarity() != null)
                    metaCollection.setString("Rarity", meta.getRarity().name());
                slot.setCollection(metaCollection);
                collection.setCollection(slot);
            }
            SSDIO.write(file, collection);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static Inventory of(File file)
    {
        if (!SSD.isSSDFile(file))
            return null;
        try
        {
            SSDCollection collection = SSDIO.read(file);
            if (!collection.getName().equals("Inventory"))
                return null;
            Inventory inventory = new Inventory(collection.getString("Name"), collection.getInt("Size"));
            for (String key : collection.keys())
            {
                try
                {
                    int i = Integer.parseInt(key);
                    SSDCollection slot = collection.getCollection(key);
                    ItemStack stack = new ItemStack(Material.valueOf(slot.getString("Type")), slot.getInt("Amount"));
                    SSDCollection metaCollection = slot.getCollection("Meta");
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(metaCollection.getString("DisplayName"));
                    if (metaCollection.getString("Rarity") != null)
                        meta.setRarity(Rarity.valueOf(metaCollection.getString("Rarity")));
                    inventory.setItem(i, stack);
                }
                catch (NumberFormatException ignored) {}
            }
            return inventory;
        }
        catch (IOException ex)
        {
            return null;
        }
    }
}