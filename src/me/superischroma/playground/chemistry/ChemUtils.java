package me.superischroma.playground.chemistry;

public final class ChemUtils
{
    public static Compound stoi(Compound insertion, Compound currentResult, Compound currentInsertion)
    {
        double quantity = (insertion.getGrams() * currentResult.getMoles() *
                currentResult.getMolarMass()) / (currentInsertion.getMoles() *
                currentInsertion.getMolarMass());
        return new Compound(quantity, Compound.GRAMS, currentResult);
    }
}