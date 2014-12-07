package org.craft.blocks.states;

public class BaseBlockStateValue implements IBlockStateValue
{

    private String id;

    public BaseBlockStateValue(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
