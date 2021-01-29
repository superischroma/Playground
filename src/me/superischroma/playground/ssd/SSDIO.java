package me.superischroma.playground.ssd;

import me.superischroma.playground.ssd.array.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static methods to read and write Structured Storage Data.
 */
public final class SSDIO
{
    public static SSDCollection read(byte[] bytes)
    {
        SSDCollection parent = null;
        List<SSDCollection> collections = new ArrayList<>();
        int tracker = -1;
        while (tracker < bytes.length - 1)
        {
            SSD ssd = SSD.create(bytes[++tracker]);
            if (ssd instanceof SSDEnd)
            {
                SSDCollection c = !collections.isEmpty() ? collections.get(collections.size() - 1) : parent;
                if (!c.equals(parent))
                {
                    (collections.size() < 2 ? parent : collections.get(collections.size() - 2)).set(c.getName(), c);
                    collections.remove(collections.size() - 1);
                }
                continue;
            }
            short nl = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
            byte[] nbs = new byte[nl];
            for (int i = 0; i < nl; i++)
                nbs[i] = bytes[++tracker];
            String n = new String(nbs);
            if (ssd instanceof SSDCollection)
            {
                SSDCollection c = new SSDCollection(n);
                if (collections.stream().filter((collection) -> collection.getName().equals(c.getName())).toArray().length != 0)
                    continue;
                if (parent == null)
                    parent = c;
                else
                    collections.add(c);
                continue;
            }
            Object val = null;
            int length = ssd.usesSpecialLength() ? 0 : ssd.length();
            if (ssd instanceof SSDByte)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).get();
            if (ssd instanceof SSDShort)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getShort();
            if (ssd instanceof SSDInt)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getInt();
            if (ssd instanceof SSDLong)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getLong();
            if (ssd instanceof SSDFloat)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getFloat();
            if (ssd instanceof SSDDouble)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getDouble();
            if (ssd instanceof SSDString)
            {
                short strl = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                val = new String(Arrays.copyOfRange(bytes, ++tracker, tracker += strl));
            }
            if (ssd instanceof SSDByteArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                val = Arrays.copyOfRange(bytes, ++tracker, tracker += al);
            }
            if (ssd instanceof SSDShortArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                short[] ss = new short[al];
                for (int i = 0; i < al; i++)
                {
                    ss[i] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += 2)).getShort();
                    tracker--;
                }
                tracker++;
                val = ss;
            }
            if (ssd instanceof SSDIntArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                int[] is = new int[al];
                for (int i = 0; i < al; i++)
                {
                    is[i] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += 4)).getInt();
                    tracker--;
                }
                tracker++;
                val = is;
            }
            if (ssd instanceof SSDLongArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                long[] ls = new long[al];
                for (int i = 0; i < al; i++)
                {
                    ls[i] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += 8)).getLong();
                    tracker--;
                }
                tracker++;
                val = ls;
            }
            if (ssd instanceof SSDFloatArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                float[] fs = new float[al];
                for (int i = 0; i < al; i++)
                {
                    fs[i] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += 4)).getFloat();
                    tracker--;
                }
                tracker++;
                val = fs;
            }
            if (ssd instanceof SSDDoubleArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                double[] ds = new double[al];
                for (int i = 0; i < al; i++)
                {
                    ds[i] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += 8)).getDouble();
                    tracker--;
                }
                tracker++;
                val = ds;
            }
            if (ssd instanceof SSDStringArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                String[] ss = new String[al];
                for (int i = 0; i < al; i++)
                {
                    short l = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                    ss[i] = new String(Arrays.copyOfRange(bytes, ++tracker, tracker += l));
                    tracker--;
                }
                tracker++;
                val = ss;
            }
            tracker--;
            SSD so = SSD.create(val);
            if (collections.size() != 0)
                collections.get(collections.size() - 1).set(n, so);
            else
                parent.set(n, so);
        }
        return parent;
    }

    public static SSDCollection read(File file) throws IOException
    {
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return read(buffer);
    }

    public static void write(File file, SSDCollection collection) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        out.write(collection.asByteArray());
        out.flush();
        out.close();
    }
}