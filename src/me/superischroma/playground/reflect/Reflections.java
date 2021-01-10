package me.superischroma.playground.reflect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extended Java Reflections
 */
public final class Reflections
{
    /**
     * Gets every class within the specified package
     * @param pkg The package to search within
     * @return A List of classes within the package
     */
    public static List<Class<?>> getClassesInPackage(String pkg)
    {
        String directoryPackage = pkg.replace(".",
                File.separator);
        try
        {
            List<Path> paths = Files.find(Paths.get(new File("./").getAbsolutePath()),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) ->
                    {
                        File file = path.toFile();
                        if (file.isDirectory())
                            return false;
                        return file.getParentFile().getAbsolutePath().endsWith(directoryPackage);
                    })
                    .collect(Collectors.toList());
            List<Class<?>> classes = new ArrayList<>();
            for (Path path : paths)
            {
                File file = path.toFile();
                if (!path.toString().endsWith(".java"))
                    continue;
                Class<?> c = Class.forName(pkg + "." +
                        file.getName().replace(".java", ""));
                classes.add(c);
            }
            return classes;
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Gets every class of the specified type within a package
     * @param pkg The package to search within
     * @param clazz The type of class to search for
     * @param <T> The class type
     * @return A List of all classes within the package that belong to the class provided
     */
    public static <T> List<Class<? extends T>> getSubclassesInPackage(String pkg, Class<T> clazz)
    {
        String directoryPackage = pkg.replace(".",
                File.separator);
        try
        {
            List<Path> paths = Files.find(Paths.get(new File("./").getAbsolutePath()),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) ->
                    {
                        File file = path.toFile();
                        if (file.isDirectory())
                            return false;
                        return file.getParentFile().getAbsolutePath().endsWith(directoryPackage);
                    })
                    .collect(Collectors.toList());
            List<Class<? extends T>> classes = new ArrayList<>();
            for (Path path : paths)
            {
                File file = path.toFile();
                if (!path.toString().endsWith(".java"))
                    continue;
                if (file.isDirectory())
                    continue;
                Class<?> c = Class.forName(pkg + "." +
                        file.getName().replace(".java", ""));
                if (c.getSuperclass() == clazz || Arrays.asList(c.getInterfaces()).contains(clazz))
                    classes.add((Class<? extends T>) c);
            }
            return classes;
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get classes of a type globally
     * @param clazz The type of class to search for
     * @param <T> The class type
     * @return A List of all classes that belong to the class provided
     */
    public static <T> List<Class<? extends T>> getSubclasses(Class<T> clazz)
    {
        File src = new File("./src");
        if (!src.exists())
            return null;
        String dir = !Arrays.asList(src.list()).contains("main") ?
                ProjectType.JAVA.getSourceDirectory() :
                ProjectType.MAVEN_GRADLE.getSourceDirectory();
        File parent = new File(dir);
        if (!parent.exists())
            return null;
        if (!parent.isDirectory())
            return null;
        try
        {
            List<Class<? extends T>> classes = new ArrayList<>();
            for (Path path : Files.walk(Paths.get(dir)).collect(Collectors.toList()))
            {
                File file = new File(path.toString());
                if (file.isDirectory())
                    continue;
                String[] spl = file.getAbsolutePath().split(dir.substring(2) + "\\\\");
                if (spl.length < 2)
                    continue;
                String name = spl[1].replace("\\", ".")
                        .replace(".java", "");
                Class<?> c = Class.forName(name);
                if (c.getSuperclass() != clazz)
                    continue;
                classes.add((Class<? extends T>) c);
            }
            return classes;
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private enum ProjectType
    {
        JAVA("./src"),
        MAVEN_GRADLE("./src/main/java");

        private final String sourceDirectory;

        ProjectType(String sourceDirectory)
        {
            this.sourceDirectory = sourceDirectory;
        }

        public String getSourceDirectory()
        {
            return sourceDirectory.replace("/", File.separator);
        }
    }
}