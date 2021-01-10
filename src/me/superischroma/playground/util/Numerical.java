package me.superischroma.playground.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;

/**
 * A convertible and versatile value of any of the four numerical data types in Java.
 */
public class Numerical implements Cloneable
{
    public static Method CEIL, FLOOR, ROUND, FLOAT_ROUND;

    static
    {
        try
        {
            CEIL = Math.class.getDeclaredMethod("ceil", double.class);
            FLOOR = Math.class.getDeclaredMethod("floor", double.class);
            ROUND = Math.class.getDeclaredMethod("round", double.class);
            FLOAT_ROUND = Math.class.getDeclaredMethod("round", float.class);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    private Integer i;
    private Double d;
    private Float f;
    private Long l;
    private Class<? extends Number> type;

    /**
     * Create an Integer Number.
     * @param i An integer
     */
    public Numerical(int i)
    {
        this.i = i;
        this.type = Integer.class;
    }

    /**
     * Create a Double Number.
     * @param d A double
     */
    public Numerical(double d)
    {
        this.d = d;
        this.type = Double.class;
    }

    /**
     * Create a Float Number.
     * @param f A float
     */
    public Numerical(float f)
    {
        this.f = f;
        this.type = Float.class;
    }

    /**
     * Create a Long Number.
     * @param l A long
     */
    public Numerical(long l)
    {
        this.l = l;
        this.type = Long.class;
    }

    /**
     * Create a numerical with a specified type and rounding mode.
     * @param number Any form of number
     * @param type The type of the numerical
     * @param roundingMode What rounding mode the numerical should use.
     */
    public Numerical(double number, Class<? extends Number> type, Method roundingMode)
    {
        if (type == Integer.class)
            this.i = round(round(number, roundingMode));
        if (type == Double.class)
            this.d = number;
        if (type == Float.class)
            this.f = (float) number;
        if (type == Long.class)
            this.l = round(number, roundingMode);
    }

    /**
     * Create a numerical with a specified type and the default rounding mode.
     * @param number Any form of number
     * @param type The type of the numerical
     */
    public Numerical(double number, Class<? extends Number> type)
    {
        this(number, type, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Change the type of the numerical.
     * @param type The new type of the numerical.
     * @param roundingMode What rounding mode to be used when converting
     */
    public void convert(Class<? extends Number> type, Method roundingMode)
    {
        double save = asDouble();
        set(0.0);
        this.type = type;
        if (type == Integer.class || type == int.class)
            this.i = Math.toIntExact(round(save, roundingMode));
        if (type == Double.class)
            this.d = save;
        if (type == Float.class)
            this.f = (float) save;
        if (type == Long.class)
            this.l = round(save, roundingMode);
    }

    /**
     * Change the type of the numerical using the default rounding mode.
     * @param type The new type of the numerical.
     */
    public void convert(Class<? extends Number> type)
    {
        convert(type, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Gets this numerical as an Integer value.
     * Integers will automatically round down to their max value if the numerical is greater than it.
     * @param roundingMode The rounding mode used when converting.
     * @return An Integer of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public int asInt(Method roundingMode)
    {
        if (type == Integer.class)
            return i;
        if (type == Double.class)
            return round(round(d, roundingMode));
        if (type == Float.class)
            return round(f, roundingMode);
        if (type == Long.class)
            return round(l);
        throw new NumberFormatException();
    }

    /**
     * Gets this numerical as a Float value.
     * @return A Float of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public float asFloat()
    {
        if (type == Integer.class)
            return (float) i;
        if (type == Double.class)
            return d.floatValue();
        if (type == Float.class)
            return f;
        if (type == Long.class)
            return (float) round(l);
        throw new NumberFormatException();
    }

    /**
     * Gets this numerical as a Long value.
     * @param roundingMode The rounding used while converting.
     * @return A Long of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public long asLong(Method roundingMode)
    {
        if (type == Integer.class)
            return (long) i;
        if (type == Double.class)
            return round(d, roundingMode);
        if (type == Float.class)
            return round(f, roundingMode);
        if (type == Long.class)
            return l;
        throw new NumberFormatException();
    }

    /**
     * Gets this numerical as a Long value using the default rounding mode.
     * @return A Long of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public long asLong()
    {
        return asLong(type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Gets this numerical as an Integer value.
     * Integers will automatically round down to their max value if the numerical is greater than it.
     * @return An Integer of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public int asInt()
    {
        return asInt(type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Gets this numerical as a Double value.
     * @return A Double of the numerical.
     * @throws NumberFormatException When an invalid number type is being used.
     */
    public double asDouble()
    {
        if (type == Integer.class)
            return (double) i;
        if (type == Double.class)
            return d;
        if (type == Float.class)
            return (double) f;
        if (type == Long.class)
            return (double) l;
        throw new NumberFormatException();
    }

    /**
     * Sets the value of this numerical.
     * @param number The new value of the numerical.
     * @param roundingMode What rounding mode the numerical should use.
     */
    public void set(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i = Math.toIntExact(round(number, roundingMode));
        if (type == Double.class)
            this.d = number;
        if (type == Float.class)
            this.f = (float) number;
        if (type == Long.class)
            this.l = round(number, roundingMode);
    }

    /**
     * Sets the value of this numerical using the default rounding mode.
     * @param number The new value of the numerical.
     */
    public void set(double number)
    {
        set(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Adds to the current value of the numerical.
     * @param number What to add to the numerical.
     * @param roundingMode What rounding mode the adding value should use.
     */
    public void add(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i += Math.toIntExact(round(number, roundingMode));
        if (type == Double.class)
            this.d += number;
        if (type == Float.class)
            this.f += (float) number;
        if (type == Long.class)
            this.l += round(number, roundingMode);
    }

    /**
     * Adds to the current value of the numerical using the default rounding mode.
     * @param number What to add to the numerical.
     */
    public void add(double number)
    {
        add(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Subtracts from the current value of the numerical.
     * @param number What to subtract from the numerical.
     * @param roundingMode What rounding mode the subtracting value should use.
     */
    public void sub(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i -= Math.toIntExact(round(number, roundingMode));
        if (type == Double.class)
            this.d -= number;
        if (type == Float.class)
            this.f -= (float) number;
        if (type == Long.class)
            this.l -= round(number, roundingMode);
    }

    /**
     * Subtracts from the current value of the numerical using the default rounding mode.
     * @param number What to subtract from the numerical.
     */
    public void sub(double number)
    {
        sub(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Multiplies the current value of the numerical.
     * @param number What to multiply the numerical by.
     * @param roundingMode What rounding mode the multiplying value should use.
     */
    public void mul(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i *= Math.toIntExact(round(number, roundingMode));
        if (type == Double.class)
            this.d *= number;
        if (type == Float.class)
            this.f *= (float) number;
        if (type == Long.class)
            this.l *= round(number, roundingMode);
    }

    /**
     * Multiplies the current value of the numerical using the default rounding mode.
     * @param number What to multiply the numerical by.
     */
    public void mul(double number)
    {
        mul(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Divides the current value of the numerical.
     * @param number What to divide the numerical by.
     * @param roundingMode What rounding mode the dividing value should use.
     */
    public void div(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i /= Math.toIntExact(round(number, roundingMode));;
        if (type == Double.class)
            this.d /= number;
        if (type == Float.class)
            this.f /= (float) number;
        if (type == Long.class)
            this.l /= round(number, roundingMode);
    }

    /**
     * Divides the current value of the numerical using the default rounding mode.
     * @param number What to divide the numerical by.
     */
    public void div(double number)
    {
        div(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Modulo the current value of the numerical.
     * @param number What to modulo the numerical by.
     * @param roundingMode What rounding mode the modulo value should use.
     */
    public void mod(double number, Method roundingMode)
    {
        if (type == Integer.class)
            this.i %= Math.toIntExact(round(number, roundingMode));;
        if (type == Double.class)
            this.d %= number;
        if (type == Float.class)
            this.f %= (float) number;
        if (type == Long.class)
            this.l %= round(number, roundingMode);
    }

    /**
     * Modulo the current value of the numerical using the default rounding mode.
     * @param number What to modulo the numerical by.
     */
    public void mod(double number)
    {
        mod(number, type == Float.class ? FLOAT_ROUND : ROUND);
    }

    /**
     * Round a double using the specified rounding mode.
     * @param d A double value.
     * @param roundingMode What rounding mode to use.
     * @return A long value.
     */
    protected long round(double d, Method roundingMode)
    {
        try
        {
            Object r = roundingMode.invoke(null, d);
            if (r instanceof Double)
                return ((Double) r).longValue();
            if (r instanceof Integer)
                return ((Integer) r).longValue();
            return (long) r;
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex)
        {
            throw new NumericalRoundException(roundingMode, double.class);
        }
        throw new NumericalRoundException(roundingMode, double.class);
    }

    /**
     * Round a float using the specified rounding mode.
     * @param f A float value.
     * @param roundingMode What rounding mode to use.
     * @return An integer value.
     */
    protected int round(float f, Method roundingMode)
    {
        try
        {
            Object r = roundingMode.invoke(null, f);
            if (r instanceof Double)
                return ((Double) r).intValue();
            return (int) roundingMode.invoke(null, f);
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
        catch (MalformedParameterizedTypeException ex)
        {
            throw new NumericalRoundException(roundingMode, float.class);
        }
        throw new NumericalRoundException(roundingMode, float.class);
    }

    /**
     * If a long value is greater than the max integer value, it'll get the max integer value.
     * @param l A long value.
     * @return A long value.
     */
    private int round(long l)
    {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) l;
    }

    /**
     * Gets the type of number that's stored in this numerical
     * @return The type of number
     */
    public Class<? extends Number> getType()
    {
        return type;
    }

    /**
     * Gets the value of the numerical as a String.
     * @return A String of the numerical.
     */
    @Override
    public String toString()
    {
        if (type == Integer.class)
            return String.valueOf(asInt());
        if (type == Double.class)
            return String.valueOf(asDouble());
        if (type == Float.class)
            return String.valueOf(asFloat());
        if (type == Long.class)
            return String.valueOf(asLong());
        throw new NumberFormatException();
    }

    /**
     * Check if two numericals are the same.
     * @param o Another numerical.
     * @return Whether this numerical and the other numerical are the same.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o.getClass() == int.class || o instanceof Integer)
            return i == o;
        if (o.getClass() == double.class || o instanceof Double)
            return d == o;
        if (o.getClass() == float.class || o instanceof Float)
            return f == o;
        if (o.getClass() == long.class || o instanceof Long)
            return l == o;
        if (o instanceof Numerical)
            return ((Numerical) o).asDouble() == this.asDouble();
        return false;
    }

    /**
     * Create an exact duplicate of this numerical.
     * @return A cloned version of this numerical.
     */
    @Override
    public Numerical clone()
    {
        if (type == Integer.class)
            return new Numerical(i);
        if (type == Double.class)
            return new Numerical(d);
        if (type == Float.class)
            return new Numerical(f);
        if (type == Long.class)
            return new Numerical(l);
        throw new NumberFormatException();
    }

    /**
     * Thrown when a numerical can't be rounded using the specified rounding mode.
     */
    public static class NumericalRoundException extends RuntimeException
    {
        /**
         * Create a numerical rounding exception.
         * @param method The rounding mode used.
         * @param type The type of the numerical.
         */
        public NumericalRoundException(Method method, Class<? extends Number> type)
        {
            super("Rounding with " + method.getDeclaringClass().getName() + "#" + method.getName() +
                    "(" + joinedMethodTypes(method) + ") is not compatible with "
                    + type.getSimpleName() + " numericals");
        }

        /**
         * Joins method parameter types by comma.
         * @param method The method.
         * @return A String spaced by comma containing the type of every parameter
         */
        protected static String joinedMethodTypes(Method method)
        {
            StringBuilder res = new StringBuilder();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++)
                res.append(i == 0 ? "" : ", ").append(parameterTypes[i].getSimpleName());
            return res.toString();
        }
    }
}