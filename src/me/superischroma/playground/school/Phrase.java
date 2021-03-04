package me.superischroma.playground.school;

/**
 * Represents a manipulable phrase.
 */
public class Phrase
{
    private String currentPhrase;

    /**
     * Creates a new phrase.
     * @param p A String to start with.
     */
    public Phrase(String p)
    {
        this.currentPhrase = p;
    }

    /**
     * Finds the Nth occurrence of str within the phrase.
     * @param str The search
     * @param n Which occurrence to get
     * @return The index of the Nth occurrence, -1 otherwise
     */
    public int findNthOccurrence(String str, int n)
    {
        String dupe = currentPhrase;
        int distance = 0;
        int reduction = str.length();
        for (int i = 0; i < n; i++)
        {
            if (!dupe.contains(str))
                return -1;
            distance = dupe.indexOf(str) + (i * reduction);
            dupe = dupe.replaceFirst(str, "");
        }
        return distance;
    }

    /**
     * Replaces the Nth occurrence of str within the phrase with repl.
     * @param str The search
     * @param n Which occurrence to get
     * @param repl The replacement value for str at n
     */
    public void replaceNthOccurrence(String str, int n, String repl)
    {
        int i = findNthOccurrence(str, n);
        if (i == -1)
            return;
        currentPhrase = currentPhrase.substring(0, i) + repl + currentPhrase.substring(str.length() + i);
    }

    /**
     * Finds the last occurrence of str within the phrase.
     * @param str The search
     * @return The index of the last occurrence
     */
    public int findLastOccurrence(String str)
    {
        int occurrences = 1;
        while (findNthOccurrence(str, occurrences) != -1)
            occurrences++;
        return findNthOccurrence(str, occurrences - 1);
    }

    /**
     * @return The phrase as a string
     */
    public String toString()
    {
        return currentPhrase;
    }
}