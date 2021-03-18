package me.superischroma.playground.chemistry;

import java.util.Objects;

public class ElementQuantifiable
{
    private Element element;
    private int quantity;

    public ElementQuantifiable(Element element, int quantity)
    {
        this.element = element;
        this.quantity = quantity;
    }

    public ElementQuantifiable(String elementResolvable, int quantity)
    {
        this(Element.resolve(elementResolvable), quantity);
    }

    public ElementQuantifiable(Element element)
    {
        this(element, 1);
    }

    public ElementQuantifiable(String elementResolvable)
    {
        this(elementResolvable, 1);
    }

    public Element getElement()
    {
        return element;
    }

    public void setElement(Element element)
    {
        this.element = element;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        return "ElementQuantifiable{element=" + element.name() + ", quantity=" + quantity + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementQuantifiable that = (ElementQuantifiable) o;
        return quantity == that.quantity &&
                element == that.element;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(element, quantity);
    }
}
