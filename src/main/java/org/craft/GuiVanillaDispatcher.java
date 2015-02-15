package org.craft;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.entity.*;
import org.craft.network.*;
import org.craft.utils.*;

public class GuiVanillaDispatcher extends GuiDispatcher
{

    public GuiVanillaDispatcher(String registryID, NetworkSide netSide)
    {
        super(registryID, netSide);
    }

    @Override
    public void openGui(String registry, int menuID, EntityPlayer player, CollisionInfos infos)
    {
        EnumVanillaGuis gui = EnumVanillaGuis.fromID(menuID);
        switch(gui)
        {
            case POWER_SOURCE_MODIFIER:
            {
                if(isClient())
                {
                    OurCraft.getOurCraft().openMenu(new GuiPowerSourceModifier(OurCraft.getOurCraft(), player.worldObj, (int) Math.floor(infos.x), (int) Math.floor(infos.y), (int) Math.floor(infos.z)));
                }
                break;
            }

            case INVENTORY:
            {
                if(isClient())
                {
                    OurCraft.getOurCraft().openMenu(new GuiInventory(OurCraft.getOurCraft(), player));
                }
                break;
            }
        }
    }
}
