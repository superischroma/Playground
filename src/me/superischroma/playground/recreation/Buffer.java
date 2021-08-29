package me.superischroma.playground.recreation;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility to read and write binary data from a byte buffer.
 *
 * There are two settings for Buffer: Fixed and Dynamic
 *
 * Fixed Buffers will not resize themselves if data being
 * modified is out of range. The only way to resize a
 * Fixed Buffer is by using the {@link #length(int)} method.
 *
 * Dynamic Buffers will automatically resize themselves if
 * data being modified cannot fit in the current underlying
 * buffer. The size of the Dynamic Buffer can still be
 * manually modified by using the {@link #length(int)} method.
 *
 * {@link String}s are encoded in a specific way to guarantee
 * the correct {@link Charset} will be used when
 * decoding it.
 * String encoding goes as follows: {@code <length (int)> <charset (byte)> <string (byte[])>}
 *
 * Objects can be serialized into binary data by extending
 * the {@link BufferSerializable} interface. Use methods
 * {@link #writeObject(BufferSerializable)} and
 * {@link #readObject(Class)} to convert data.
 *
 * Arrays and {@link java.util.Collection}s support Object serialization and
 * are encoded as such: {@code <length (int)> <elements (byte[])>}
 * Currently, Arrays and Collections only support the UTF-8
 * charset for Strings. This may be changed in the future.
 *
 * The effective length of a Buffer is dependent on the state
 * of the Buffer. If it is fixed, then the effective length
 * will be its length. Otherwise, it is the amount of
 * significant bytes in the Buffer. The significant bytes
 * of the Buffer are all bytes that come before the
 * highest indexed byte that was modified, and the
 * highest indexed byte.
 *
 * For example, if you have a fixed Buffer that contains the
 * data [00 00 00 05 00 00], then the effective length
 * of that Buffer will be 4.
 *
 * But also, if you have another fixed Buffer that contains the
 * data [00 00 00 FF 00 00], and if [FF 00] was inserted as a
 * short, then the effective length will be 5.
 *
 * If you're wrapping binary data you already have, you can use
 * one of the constructors with a {@code significant} parameter
 * and describe how much of your binary data is significant.
 */
public class Buffer
{
    private static final Charset[] AVAILABLE_CHARSETS = {
            StandardCharsets.UTF_8,
            StandardCharsets.UTF_16,
            StandardCharsets.US_ASCII,
            StandardCharsets.ISO_8859_1
    };

    private byte[] data;
    private int position;
    private int lastIndex;
    private boolean fixed;

    private Buffer(byte[] data, boolean fixed, int significant, int position)
    {
        this.data = data;
        this.fixed = fixed;
        this.lastIndex = significant - 1;
        this.position = position;
    }

    public Buffer(byte[] data, boolean fixed, int significant)
    {
        this(data, fixed, significant, 0);
    }

    public Buffer(byte[] data, boolean fixed)
    {
        this(data, fixed, data.length, 0);
    }

    public Buffer(byte[] data)
    {
        this(data, true, data.length, 0);
    }

    public Buffer(int alloc, boolean fixed)
    {
        this.data = new byte[alloc];
        this.fixed = fixed;
        this.lastIndex = -1;
        this.position = 0;
    }

    public Buffer(int alloc)
    {
        this(alloc, false);
    }

    public Buffer()
    {
        this(1024, false);
    }

    public Buffer(Buffer buffer)
    {
        this.data = buffer.data.clone();
        this.fixed = buffer.fixed;
        this.lastIndex = buffer.lastIndex;
        this.position = buffer.position;
    }

    public int write(int index, byte b)
    {
        grow(index, Byte.BYTES);
        this.data[index++] = b;
        checkLastIndex(index - 1);
        return Byte.BYTES;
    }

    public int write(byte b)
    {
        grow(Byte.BYTES);
        this.data[position++] = b;
        checkLastIndex(position - 1);
        return Byte.BYTES;
    }

    public byte read(int index)
    {
        return this.data[index];
    }

    public byte read()
    {
        return this.data[position++];
    }

    /**
     * Writes out an unsigned byte to the Buffer
     * at the specified index.
     * @param index Index in underlying buffer
     * @param ub An unsigned byte
     * @return Number of bytes needed to store this value (1)
     * @throws IllegalArgumentException If unsigned byte is out of range (0-255)
     */
    public int writeU(int index, int ub)
    {
        if (ub < 0 || ub > 255)
            throw new IllegalArgumentException("unsigned byte must be within 0-255");
        if (ub > 127)
            ub -= 256;
        byte b = (byte) ub;
        return this.write(index, b);
    }

    /**
     * Writes out an unsigned byte to the Buffer
     * at the current position, and shifts the
     * position right 1 time.
     * @param ub An unsigned byte
     * @return Number of bytes needed to store this value (1)
     * @throws IllegalArgumentException If unsigned byte is out of range (0-255)
     */
    public int writeU(int ub)
    {
        if (ub < 0 || ub > 255)
            throw new IllegalArgumentException("unsigned byte must be within 0-255");
        if (ub > 127)
            ub -= 256;
        byte b = (byte) ub;
        return this.write(b);
    }

    /**
     * Reads in an unsigned byte from
     * the index provided.
     * @param index Index to start reading from
     * @return An unsigned byte (short)
     */
    public short readU(int index)
    {
        short s = this.data[index];
        if (s < 0)
            s += 256;
        return s;
    }

    /**
     * Reads in an unsigned byte from
     * the current position, and shifts
     * the position right 1 time.
     * @return An unsigned byte (short)
     */
    public short readU()
    {
        short s = this.data[position++];
        if (s < 0)
            s += 256;
        return s;
    }

    public int writeBytes(int index, byte[] bs)
    {
        grow(index, bs.length);
        for (byte b : bs)
            this.data[index++] = b;
        checkLastIndex(index - 1);
        return bs.length;
    }

    public int writeBytes(byte[] bs)
    {
        grow(bs.length);
        for (byte b : bs)
            this.data[position++] = b;
        checkLastIndex(position - 1);
        return bs.length;
    }

    public void readBytes(int index, byte[] bs)
    {
        for (int i = 0; i < bs.length; i++)
            bs[i] = this.data[index++];
    }

    public void readBytes(byte[] bs)
    {
        for (int i = 0; i < bs.length; i++)
            bs[i] = this.data[position++];
    }

    public byte[] readBytes(int index, int amount)
    {
        byte[] bs = new byte[amount];
        for (int i = 0; i < bs.length; i++)
            bs[i] = this.data[index++];
        return bs;
    }

    public byte[] readBytes(int amount)
    {
        byte[] bs = new byte[amount];
        for (int i = 0; i < bs.length; i++)
            bs[i] = this.data[position++];
        return bs;
    }

    public int writeShort(int index, short s)
    {
        grow(index, Short.BYTES);
        for (int i = 8; i >= 0; i -= 8)
            this.data[index++] = (byte) (0xFF & (s >> i));
        checkLastIndex(index - 1);
        return Short.BYTES;
    }

    public int writeShort(short s)
    {
        grow(Short.BYTES);
        for (int i = 8; i >= 0; i -= 8)
            this.data[position++] = (byte) (0xFF & (s >> i));
        checkLastIndex(position - 1);
        return Short.BYTES;
    }

    public short readShort(int index)
    {
        short s = this.data[index++]; // init as first byte
        s &= 0xFF; // remove any 1 bits not in the actual sequence
        s <<= 8; // shift to the left 8
        s |= (this.data[index] & 0xFF); // remove 1 bits from second byte and combine with first byte
        return s;
    }

    public short readShort()
    {
        short s = this.data[position++]; // init as first byte
        s &= 0xFF; // remove any 1 bits not in the actual sequence
        s <<= 8; // shift to the left 8
        s |= (this.data[position++] & 0xFF); // remove 1 bits from second byte and combine with first byte
        return s;
    }

    public int writeInt(int index, int i)
    {
        grow(index, Integer.BYTES);
        for (int j = 24; j >= 0; j -= 8)
            this.data[index++] = (byte) (0xFF & (i >> j));
        checkLastIndex(index - 1);
        return Integer.BYTES;
    }

    public int writeInt(int i)
    {
        grow(Integer.BYTES);
        for (int j = 24; j >= 0; j -= 8)
            this.data[position++] = (byte) (0xFF & (i >> j));
        checkLastIndex(position - 1);
        return Integer.BYTES;
    }

    public int readInt(int index)
    {
        int i = this.data[index++]; // init as first byte
        i &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 3; j++)
        {
            i <<= 8;
            i |= (this.data[index++] & 0xFF);
        }
        return i;
    }

    public int readInt()
    {
        int i = this.data[position++]; // init as first byte
        i &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 3; j++)
        {
            i <<= 8;
            i |= (this.data[position++] & 0xFF);
        }
        return i;
    }

    public int writeLong(int index, long l)
    {
        grow(index, Long.BYTES);
        for (int i = 56; i >= 0; i -= 8)
            this.data[index++] = (byte) (0xFF & (l >> i));
        checkLastIndex(index - 1);
        return Long.BYTES;
    }

    public int writeLong(long l)
    {
        grow(Long.BYTES);
        for (int i = 56; i >= 0; i -= 8)
            this.data[position++] = (byte) (0xFF & (l >> i));
        checkLastIndex(position - 1);
        return Long.BYTES;
    }

    public long readLong(int index)
    {
        long l = this.data[index++]; // init as first byte
        l &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int i = 0; i < 7; i++)
        {
            l <<= 8;
            l |= (this.data[index++] & 0xFF);
        }
        return l;
    }

    public long readLong()
    {
        long l = this.data[position++]; // init as first byte
        l &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int i = 0; i < 7; i++)
        {
            l <<= 8;
            l |= (this.data[position++] & 0xFF);
        }
        return l;
    }

    public int writeFloat(int index, float f)
    {
        grow(index, Float.BYTES);
        int fi = Float.floatToRawIntBits(f);
        for (int i = 24; i >= 0; i -= 8)
            this.data[index++] = (byte) (0xFF & (fi >> i));
        checkLastIndex(index - 1);
        return Float.BYTES;
    }

    public int writeFloat(float f)
    {
        grow(Float.BYTES);
        int fi = Float.floatToRawIntBits(f);
        for (int i = 24; i >= 0; i -= 8)
            this.data[position++] = (byte) (0xFF & (fi >> i));
        checkLastIndex(position - 1);
        return Float.BYTES;
    }

    public float readFloat(int index)
    {
        int i = this.data[index++]; // init as first byte
        i &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 3; j++)
        {
            i <<= 8;
            i |= (this.data[index++] & 0xFF);
        }
        return Float.intBitsToFloat(i);
    }

    public float readFloat()
    {
        int i = this.data[position++]; // init as first byte
        i &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 3; j++)
        {
            i <<= 8;
            i |= (this.data[position++] & 0xFF);
        }
        return Float.intBitsToFloat(i);
    }

    public int writeDouble(int index, double d)
    {
        grow(index, Double.BYTES);
        long dl = Double.doubleToRawLongBits(d);
        for (int i = 56; i >= 0; i -= 8)
            this.data[index++] = (byte) (0xFF & (dl >> i));
        checkLastIndex(index - 1);
        return Double.BYTES;
    }

    public int writeDouble(double d)
    {
        grow(Double.BYTES);
        long dl = Double.doubleToRawLongBits(d);
        for (int i = 56; i >= 0; i -= 8)
            this.data[position++] = (byte) (0xFF & (dl >> i));
        checkLastIndex(position - 1);
        return Double.BYTES;
    }

    public double readDouble(int index)
    {
        long l = this.data[index++]; // init as first byte
        l &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 7; j++)
        {
            l <<= 8;
            l |= (this.data[index++] & 0xFF);
        }
        return Double.longBitsToDouble(l);
    }

    public double readDouble()
    {
        long l = this.data[position++]; // init as first byte
        l &= 0xFF; // remove any 1 bits not in the actual sequence
        for (int j = 0; j < 7; j++)
        {
            l <<= 8;
            l |= (this.data[position++] & 0xFF);
        }
        return Double.longBitsToDouble(l);
    }

    public int writeString(int index, String s, Charset charset)
    {
        byte[] bytes = s.getBytes(charset);
        grow(index, 5 + bytes.length);
        byte ci = getCharsetIndex(charset);
        if (ci == -1)
            throw new IllegalArgumentException("Charset provided is not supported");
        index += this.writeInt(index, bytes.length);
        index += this.write(index, ci);
        index += this.writeBytes(index, bytes);
        checkLastIndex(index - 1);
        return 5 + bytes.length;
    }

    public int writeString(int index, String s)
    {
        return writeString(index, s, StandardCharsets.UTF_8);
    }

    public int writeString(String s, Charset charset)
    {
        byte[] bytes = s.getBytes(charset);
        grow(5 + bytes.length);
        byte ci = getCharsetIndex(charset);
        if (ci == -1)
            throw new IllegalArgumentException("Charset provided is not supported");
        this.writeInt(bytes.length);
        this.write(ci);
        this.writeBytes(bytes);
        checkLastIndex(position - 1);
        return 5 + bytes.length;
    }

    public int writeString(String s)
    {
        return writeString(s, StandardCharsets.UTF_8);
    }

    public String readString(int index)
    {
        return this.readString(new AtomicInteger(index));
    }

    public String readString(AtomicInteger index)
    {
        int blen = this.readInt(index.getAndAdd(4));
        Charset charset = AVAILABLE_CHARSETS[this.read(index.getAndAdd(1))];
        byte[] bs = this.readBytes(index.get(), blen);
        index.getAndAdd(bs.length);
        return new String(bs, charset);
    }

    public String readString()
    {
        int blen = this.readInt();
        Charset charset = AVAILABLE_CHARSETS[this.read()];
        byte[] bs = this.readBytes(blen);
        return new String(bs, charset);
    }

    public int writeArray(Object[] arr)
    {
        grow(calcArrBytes(arr));
        int len = 0;
        len += this.writeInt(arr.length);
        for (Object el : arr)
        {
            if (el instanceof Byte)
                len += this.write((Byte) el);
            else if (el instanceof Short)
                len += this.writeShort((Short) el);
            else if (el instanceof Integer)
                len += this.writeInt((Integer) el);
            else if (el instanceof Long)
                len += this.writeLong((Long) el);
            else if (el instanceof Float)
                len += this.writeFloat((Float) el);
            else if (el instanceof Double)
                len += this.writeDouble((Double) el);
            else if (el instanceof String)
                len += this.writeString((String) el);
            else if (el instanceof Object[])
                len += this.writeArray((Object[]) el);
            else if (el instanceof Collection)
                len += this.writeCollection((Collection) el);
            else if (el instanceof BufferSerializable)
                len += this.writeObject((BufferSerializable) el);
            else
                throw new IllegalArgumentException("Array element is of invalid type");
        }
        checkLastIndex(position - 1);
        return len;
    }

    public int writeArray(int index, Object[] arr)
    {
        grow(index, calcArrBytes(arr));
        int len = 0;
        int wrlen = this.writeInt(index, arr.length);
        len += wrlen;
        index += wrlen;
        for (Object el : arr)
        {
            if (el instanceof Byte)
            {
                int wr = this.write(index, (Byte) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Short)
            {
                int wr = this.writeShort(index, (Short) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Integer)
            {
                int wr = this.writeInt(index, (Integer) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Long)
            {
                int wr = this.writeLong(index, (Long) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Float)
            {
                int wr = this.writeFloat(index, (Float) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Double)
            {
                int wr = this.writeDouble(index, (Double) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof String)
            {
                int wr = this.writeString(index, (String) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Object[])
            {
                int wr = this.writeArray(index, (Object[]) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Collection)
            {
                int wr = this.writeCollection(index, (Collection) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof BufferSerializable)
            {
                int wr = this.writeObject((BufferSerializable) el);
                len += wr;
                index += wr;
            }
            else
                throw new IllegalArgumentException("Array element is of invalid type");
        }
        checkLastIndex(index - 1);
        return len;
    }

    public <T> T[] readArray(int index, Class<T> clazz)
    {
        return readArray(new AtomicInteger(index), clazz);
    }

    private <T> T[] readArray(AtomicInteger index, Class<T> clazz)
    {
        int arrlen = this.readInt(index.getAndAdd(4));
        Object arrobj = Array.newInstance(clazz, arrlen);
        Object[] arr = (Object[]) arrobj;
        for (int i = 0; i < arr.length; i++)
        {
            if (clazz == Byte.class)
                arr[i] = this.read(index.getAndAdd(1));
            else if (clazz == Short.class)
                arr[i] = this.readShort(index.getAndAdd(2));
            else if (clazz == Integer.class)
                arr[i] = this.readInt(index.getAndAdd(4));
            else if (clazz == Long.class)
                arr[i] = this.readLong(index.getAndAdd(8));
            else if (clazz == Float.class)
                arr[i] = this.readFloat(index.getAndAdd(4));
            else if (clazz == Double.class)
                arr[i] = this.readDouble(index.getAndAdd(8));
            else if (clazz == String.class)
                arr[i] = this.readString(index);
            else if (clazz.isArray())
                arr[i] = this.readArray(index, clazz);
            else if (clazz == Collection.class)
                arr[i] = this.readCollection(index, clazz);
            else if (hasInterface(clazz, BufferSerializable.class))
                arr[i] = this.readObject(index, clazz);
        }
        return (T[]) arrobj;
    }

    public <T> T[] readArray(Class<T> clazz)
    {
        int arrlen = this.readInt();
        Object arrobj = Array.newInstance(clazz, arrlen);
        Object[] arr = (Object[]) arrobj;
        for (int i = 0; i < arr.length; i++)
        {
            if (clazz == Byte.class)
                arr[i] = this.read();
            else if (clazz == Short.class)
                arr[i] = this.readShort();
            else if (clazz == Integer.class)
                arr[i] = this.readInt();
            else if (clazz == Long.class)
                arr[i] = this.readLong();
            else if (clazz == Float.class)
                arr[i] = this.readFloat();
            else if (clazz == Double.class)
                arr[i] = this.readDouble();
            else if (clazz == String.class)
                arr[i] = this.readString();
            else if (clazz.isArray())
                arr[i] = this.readArray(clazz);
            else if (clazz == Collection.class)
                arr[i] = this.readCollection(clazz);
            else if (hasInterface(clazz, BufferSerializable.class))
                arr[i] = this.readObject(clazz);
        }
        return (T[]) arrobj;
    }

    public int writeCollection(Collection collection)
    {
        grow(calcColBytes(collection));
        int len = 0;
        len += this.writeInt(collection.size());
        for (Object el : collection)
        {
            if (el instanceof Byte)
                len += this.write((Byte) el);
            else if (el instanceof Short)
                len += this.writeShort((Short) el);
            else if (el instanceof Integer)
                len += this.writeInt((Integer) el);
            else if (el instanceof Long)
                len += this.writeLong((Long) el);
            else if (el instanceof Float)
                len += this.writeFloat((Float) el);
            else if (el instanceof Double)
                len += this.writeDouble((Double) el);
            else if (el instanceof String)
                len += this.writeString((String) el);
            else if (el instanceof Object[])
                len += this.writeArray((Object[]) el);
            else if (el instanceof Collection)
                len += this.writeCollection((Collection) el);
            else if (el instanceof BufferSerializable)
                len += this.writeObject((BufferSerializable) el);
            else
                throw new IllegalArgumentException("Array element is of invalid type");
        }
        checkLastIndex(position - 1);
        return len;
    }

    public int writeCollection(int index, Collection collection)
    {
        grow(index, calcColBytes(collection));
        int len = 0;
        int wrlen = this.writeInt(index, collection.size());
        len += wrlen;
        index += wrlen;
        for (Object el : collection)
        {
            if (el instanceof Byte)
            {
                int wr = this.write(index, (Byte) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Short)
            {
                int wr = this.writeShort(index, (Short) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Integer)
            {
                int wr = this.writeInt(index, (Integer) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Long)
            {
                int wr = this.writeLong(index, (Long) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Float)
            {
                int wr = this.writeFloat(index, (Float) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Double)
            {
                int wr = this.writeDouble(index, (Double) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof String)
            {
                int wr = this.writeString(index, (String) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Object[])
            {
                int wr = this.writeArray(index, (Object[]) el);
                len += wr;
                index += wr;
            }
            else if (el instanceof Collection)
            {
                int wr = this.writeCollection(index, collection);
                len += wr;
                index += wr;
            }
            else if (el instanceof BufferSerializable)
            {
                int wr = this.writeObject((BufferSerializable) el);
                len += wr;
                index += wr;
            }
            else
                throw new IllegalArgumentException("Array element is of invalid type");
        }
        checkLastIndex(index - 1);
        return len;
    }

    public <T> Collection<T> readCollection(int index, Class<T> clazz)
    {
        return readCollection(new AtomicInteger(index), clazz);
    }

    private <T> Collection<T> readCollection(AtomicInteger index, Class<T> clazz)
    {
        return new ArrayList<>(Arrays.asList(this.readArray(index, clazz)));
    }

    public <T> Collection<T> readCollection(Class<T> clazz)
    {
        return new ArrayList<>(Arrays.asList(this.readArray(clazz)));
    }

    public int calcArrBytes(Object[] arr)
    {
        int bs = 0;
        for (Object el : arr)
        {
            if (el instanceof Byte)
                bs += Byte.BYTES;
            else if (el instanceof Short)
                bs += Short.BYTES;
            else if (el instanceof Integer)
                bs += Integer.BYTES;
            else if (el instanceof Long)
                bs += Long.BYTES;
            else if (el instanceof Float)
                bs += Float.BYTES;
            else if (el instanceof Double)
                bs += Double.BYTES;
            else if (el instanceof String)
                bs += 5 + ((String) el).length(); // TODO: change this to get bytes if support for diff charsets
            else if (el instanceof Object[])
                bs += calcArrBytes((Object[]) el);
            else if (el instanceof Collection)
                bs += calcColBytes((Collection) el);
        }
        return bs;
    }

    public int calcColBytes(Collection col)
    {
        int bs = 0;
        for (Object el : col)
        {
            if (el instanceof Byte)
                bs += Byte.BYTES;
            else if (el instanceof Short)
                bs += Short.BYTES;
            else if (el instanceof Integer)
                bs += Integer.BYTES;
            else if (el instanceof Long)
                bs += Long.BYTES;
            else if (el instanceof Float)
                bs += Float.BYTES;
            else if (el instanceof Double)
                bs += Double.BYTES;
            else if (el instanceof String)
                bs += 5 + ((String) el).length(); // TODO: change this to get bytes if support for diff charsets
            else if (el instanceof Object[])
                bs += calcArrBytes((Object[]) el);
            else if (el instanceof Collection)
                bs += calcColBytes((Collection) el);
        }
        return bs;
    }

    public int writeObject(int index, BufferSerializable obj)
    {
        Buffer buffer = obj.buffer();
        return this.writeBytes(index, buffer.array());
    }

    public int writeObject(BufferSerializable obj)
    {
        return this.writeBytes(obj.buffer().array());
    }

    public <T> T readObject(int index, Class<T> clazz)
    {
        return readObject(new AtomicInteger(index), clazz);
    }

    public <T> T readObject(AtomicInteger index, Class<T> clazz)
    {
        int pos = this.position;
        try
        {
            Method method = clazz.getDeclaredMethod("from", Buffer.class);
            method.setAccessible(true);
            this.position = index.get();
            Object obj = method.invoke(null, this);
            index.set(this.position);
            this.position = pos;
            if (obj == null) return null;
            return (T) obj;
        }
        catch (NoSuchMethodException | ClassCastException e)
        {
            throw new IllegalStateException(clazz.getName() + " does not have static method #from(Buffer) -> T");
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            this.position = pos;
            throw new IllegalStateException("Issue accessing static method #from(Buffer) -> T");
        }
    }

    public <T> T readObject(Class<T> clazz)
    {
        try
        {
            Method method = clazz.getDeclaredMethod("from", Buffer.class);
            method.setAccessible(true);
            Object obj = method.invoke(null, this);
            return (T) obj;
        }
        catch (NoSuchMethodException | ClassCastException e)
        {
            throw new IllegalStateException(clazz.getName() + " does not have static method #from(Buffer) -> T");
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            throw new IllegalStateException("Issue accessing static method #from(Buffer) -> T");
        }
    }

    public Buffer position(int position)
    {
        this.position = position;
        return this;
    }

    public int position()
    {
        return position;
    }

    public Buffer fixed(boolean fixed)
    {
        this.fixed = fixed;
        return this;
    }

    public boolean fixed()
    {
        return fixed;
    }

    /**
     * Resizes this Buffer to the provided {@code length}.
     * If the new length is shorter than the current length,
     * then data indexed higher than the new length will
     * be lost.
     * @param length New length of the Buffer
     * @return This Buffer
     */
    public Buffer length(int length)
    {
        byte[] d = this.data;
        this.data = new byte[length];
        System.arraycopy(d, 0, this.data, 0, length);
        if (this.lastIndex >= this.data.length)
            this.lastIndex = this.data.length - 1;
        return this;
    }

    public int length()
    {
        return this.data.length;
    }

    private boolean grow(int pos, int amount)
    {
        if (this.fixed)
            return false;
        if (pos + amount <= this.data.length)
            return false;
        byte[] d = this.data;
        this.data = new byte[this.data.length + (pos + amount - this.data.length)];
        System.arraycopy(d, 0, this.data, 0, d.length);
        return true;
    }

    private boolean grow(int amount)
    {
        return grow(this.position, amount);
    }

    private boolean checkLastIndex(int lastIndex)
    {
        if (this.lastIndex < lastIndex)
        {
            this.lastIndex = lastIndex;
            return true;
        }
        return false;
    }

    /**
     * Concatenates an entire Buffer to
     * the end of this Buffer.
     * Concatenation Buffer is truncated
     * to its effective length.
     * @param buffer
     * @return Amount of bytes used to write this Buffer
     */
    public int concat(Buffer buffer)
    {
        return this.writeBytes(buffer.data.length, buffer.array());
    }

    /**
     * Gets the significant binary data of this Buffer.
     * @return A byte array containing significants from this Buffer
     */
    public byte[] array()
    {
        if (this.data.length == lastIndex + 1)
            return this.data;
        byte[] a = new byte[lastIndex + 1];
        System.arraycopy(this.data, 0, a, 0, a.length);
        return a;
    }

    /**
     * @return All binary data from this Buffer
     */
    public byte[] raw()
    {
        return this.data;
    }

    /**
     * Gets a portion of this Buffer
     * and creates its own sub-Buffer of
     * the data within the selected indices.
     * @param start Start of a portion of the Buffer, inclusive
     * @param end End of a portion of the Buffer, exclusive
     * @return A sub-Buffer containing the data within the provided indices
     */
    public Buffer slice(int start, int end)
    {
        if (start > end)
        {
            int t = end;
            end = start;
            start = t;
        }
        byte[] sl = new byte[end - start];
        System.arraycopy(this.data, start, sl, 0, sl.length);
        int significant = this.lastIndex - start + 1;
        if (significant < 0) significant = 0;
        if (significant > sl.length) significant = sl.length;
        return new Buffer(sl,
                this.fixed,
                significant,
                position >= start && position < end ? position - start : 0);
    }

    /**
     * Gets a portion of this Buffer
     * and creates its own sub-Buffer of
     * the data between the start index
     * and the end of the Buffer.
     * @param start Start of a portion of the Buffer, inclusive
     * @return A sub-Buffer containing the data from the start index to the end of the Buffer
     */
    public Buffer slice(int start)
    {
        return slice(start, this.data.length);
    }

    /**
     * Writes this Buffer out to a file.
     * @param file Destination to write to
     */
    public void writeFile(File file)
    {
        try
        {
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(this.array());
            fos.flush();
            fos.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes this Buffer out to a file.
     * @param fileName Name of destination to write to
     */
    public void writeFile(String fileName)
    {
        writeFile(new File(fileName));
    }

    /**
     * Reads the file provided into a Buffer.
     * @param file Source to read from
     * @return Buffer constructed from the file provided
     */
    public static Buffer readFile(File file)
    {
        try
        {
            FileInputStream fis = new FileInputStream(file);
            int b;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((b = fis.read()) != -1)
                baos.write(b);
            Buffer buffer = new Buffer(baos.toByteArray());
            fis.close();
            baos.flush();
            baos.close();
            return buffer;
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reads the file provided into a Buffer.
     * @param fileName Name of source to read from
     * @return Buffer constructed from the file provided
     */
    public static Buffer readFile(String fileName)
    {
        return readFile(new File(fileName));
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder()
                .append("Buffer{length=")
                .append(this.data.length)
                .append(", significants=")
                .append(this.lastIndex + 1)
                .append(", fixed=")
                .append(fixed)
                .append(", data=[");
        for (int i = 0; i < this.data.length; i++)
        {
            String hex = Integer.toHexString(this.data[i]).toUpperCase();
            if (hex.length() == 1) hex = "0" + hex;
            hex = hex.substring(hex.length() - 2);
            if (i != 0) builder.append(" ");
            builder.append(hex);
        }
        builder.append("]}");
        return builder.toString();
    }

    private static byte getCharsetIndex(Charset charset)
    {
        for (byte b = 0; b < AVAILABLE_CHARSETS.length; b++)
        {
            if (charset.equals(AVAILABLE_CHARSETS[b]))
                return b;
        }
        return -1;
    }

    private static boolean hasInterface(Class<?> clazz, Class<?> searchIntf)
    {
        for (Class<?> intf : clazz.getInterfaces())
        {
            if (intf == searchIntf)
                return true;
        }
        return false;
    }
}