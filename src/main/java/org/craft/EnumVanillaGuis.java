package org.craft;

import java.util.*;

import org.craft.network.*;

public enum EnumVanillaGuis
{
    POWER_SOURCE_MODIFIER(0);

    private int id;

    private EnumVanillaGuis(int id)
    {
        this.id = id;
    }

    public int id()
    {
        return id;
    }

    public static EnumVanillaGuis fromID(int id)
    {
        for(EnumVanillaGuis gui : values())
        {
            if(gui.id == id)
                return gui;
        }
        return null;
    }

    public static void register(HashMap<String, GuiDispatcher> guiMap, NetworkSide side)
    {
        GuiDispatcher dispacher = new GuiVanillaDispatcher(OurCraftInstance.REGISTRIES_ID, side);
        guiMap.put(OurCraftInstance.REGISTRIES_ID, dispacher);
    }

}
