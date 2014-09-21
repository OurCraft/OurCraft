package org.craft.utils;

public abstract class AbstractReference
{

    private int referenceCounter = 0;

    protected void increaseReferenceCounter()
    {
        referenceCounter++ ;
    }

    protected boolean decreaseReferenceCounter()
    {
        referenceCounter-- ;
        return referenceCounter <= 0;
    }
}
