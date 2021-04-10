package me.superischroma.playground;

import me.superischroma.playground.util.Numbers;
import me.superischroma.playground.util.ArraysX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class Sim
{
    public static void execute(File file) throws FileNotFoundException
    {
        Scanner ln = new Scanner(System.in);
        Scanner in = new Scanner(file);
        String current = null;
        int line = 0;
        Map<String, NamelessVariable> variables = new HashMap<>();
        try
        {
            while (in.hasNextLine())
            {
                current = in.nextLine();
                line++;
                String[] split = current.split(" ");
                String instruction = split[0];
                String suffix = "";
                try
                {
                    suffix = ArraysX.join(Arrays.copyOfRange(split, 1, split.length), " ");
                }
                catch (IndexOutOfBoundsException ignored) {}
                switch (instruction)
                {
                    case "int":
                    case "int32":
                        variables.put(split[1], new NamelessVariable(VariableType.INT_32, split.length != 2 ? Integer.parseInt(split[3]) : null));
                        break;
                    case "long":
                    case "int64":
                        variables.put(split[1], new NamelessVariable(VariableType.INT_64, split.length != 2 ? Long.parseLong(split[3]) : null));
                        break;
                    case "float":
                    case "float32":
                        variables.put(split[1], new NamelessVariable(VariableType.FLOAT_32, split.length != 2 ? Float.parseFloat(split[3]) : null));
                        break;
                    case "double":
                    case "float64":
                        variables.put(split[1], new NamelessVariable(VariableType.FLOAT_64, split.length != 2 ? Double.parseDouble(split[3]) : null));
                        break;
                    case "char":
                    {
                        if (split.length == 2)
                        {
                            variables.put(split[1], new NamelessVariable(VariableType.CHAR, null));
                            break;
                        }
                        String raw = split[3];
                        try
                        {
                            // testing for potential byte value
                            byte b = Byte.parseByte(raw);
                            variables.put(split[1], new NamelessVariable(VariableType.CHAR, (char) b));
                            break;
                        }
                        catch (NumberFormatException ignored) {}
                        if (!raw.startsWith("'") || !raw.endsWith("'"))
                            throw new Exception("char must be surrounded by single quotes!");
                        variables.put(split[1], new NamelessVariable(VariableType.CHAR, raw.substring(1, raw.length() - 1).charAt(0)));
                        break;
                    }
                    case "bool":
                        variables.put(split[1], new NamelessVariable(VariableType.BOOL, split.length != 2 ? Boolean.parseBoolean(split[3]) : null));
                        break;
                    case "str":
                    case "string":
                    {
                        if (split.length == 2)
                        {
                            variables.put(split[1], new NamelessVariable(VariableType.STR, null));
                            break;
                        }
                        String raw = ArraysX.join(Arrays.copyOfRange(split, 3, split.length), " ");
                        if (!raw.startsWith("\"") || !raw.endsWith("\""))
                            throw new RuntimeException("string must be surrounded by double quotes!");
                        variables.put(split[1], new NamelessVariable(VariableType.STR, parseStringLiteral(split, 3)));
                        break;
                    }
                    case "out":
                    {
                        for (String o : suffix.split(", "))
                        {
                            Object val = variables.get(o);
                            if (val == null)
                                throw new VariableNotFoundException(o);
                            System.out.print(val.toString());
                        }
                        break;
                    }
                    case "ins":
                    {
                        if (!variables.containsKey(suffix))
                            throw new VariableNotFoundException(suffix);
                        NamelessVariable variable = variables.get(suffix);
                        if (variable.getType() != VariableType.STR)
                            throw new VariableTypeMismatchException(suffix);
                        variable.set(ln.nextLine());
                        break;
                    }
                    case "cnc":
                    {
                        String[] os = suffix.split(", ");
                        if (!variables.containsKey(os[0]))
                            throw new VariableNotFoundException(os[0]);
                        NamelessVariable dest = variables.get(os[0]);
                        if (dest.getType() != VariableType.STR)
                            throw new VariableTypeMismatchException(os[0]);
                        NamelessVariable from = variables.get(os[1]);
                        String result;
                        if (from == null)
                            result = parseStringLiteral(os, 1);
                        else
                            result = from.get().toString();
                        dest.set(dest.get().toString() + result);
                        break;
                    }
                    case "inc":
                    {
                        if (!variables.containsKey(suffix))
                            throw new VariableNotFoundException(suffix);
                        NamelessVariable variable = variables.get(suffix);
                        if (!variable.isIntegral())
                            throw new VariableTypeMismatchException(suffix);
                        variable.set(Numbers.add((Number) variable.get(), 1));
                        break;
                    }
                }
                if (instruction.equals("add") || instruction.equals("sub") || instruction.equals("mul") || instruction.equals("div") ||
                    instruction.equals("mod"))
                {
                    String[] os = suffix.split(", ");
                    if (!variables.containsKey(os[0]))
                        throw new VariableNotFoundException(os[0]);
                    NamelessVariable variable = variables.get(os[0]);
                    if (!variable.isIntegral())
                        throw new VariableTypeMismatchException(os[0]);
                    NamelessVariable from = variables.get(os[1]);
                    Number result;
                    if (from == null)
                        result = Numbers.parseNumber(os[1]);
                    else
                        result = from.valueAs(Number.class);
                    variable.set(Numbers.arith((Number) variable.get(), result, instruction));
                }
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error executing Sim file at line " + line + ": " + current + " (" + ex.getMessage() + ")");
        }
    }

    private static String parseStringLiteral(String[] arr, int startIndex)
    {
        String raw = ArraysX.join(Arrays.copyOfRange(arr, startIndex, arr.length), " ");
        if (!raw.startsWith("\"") || !raw.endsWith("\""))
            throw new RuntimeException("string must be surrounded by double quotes!");
        return raw.substring(1, raw.length() - 1);
    }

    private static class VariableNotFoundException extends RuntimeException
    {
        public VariableNotFoundException(String name)
        {
            super("Could not find variable declaration for \"" + name + "\"");
        }
    }

    private static class VariableTypeMismatchException extends RuntimeException
    {
        public VariableTypeMismatchException(String name)
        {
            super("Variable type for \"" + name + "\" does not match the required type for this instruction");
        }
    }

    private static class NamelessVariable
    {
        private final VariableType type;
        private Object value;

        public NamelessVariable(VariableType type, Object value)
        {
            this.type = type;
            this.value = value;
        }

        public VariableType getType()
        {
            return type;
        }

        public Object get()
        {
            return value;
        }

        public void set(Object value)
        {
            this.value = value;
        }

        public boolean isIntegral()
        {
            return type == VariableType.INT_32 || type == VariableType.INT_64;
        }

        public boolean isFloatingPoint()
        {
            return type == VariableType.FLOAT_32 || type == VariableType.FLOAT_64;
        }

        public boolean isNumber()
        {
            return type == VariableType.INT_32 || type == VariableType.INT_64 ||
                    type == VariableType.FLOAT_32 || type == VariableType.FLOAT_64;
        }

        public <T> T valueAs(Class<T> clazz)
        {
            return clazz.cast(value);
        }

        @Override
        public String toString()
        {
            return value.toString();
        }
    }

    private enum VariableType
    {
        INT_32,
        INT_64,
        FLOAT_32,
        FLOAT_64,
        CHAR,
        BOOL,
        STR
    }
}