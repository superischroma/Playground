package me.superischroma.playground.struct;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StructCollection implements Struct<Map<String, Struct>>
{
    public static final String NAME = "Struct_Collection";
    public static final byte TYPE = 7;

    private String name;
    private final Map<String, Struct> map;

    public StructCollection(String name)
    {
        this.name = name;
        this.map = new HashMap<>();
    }

    public String getName()
    {
        return name;
    }

    public void set(String k, Struct v)
    {
        map.put(k, v);
    }

    public void setByte(String k, byte v)
    {
        map.put(k, new StructByte(v));
    }

    public void setShort(String k, short v)
    {
        map.put(k, new StructShort(v));
    }

    public void setInt(String k, int v)
    {
        map.put(k, new StructInt(v));
    }

    public void setLong(String k, long v)
    {
        map.put(k, new StructLong(v));
    }

    public void setFloat(String k, float v)
    {
        map.put(k, new StructFloat(v));
    }

    public void setDouble(String k, double v)
    {
        map.put(k, new StructDouble(v));
    }

    public void setCollection(StructCollection collection)
    {
        map.put(collection.getName(), collection);
    }

    public void setString(String k, String v)
    {
        map.put(k, new StructString(v));
    }

    public void setByteArray(String k, byte[] v)
    {
        map.put(k, new StructByteArray(v));
    }

    public void setIntArray(String k, int[] v)
    {
        map.put(k, new StructIntArray(v));
    }

    public void setLongArray(String k, long[] v)
    {
        map.put(k, new StructLongArray(v));
    }

    public Set<String> keys()
    {
        return map.keySet();
    }

    public Struct get(String k)
    {
        return map.get(k);
    }

    public byte getByte(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructByte)
            return ((StructByte) d).getValue();
        return 0;
    }

    public short getShort(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructShort)
            return ((StructShort) d).getValue();
        return 0;
    }

    public int getInt(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructInt)
            return ((StructInt) d).getValue();
        return 0;
    }

    public long getLong(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructLong)
            return ((StructLong) d).getValue();
        return 0;
    }

    public float getFloat(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructFloat)
            return ((StructFloat) d).getValue();
        return 0;
    }

    public double getDouble(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructDouble)
            return ((StructDouble) d).getValue();
        return 0;
    }

    public StructCollection getCollection(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructCollection)
            return ((StructCollection) d);
        return new StructCollection(name);
    }

    public String getString(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructString)
            return ((StructString) d).getValue();
        return null;
    }

    public byte[] getByteArray(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructByteArray)
            return ((StructByteArray) d).getValue();
        return null;
    }

    public int[] getIntArray(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructIntArray)
            return ((StructIntArray) d).getValue();
        return null;
    }

    public long[] getLongArray(String k)
    {
        Struct d = map.get(k);
        if (d instanceof StructLongArray)
            return ((StructLongArray) d).getValue();
        return null;
    }

    public void remove(String k)
    {
        map.remove(k);
    }

    @Override
    public byte getType()
    {
        return TYPE;
    }

    public int length()
    {
        // 1 for type (collection), 2 for length of collection name, 1 for end
        int size = 4;
        size += name.length(); // the name size for the name
        for (Map.Entry<String, Struct> entry : map.entrySet())
        {
            Struct struct = entry.getValue();
            if (struct instanceof StructCollection) // we get it early here because collection lengths account for everything
            {
                size += struct.length();
                continue;
            }
            // 1 for type, 2 for key name length
            size += 3;
            if (struct.getValue() == null)
                continue;
            size += entry.getKey().length();
            if (struct instanceof StructString ||
                    struct instanceof StructByteArray ||
                    struct instanceof StructIntArray ||
                    struct instanceof StructLongArray) // string: 2 for value length; array: 2 for array length
                size += 2;
            size += struct.length();
        }
        return size;
    }

    public byte[] asByteArray()
    {
        byte[] bytes = new byte[length()];
        bytes[0] = getType();
        if (name == null)
        {
            System.err.println("Name of Structured Collection cannot be null!");
            return null;
        }
        if (name.length() > Short.MAX_VALUE)
        {
            System.err.println("Name of Structured Collection Length > 32767!");
            return null;
        }
        ByteBuffer nbb = ByteBuffer.allocate(2).putShort((short) name.length());
        bytes[1] = nbb.get(0);
        bytes[2] = nbb.get(1);
        byte[] nbs = name.getBytes();
        int tracker = 2;
        for (byte nb : nbs)
        {
            tracker++;
            bytes[tracker] = nb;
        }
        for (Map.Entry<String, Struct> entry : map.entrySet())
        {
            tracker++;
            String k = entry.getKey();
            if (k.length() > Short.MAX_VALUE)
            {
                System.err.println("Key of Data Length > 32767!");
                return null;
            }
            Struct struct = entry.getValue();
            bytes[tracker] = struct.getType();
            if (struct instanceof StructCollection)
            {
                tracker--;
                for (byte vb : struct.asByteArray())
                {
                    tracker++;
                    bytes[tracker] = vb;
                }
                continue;
            }
            ByteBuffer kbb = ByteBuffer.allocate(2).putShort((short) k.length());
            tracker++;
            bytes[tracker] = kbb.get(0);
            tracker++;
            bytes[tracker] = kbb.get(1);
            byte[] kbs = k.getBytes();
            for (byte kb : kbs)
            {
                tracker++;
                bytes[tracker] = kb;
            }
            if (struct.getValue() == null) continue;
            if (struct instanceof StructString) // the length of the string
            {
                String v = ((StructString) struct).getValue();
                ByteBuffer vbb = ByteBuffer.allocate(2).putShort((short) v.length());
                tracker++;
                bytes[tracker] = vbb.get(0);
                tracker++;
                bytes[tracker] = vbb.get(1);
            }
            if (struct instanceof StructByteArray)
            {
                byte[] bs = ((StructByteArray) struct).getValue();
                ByteBuffer bbb = ByteBuffer.allocate(2).putShort((short) bs.length);
                tracker++;
                bytes[tracker] = bbb.get(0);
                tracker++;
                bytes[tracker] = bbb.get(1);
            }
            if (struct instanceof StructIntArray)
            {
                int[] is = ((StructIntArray) struct).getValue();
                ByteBuffer ibb = ByteBuffer.allocate(2).putShort((short) is.length);
                tracker++;
                bytes[tracker] = ibb.get(0);
                tracker++;
                bytes[tracker] = ibb.get(1);
            }
            if (struct instanceof StructLongArray)
            {
                long[] is = ((StructLongArray) struct).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) is.length);
                tracker++;
                bytes[tracker] = lbb.get(0);
                tracker++;
                bytes[tracker] = lbb.get(1);
            }
            for (byte vb : struct.asByteArray())
            {
                tracker++;
                bytes[tracker] = vb;
            }
        }
        tracker++;
        bytes[tracker] = new StructEnd().getValue();
        return bytes;
    }

    /**
     * Unused.
     */
    @Override
    public void setValue(Map<String, Struct> value) {}

    @Override
    public Map<String, Struct> getValue()
    {
        return map;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder()
                .append(name + ":{");
        boolean c = false;
        for (Map.Entry<String, Struct> entry : map.entrySet())
        {
            if (!c)
                c = true;
            else
                builder.append(",");
            String key = entry.getKey();
            Struct struct = entry.getValue();
            if (!(struct instanceof StructCollection))
                builder.append(key).append(":");
            builder.append(struct.toString());
        }
        builder.append("}");
        return builder.toString();
    }
}
