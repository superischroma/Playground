package me.superischroma.playground.chemistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Compound extends HashSet<ElementQuantifiable>
{
    public static final int GRAMS = 0;
    public static final int MOLES = 1;

    private double quantity;
    private int mode;

    /**
     * Creates a new compound with the specified value, mode, and elements
     * @param quantity The amount of the compound; can be moles or grams
     * @param mode Magic value specified as grams or moles, which are constant fields in this class
     * @param collection The elements to be included in this compound
     * @throws IllegalStateException When the mode is invalid
     */
    public Compound(double quantity, int mode, Collection<ElementQuantifiable> collection)
    {
        super(collection);
        if (mode < 0 || mode > 1)
            throw new IllegalStateException("Invalid quantity mode!");
        this.quantity = quantity;
        this.mode = mode;
    }

    /**
     * Creates a new compound with the specified value, mode, and elements
     * @param quantity The amount of the compound; can be moles or grams
     * @param mode Magic value specified as grams or moles, which are constant fields in this class
     * @param elements The elements to be included in this compound
     * @throws IllegalStateException When the mode is invalid
     */
    public Compound(double quantity, int mode, ElementQuantifiable... elements)
    {
        super(Arrays.asList(elements));
        if (mode < 0 || mode > 1)
            throw new IllegalStateException("Invalid quantity mode!");
        this.quantity = quantity;
        this.mode = mode;
    }

    public void setQuantity(double quantity, int mode)
    {
        this.quantity = quantity;
        this.mode = mode;
    }

    public void setQuantity(double quantity)
    {
        setQuantity(quantity, mode);
    }

    public double getMoles()
    {
        if (mode == MOLES)
            return quantity;
        return quantity / getMolarMass();
    }

    /**
     * Gets the amount of grams specified by this compound.
     * If the mode is moles, it will convert the amount to grams.
     * @return The quantity of this compound in grams
     */
    public double getGrams()
    {
        if (mode == GRAMS)
            return quantity;
        return getMolarMass() * quantity;
    }

    public double getMolarMass()
    {
        double val = 0.0;
        for (ElementQuantifiable quantifiable : this)
            val += quantifiable.getElement().getAtomicMass() * quantifiable.getQuantity();
        return val;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder()
                .append(quantity)
                .append(mode == GRAMS ? "g " : " mol ");
        for (ElementQuantifiable quantifiable : this)
        {
            builder.append(quantifiable.getElement().getSymbol());
            if (quantifiable.getQuantity() != 1)
                builder.append(quantifiable.getQuantity());
        }
        builder.append(" (");
        if (mode == GRAMS)
            builder.append(getMoles()).append(" mol");
        else
            builder.append(getGrams()).append("g");
        builder.append(")");
        return builder.toString();
    }
}