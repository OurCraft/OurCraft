package org.craft.utils;

public abstract class AbstractReference
{

    private int     referenceCounter = 0;
    private boolean disposable       = true;

    protected void increaseReferenceCounter()
    {
        referenceCounter++ ;
    }

    protected boolean decreaseReferenceCounter()
    {
        referenceCounter-- ;
        return referenceCounter == 0;
    }

    public void setDisposable(boolean disposable)
    {
        this.disposable = disposable;
    }

    public boolean isDisposable()
    {
        return disposable;
    }
}
