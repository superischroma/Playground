package me.superischroma.playground.school;

import java.util.Arrays;

/**
 * A place for horses to be stored.
 */
public class HorseBarn
{
    private final Horse[] spaces;

    /**
     * Creates a new horse barn.
     * @param horses Horses to be instantly put into the barn.
     */
    public HorseBarn(Horse... horses)
    {
        this.spaces = horses;
    }

    /**
     * Attempts to find the space of the horse with the specified name.
     * @param name The name to search for.
     * @return The index of the horse found, otherwise -1.
     */
    private int findHorseSpace(String name)
    {
        for (int i = 0; i < spaces.length; i++)
        {
            if (spaces[i] == null) continue;
            if (name.equals(spaces[i].getName()))
                return i;
        }
        return -1;
    }

    /**
     * Consolidates the horses in the barn by moving all
     * animals near each other and away from null values.
     */
    public void consolidate()
    {
        Arrays.sort(spaces, (o1, o2) ->
        {
            if (o1 == null)
                return 1;
            if (o2 == null)
                return -1;
            return 0;
        });
    }

    /**
     * @return The horses inside the barn.
     */
    public Horse[] getSpaces()
    {
        return spaces;
    }
}