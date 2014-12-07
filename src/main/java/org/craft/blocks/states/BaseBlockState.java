package org.craft.blocks.states;

public class BaseBlockState extends BlockState
{

    private String id;

    public BaseBlockState(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
