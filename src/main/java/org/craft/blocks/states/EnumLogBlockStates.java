package org.craft.blocks.states;

public enum EnumLogBlockStates implements IBlockStateValue
{
    UP("up"), LYING_NS("lying_ns"), LYING_WE("lying_we");

    private String id;

    private EnumLogBlockStates(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }

}
