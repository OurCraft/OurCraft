package org.craft.blocks.states;

public enum EnumConnexionStates implements IBlockStateValue
{
    NONE("none"), ALL("all"), NORTH_SOUTH("ns");

    private String id;

    private EnumConnexionStates(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
