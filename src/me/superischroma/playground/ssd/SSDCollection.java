package me.superischroma.playground.ssd;

import me.superischroma.playground.ssd.array.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SSDCollection implements SSD<Map<String, SSD>>
{
    public static final String NAME = "SSD_Collection";
    public static final byte TYPE = 7;

    private String name;
    private final Map<String, SSD> map;

    public SSDCollection(String name)
    {
        this.name = name;
        this.map = new HashMap<>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void set(String k, SSD v)
    {
        map.put(k, v);
    }

    public void setBoolean(String k, boolean v)
    {
        map.put(k, new SSDByte((byte) (v ? 1 : 0)));
    }

    public void setByte(String k, byte v)
    {
        map.put(k, new SSDByte(v));
    }

    public void setShort(String k, short v)
    {
        map.put(k, new SSDShort(v));
    }

    public void setInt(String k, int v)
    {
        map.put(k, new SSDInt(v));
    }

    public void setLong(String k, long v)
    {
        map.put(k, new SSDLong(v));
    }

    public void setFloat(String k, float v)
    {
        map.put(k, new SSDFloat(v));
    }

    public void setDouble(String k, double v)
    {
        map.put(k, new SSDDouble(v));
    }

    public void setCollection(SSDCollection collection)
    {
        map.put(collection.getName(), collection);
    }

    public void setString(String k, String v)
    {
        map.put(k, new SSDString(v));
    }

    public void setByteArray(String k, byte[] v)
    {
        map.put(k, new SSDByteArray(v));
    }

    public void setShortArray(String k, short[] v)
    {
        map.put(k, new SSDShortArray(v));
    }

    public void setIntArray(String k, int[] v)
    {
        map.put(k, new SSDIntArray(v));
    }

    public void setLongArray(String k, long[] v)
    {
        map.put(k, new SSDLongArray(v));
    }

    public void setFloatArray(String k, float[] v)
    {
        map.put(k, new SSDFloatArray(v));
    }

    public void setDoubleArray(String k, double[] v)
    {
        map.put(k, new SSDDoubleArray(v));
    }

    public void setStringArray(String k, String[] v)
    {
        map.put(k, new SSDStringArray(v));
    }

    public Set<String> keys()
    {
        return map.keySet();
    }

    public SSD get(String k)
    {
        return map.get(k);
    }

    public boolean getBoolean(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDByte)
            return ((SSDByte) d).getValue() > 0;
        return false;
    }

    public byte getByte(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDByte)
            return ((SSDByte) d).getValue();
        return 0;
    }

    public short getShort(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDShort)
            return ((SSDShort) d).getValue();
        return 0;
    }

    public int getInt(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDInt)
            return ((SSDInt) d).getValue();
        return 0;
    }

    public long getLong(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDLong)
            return ((SSDLong) d).getValue();
        return 0;
    }

    public float getFloat(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDFloat)
            return ((SSDFloat) d).getValue();
        return 0;
    }

    public double getDouble(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDDouble)
            return ((SSDDouble) d).getValue();
        return 0;
    }

    public SSDCollection getCollection(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDCollection)
            return ((SSDCollection) d);
        return new SSDCollection(name);
    }

    public String getString(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDString)
            return ((SSDString) d).getValue();
        return null;
    }

    public byte[] getByteArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDByteArray)
            return ((SSDByteArray) d).getValue();
        return null;
    }

    public short[] getShortArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDShortArray)
            return ((SSDShortArray) d).getValue();
        return null;
    }

    public int[] getIntArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDIntArray)
            return ((SSDIntArray) d).getValue();
        return null;
    }

    public long[] getLongArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDLongArray)
            return ((SSDLongArray) d).getValue();
        return null;
    }

    public float[] getFloatArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDFloatArray)
            return ((SSDFloatArray) d).getValue();
        return null;
    }

    public double[] getDoubleArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDDoubleArray)
            return ((SSDDoubleArray) d).getValue();
        return null;
    }

    public String[] getStringArray(String k)
    {
        SSD d = map.get(k);
        if (d instanceof SSDStringArray)
            return ((SSDStringArray) d).getValue();
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
        for (Map.Entry<String, SSD> entry : map.entrySet())
        {
            SSD ssd = entry.getValue();
            if (ssd instanceof SSDCollection) // we get it early here because collection lengths account for everything
            {
                size += ssd.length();
                continue;
            }
            // 1 for type, 2 for key name length
            if (ssd.getValue() == null)
                continue;
            size += 3;
            size += entry.getKey().length();
            if (ssd.usesSpecialLength()) // string: 2 for value length; array: 2 for array length
                size += 2;
            size += ssd.length();
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
            bytes[++tracker] = nb;
        for (Map.Entry<String, SSD> entry : map.entrySet())
        {
            String k = entry.getKey();
            if (k.length() > Short.MAX_VALUE)
            {
                System.err.println("Key of Data Length > 32767!");
                return null;
            }
            SSD ssd = entry.getValue();
            if (ssd.getValue() == null) continue;
            tracker++;
            bytes[tracker] = ssd.getType();
            if (ssd instanceof SSDCollection)
            {
                tracker--;
                for (byte vb : ssd.asByteArray())
                    bytes[++tracker] = vb;
                continue;
            }
            ByteBuffer kbb = ByteBuffer.allocate(2).putShort((short) k.length());
            bytes[++tracker] = kbb.get(0);
            bytes[++tracker] = kbb.get(1);
            byte[] kbs = k.getBytes();
            for (byte kb : kbs)
                bytes[++tracker] = kb;
            if (ssd instanceof SSDString) // the length of the string
            {
                String v = ((SSDString) ssd).getValue();
                ByteBuffer vbb = ByteBuffer.allocate(2).putShort((short) v.length());
                bytes[++tracker] = vbb.get(0);
                bytes[++tracker] = vbb.get(1);
            }
            if (ssd instanceof SSDByteArray)
            {
                byte[] bs = ((SSDByteArray) ssd).getValue();
                ByteBuffer bbb = ByteBuffer.allocate(2).putShort((short) bs.length);
                bytes[++tracker] = bbb.get(0);
                bytes[++tracker] = bbb.get(1);
            }
            if (ssd instanceof SSDShortArray)
            {
                short[] ss = ((SSDShortArray) ssd).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) ss.length);
                bytes[++tracker] = lbb.get(0);
                bytes[++tracker] = lbb.get(1);
            }
            if (ssd instanceof SSDIntArray)
            {
                int[] is = ((SSDIntArray) ssd).getValue();
                ByteBuffer ibb = ByteBuffer.allocate(2).putShort((short) is.length);
                bytes[++tracker] = ibb.get(0);
                bytes[++tracker] = ibb.get(1);
            }
            if (ssd instanceof SSDLongArray)
            {
                long[] ls = ((SSDLongArray) ssd).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) ls.length);
                bytes[++tracker] = lbb.get(0);
                bytes[++tracker] = lbb.get(1);
            }
            if (ssd instanceof SSDFloatArray)
            {
                float[] fs = ((SSDFloatArray) ssd).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) fs.length);
                bytes[++tracker] = lbb.get(0);
                bytes[++tracker] = lbb.get(1);
            }
            if (ssd instanceof SSDDoubleArray)
            {
                double[] ds = ((SSDDoubleArray) ssd).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) ds.length);
                bytes[++tracker] = lbb.get(0);
                bytes[++tracker] = lbb.get(1);
            }
            if (ssd instanceof SSDStringArray)
            {
                String[] ds = ((SSDStringArray) ssd).getValue();
                ByteBuffer lbb = ByteBuffer.allocate(2).putShort((short) ds.length);
                bytes[++tracker] = lbb.get(0);
                bytes[++tracker] = lbb.get(1);
            }
            for (byte vb : ssd.asByteArray())
                bytes[++tracker] = vb;
        }
        bytes[++tracker] = new SSDEnd().getValue();
        return bytes;
    }

    /**
     * Unused.
     */
    @Override
    public void setValue(Map<String, SSD> value) {}

    @Override
    public Map<String, SSD> getValue()
    {
        return map;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder()
                .append(name + ":{");
        boolean c = false;
        for (Map.Entry<String, SSD> entry : map.entrySet())
        {
            if (!c)
                c = true;
            else
                builder.append(",");
            String key = entry.getKey();
            SSD ssd = entry.getValue();
            if (!(ssd instanceof SSDCollection))
                builder.append(key).append(":");
            builder.append(ssd.toString());
        }
        builder.append("}");
        return builder.toString();
    }
}
