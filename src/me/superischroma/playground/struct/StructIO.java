package me.superischroma.playground.struct;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static methods to read and write Structured Storage Data.
 */
public final class StructIO
{
    public static StructCollection read(byte[] bytes)
    {
        StructCollection parent = null;
        List<StructCollection> collections = new ArrayList<>();
        int tracker = -1;
        while (tracker < bytes.length - 1)
        {
            Struct struct = Struct.create(bytes[++tracker]);
            if (struct instanceof StructEnd)
            {
                StructCollection c = !collections.isEmpty() ? collections.get(collections.size() - 1) : parent;
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
            if (struct instanceof StructCollection)
            {
                StructCollection c = new StructCollection(n);
                if (collections.stream().filter((collection) -> collection.getName().equals(c.getName())).toArray().length != 0)
                    continue;
                if (parent == null)
                    parent = c;
                else
                    collections.add(c);
                continue;
            }
            Object val = null;
            int length = struct.usesSpecialLength() ? 0 : struct.length();
            if (struct instanceof StructByte)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).get();
            if (struct instanceof StructShort)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getShort();
            if (struct instanceof StructInt)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getInt();
            if (struct instanceof StructLong)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getLong();
            if (struct instanceof StructFloat)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getFloat();
            if (struct instanceof StructDouble)
                val = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ++tracker, tracker += length)).getDouble();
            if (struct instanceof StructString)
            {
                short strl = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                val = new String(Arrays.copyOfRange(bytes, ++tracker, tracker += strl));
            }
            if (struct instanceof StructByteArray)
            {
                short al = ByteBuffer.wrap(new byte[]{bytes[++tracker], bytes[++tracker]}).getShort();
                val = Arrays.copyOfRange(bytes, ++tracker, tracker += al);
            }
            if (struct instanceof StructIntArray)
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
            if (struct instanceof StructLongArray)
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
            tracker--;
            Struct so = Struct.create(val);
            if (collections.size() != 0)
                collections.get(collections.size() - 1).set(n, so);
            else
                parent.set(n, so);
        }
        return parent;
    }

    public static StructCollection read(File file) throws IOException
    {
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return read(buffer);
    }

    public static void write(File file, StructCollection collection) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        out.write(collection.asByteArray());
        out.flush();
        out.close();
    }
}