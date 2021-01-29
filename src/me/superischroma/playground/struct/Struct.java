package me.superischroma.playground.struct;

import java.lang.reflect.Field;

/**
 * Structured Storage Interface - SSI (.ssf)
 * Types:
 *  - Struct_End > signifies the end of a Struct_Collection
 *  - Struct_Byte > a byte
 *  - Struct_Short > a signed, 16-bit (2 byte) integer
 *  - Struct_Int > a signed, 32-bit (4 byte) integer
 *  - Struct_Long > a signed, 64-bit (8 byte) integer
 *  - Struct_Float > a 32-bit (4 byte) IEEE-754 single-precision number
 *  - Struct_Double > a 64-bit (8 byte) IEEE-754 double-precision number
 *  - Struct_Collection > a grouping of Structured Objects
 *  - Struct_String > a string of characters
 *  - Struct_Byte_Array > a sequence of bytes
 *  - Struct_Int_Array > a sequence of signed, 32-bit (4 byte) integers
 *  - Struct_Long_Array > a sequence of signed, 64-bit (8 byte) integers
 * @param <T> Type to be stored as
 */
public interface Struct<T>
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

    static Struct create(Object object)
    {
        if (object instanceof Byte)
            return new StructByte((Byte) object);
        if (object instanceof Short)
            return new StructShort((Short) object);
        if (object instanceof Integer)
            return new StructInt((Integer) object);
        if (object instanceof Long)
            return new StructLong((Long) object);
        if (object instanceof Float)
            return new StructFloat((Float) object);
        if (object instanceof Double)
            return new StructDouble((Double) object);
        if (object instanceof String)
            return new StructString((String) object);
        if (object instanceof byte[])
            return new StructByteArray((byte[]) object);
        if (object instanceof int[])
            return new StructIntArray((int[]) object);
        if (object instanceof long[])
            return new StructLongArray((long[]) object);
        return null;
    }

    static Struct create(byte type)
    {
        if (type == StructEnd.TYPE)
            return new StructEnd();
        if (type == StructByte.TYPE)
            return new StructByte((byte) 0);
        if (type == StructShort.TYPE)
            return new StructShort((short) 0);
        if (type == StructInt.TYPE)
            return new StructInt(0);
        if (type == StructLong.TYPE)
            return new StructLong(0);
        if (type == StructFloat.TYPE)
            return new StructFloat(0.0f);
        if (type == StructDouble.TYPE)
            return new StructDouble(0.0);
        if (type == StructCollection.TYPE)
            return new StructCollection(null);
        if (type == StructString.TYPE)
            return new StructString(null);
        if (type == StructByteArray.TYPE)
            return new StructByteArray(null);
        if (type == StructIntArray.TYPE)
            return new StructIntArray(null);
        if (type == StructLongArray.TYPE)
            return new StructLongArray(null);
        return null;
    }

    static String getName(Struct struct)
    {
        try
        {
            Field f = struct.getClass().getDeclaredField("NAME");
            return (String) f.get(struct);
        }
        catch (IllegalAccessException | NoSuchFieldException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}