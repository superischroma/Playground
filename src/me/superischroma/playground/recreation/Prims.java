package me.superischroma.playground.recreation;

public final class Prims
{
    public static int atoi(String s)
    {
        if (s.isEmpty())
            throw new NumberFormatException();
        char[] chars = s.toCharArray();
        int result = 0;
        for (int i = chars.length - 1, k = 1; i >= (chars[0] == '-' ? 1 : 0); i--, k *= 10)
            result += (((int) chars[i]) - 48) * k;
        if (chars[0] == '-')
            result = -result;
        return result;
    }

    public static String itoa(int i)
    {
        String s = "";
        int c = (i < 0 ? -i : i);
        do
        {
            int modulo = c % 10;
            c -= modulo;
            c /= 10;
            s = (char) (modulo + 48) + s;
        }
        while (c != 0);
        return (i < 0 ? "-" : "") + s;
    }

    public static boolean atob(String s)
    {
        switch (s.toLowerCase())
        {
            case "true":
            case "1":
                return true;
            default:
                return false;
        }
    }

    public static boolean itob(int i)
    {
        return i > 0;
    }

    public static String btoa(boolean b)
    {
        return b ? "true" : "false";
    }

    public static int btoi(boolean b)
    {
        return b ? 1 : 0;
    }
}