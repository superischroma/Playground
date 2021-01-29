package me.superischroma.playground.ssd;

import me.superischroma.playground.ssd.array.*;

import java.lang.reflect.Field;

/**
 * Structured Storage Data - SSD (.ssf)
 * Types:
 *  - SSD_End > a byte value (0) that signifies the end of an SSD_Collection
 *  - SSD_Byte > a byte
 *  - SSD_Short > a signed, 16-bit (2 byte) integer
 *  - SSD_Int > a signed, 32-bit (4 byte) integer
 *  - SSD_Long > a signed, 64-bit (8 byte) integer
 *  - SSD_Float > a 32-bit (4 byte) IEEE-754 single-precision number
 *  - SSD_Double > a 64-bit (8 byte) IEEE-754 double-precision number
 *  - SSD_Collection > a grouping of SSDured Objects
 *  - SSD_String > a string of characters
 *  - SSD_Byte_Array > a sequence of bytes
 *  - SSD_Short_Array > a sequence of signed, 16-bit (2 byte) integers
 *  - SSD_Int_Array > a sequence of signed, 32-bit (4 byte) integers
 *  - SSD_Long_Array > a sequence of signed, 64-bit (8 byte) integers
 *  - SSD_Float_Array > a sequence of 32-bit (4 byte) IEEE-754 single-precision numbers
 *  - SSD_Double_Array > a sequence of 64-bit (8 byte) IEEE-754 double-precision numbers
 * @param <T> Type to be stored as
 */
public interface SSD<T>
{
    byte getType();
    void setValue(T t);
    T getValue();
    int length();
    byte[] asByteArray();
    default boolean usesSpecialLength()
    {
        return false;
    }

    static SSD create(Object object)
    {
        if (object instanceof Byte)
            return new SSDByte((Byte) object);
        if (object instanceof Short)
            return new SSDShort((Short) object);
        if (object instanceof Integer)
            return new SSDInt((Integer) object);
        if (object instanceof Long)
            return new SSDLong((Long) object);
        if (object instanceof Float)
            return new SSDFloat((Float) object);
        if (object instanceof Double)
            return new SSDDouble((Double) object);
        if (object instanceof String)
            return new SSDString((String) object);
        if (object instanceof byte[])
            return new SSDByteArray((byte[]) object);
        if (object instanceof short[])
            return new SSDShortArray((short[]) object);
        if (object instanceof int[])
            return new SSDIntArray((int[]) object);
        if (object instanceof long[])
            return new SSDLongArray((long[]) object);
        if (object instanceof float[])
            return new SSDFloatArray((float[]) object);
        if (object instanceof double[])
            return new SSDDoubleArray((double[]) object);
        return null;
    }

    static SSD create(byte type)
    {
        if (type == SSDEnd.TYPE)
            return new SSDEnd();
        if (type == SSDByte.TYPE)
            return new SSDByte((byte) 0);
        if (type == SSDShort.TYPE)
            return new SSDShort((short) 0);
        if (type == SSDInt.TYPE)
            return new SSDInt(0);
        if (type == SSDLong.TYPE)
            return new SSDLong(0);
        if (type == SSDFloat.TYPE)
            return new SSDFloat(0.0f);
        if (type == SSDDouble.TYPE)
            return new SSDDouble(0.0);
        if (type == SSDCollection.TYPE)
            return new SSDCollection(null);
        if (type == SSDString.TYPE)
            return new SSDString(null);
        if (type == SSDByteArray.TYPE)
            return new SSDByteArray(null);
        if (type == SSDShortArray.TYPE)
            return new SSDShortArray(null);
        if (type == SSDIntArray.TYPE)
            return new SSDIntArray(null);
        if (type == SSDLongArray.TYPE)
            return new SSDLongArray(null);
        if (type == SSDFloatArray.TYPE)
            return new SSDFloatArray(null);
        if (type == SSDDoubleArray.TYPE)
            return new SSDDoubleArray(null);
        return null;
    }

    static String getName(SSD ssd)
    {
        try
        {
            Field f = ssd.getClass().getDeclaredField("NAME");
            return (String) f.get(ssd);
        }
        catch (IllegalAccessException | NoSuchFieldException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}