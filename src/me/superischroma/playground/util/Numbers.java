package me.superischroma.playground.util;

public final class Numbers
{
    public static Number add(Number original, Number app)
    {
        if (original instanceof Byte)
            return original.byteValue() + app.byteValue();
        if (original instanceof Short)
            return original.shortValue() + app.shortValue();
        if (original instanceof Integer)
            return original.intValue() + app.intValue();
        if (original instanceof Long)
            return original.longValue() + app.longValue();
        if (original instanceof Float)
            return original.floatValue() + app.floatValue();
        if (original instanceof Double)
            return original.doubleValue() + app.doubleValue();
        throw new ArithmeticException();
    }

    public static Number sub(Number original, Number app)
    {
        if (original instanceof Byte)
            return original.byteValue() - app.byteValue();
        if (original instanceof Short)
            return original.shortValue() - app.shortValue();
        if (original instanceof Integer)
            return original.intValue() - app.intValue();
        if (original instanceof Long)
            return original.longValue() - app.longValue();
        if (original instanceof Float)
            return original.floatValue() - app.floatValue();
        if (original instanceof Double)
            return original.doubleValue() - app.doubleValue();
        throw new ArithmeticException();
    }

    public static Number mul(Number original, Number app)
    {
        if (original instanceof Byte)
            return original.byteValue() * app.byteValue();
        if (original instanceof Short)
            return original.shortValue() * app.shortValue();
        if (original instanceof Integer)
            return original.intValue() * app.intValue();
        if (original instanceof Long)
            return original.longValue() * app.longValue();
        if (original instanceof Float)
            return original.floatValue() * app.floatValue();
        if (original instanceof Double)
            return original.doubleValue() * app.doubleValue();
        throw new ArithmeticException();
    }

    public static Number div(Number original, Number app)
    {
        if (original instanceof Byte)
            return original.byteValue() / app.byteValue();
        if (original instanceof Short)
            return original.shortValue() / app.shortValue();
        if (original instanceof Integer)
            return original.intValue() / app.intValue();
        if (original instanceof Long)
            return original.longValue() / app.longValue();
        if (original instanceof Float)
            return original.floatValue() / app.floatValue();
        if (original instanceof Double)
            return original.doubleValue() / app.doubleValue();
        throw new ArithmeticException();
    }

    public static Number mod(Number original, Number app)
    {
        if (original instanceof Byte)
            return original.byteValue() % app.byteValue();
        if (original instanceof Short)
            return original.shortValue() % app.shortValue();
        if (original instanceof Integer)
            return original.intValue() % app.intValue();
        if (original instanceof Long)
            return original.longValue() % app.longValue();
        if (original instanceof Float)
            return original.floatValue() % app.floatValue();
        if (original instanceof Double)
            return original.doubleValue() % app.doubleValue();
        throw new ArithmeticException();
    }

    public static Number arith(Number original, Number app, Type type)
    {
        switch (type)
        {
            case ADDITION: return add(original, app);
            case SUBTRACTION: return sub(original, app);
            case MULTIPLICATION: return mul(original, app);
            case DIVISION: return div(original, app);
            case MODULO: return mod(original, app);
        }
        throw new ArithmeticException();
    }

    public static Number arith(Number original, Number app, String typeResolvable)
    {
        switch (typeResolvable)
        {
            case "add": return add(original, app);
            case "sub": return sub(original, app);
            case "mul": return mul(original, app);
            case "div": return div(original, app);
            case "mod": return mod(original, app);
        }
        throw new ArithmeticException();
    }

    public static Number parseNumber(String s, int radix)
    {
        try { return Byte.parseByte(s, radix); } catch (NumberFormatException ignored) {}
        try { return Short.parseShort(s, radix); } catch (NumberFormatException ignored) {}
        try { return Integer.parseInt(s, radix); } catch (NumberFormatException ignored) {}
        try { return Long.parseLong(s, radix); } catch (NumberFormatException ignored) {}
        try { return Float.parseFloat(s); } catch (NumberFormatException ignored) {}
        try { return Double.parseDouble(s); } catch (NumberFormatException ignored) {}
        throw new NumberFormatException();
    }

    public static Number parseNumber(String s)
    {
        return parseNumber(s, 10);
    }

    public enum Type
    {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        MODULO
    }
}