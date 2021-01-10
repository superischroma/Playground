package me.superischroma.playground.storage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple key-value mapping extension
 * for {@link SimpleTextFile}.
 * In order to write to disk, you MUST use
 * {@link MappedTextFile#save()}. Changes
 * are stashed in a {@link LinkedHashMap}
 * and will not be updated until saved.
 * Objects stored as values will attempt
 * to convert to strings. If your object is
 * not already in a string-like format, it
 * will save as the object's hashcode.
 */
public class MappedTextFile extends SimpleTextFile
{
    private final LinkedHashMap<String, Object> map;

    public MappedTextFile(String path)
    {
        super(path);
        this.map = new LinkedHashMap<>();
        for (String line : getLines())
        {
            String[] spl = line.split("=");
            if (spl.length <= 1)
                continue;
            this.map.put(spl[0], spl[1]);
        }
    }

    public void set(String k, Object v)
    {
        map.remove(k);
        map.put(k, v);
    }

    public void remove(String k)
    {
        map.remove(k);
    }

    public Object get(String k)
    {
        return map.get(k);
    }

    public void save()
    {
        wipe();
        for (Map.Entry<String, Object> entry : map.entrySet())
            println(entry.getKey() + "=" + entry.getValue().toString());
    }
}