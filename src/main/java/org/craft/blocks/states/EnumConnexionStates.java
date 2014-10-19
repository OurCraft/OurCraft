package org.craft.blocks.states;

import org.craft.utils.*;

public enum EnumConnexionStates implements IBlockStateValue
{
    NONE("none"), ALL("all"),

    EAST_ONLY("e"),
    NORTH_ONLY("n"),
    WEST_ONLY("w"),
    SOUTH_ONLY("s"),

    NORTH_SOUTH("ns"), NORTH_EAST("ne"), NORTH_WEST("nw"),
    SOUTH_EAST("se"), SOUTH_WEST("sw"), EAST_WEST("ew"),

    NORTH_WEST_SOUTH("nws"), NORTH_EAST_SOUTH("nes"), EAST_SOUTH_WEST("esw"), EAST_NORTH_WEST("enw");

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

    public static IBlockStateValue fromFlag(int fullFlag)
    {
        Log.message(">> " + Integer.toBinaryString(fullFlag));
        int northModifier = 1 << 0;
        int southModifier = 1 << 1;
        int eastModifier = 1 << 2;
        int westModifier = 1 << 3;
        if(fullFlag == 0)
            return NONE;
        if(fullFlag == (northModifier | southModifier | eastModifier | westModifier))
            return ALL;

        String full = "";
        if((fullFlag & northModifier) != 0)
        {
            full += "n";
        }
        if((fullFlag & southModifier) != 0)
        {
            full += "s";
        }
        if((fullFlag & eastModifier) != 0)
        {
            full += "e";
        }
        if((fullFlag & westModifier) != 0)
        {
            full += "w";
        }

        stateLoop: for(EnumConnexionStates state : values())
        {
            String id = state.id;
            if(id.length() == full.length())
            {
                for(char c : id.toCharArray())
                {
                    if(full.indexOf(c) < 0)
                    {
                        Log.message(id + " != " + full);
                        continue stateLoop;
                    }
                }
                Log.message(id + " == " + full);
                return state;
            }
        }
        return null;
    }
}
