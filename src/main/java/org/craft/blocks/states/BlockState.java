package org.craft.blocks.states;

public abstract class BlockState
{
    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public abstract String toString();
}
