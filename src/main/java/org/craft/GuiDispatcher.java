package org.craft;

import org.craft.entity.*;
import org.craft.network.*;
import org.craft.utils.*;

public abstract class GuiDispatcher
{

    private String      registryID;
    private NetworkSide netSide;

    public GuiDispatcher(String registryID, NetworkSide netSide)
    {
        this.registryID = registryID;
        this.netSide = netSide;
    }

    public NetworkSide getSide()
    {
        return netSide;
    }

    public boolean isClient()
    {
        return netSide == NetworkSide.CLIENT;
    }

    public boolean isServer()
    {
        return netSide == NetworkSide.SERVER;
    }

    public String getRegistryID()
    {
        return registryID;
    }

    /**
     * Opens a new gui.
     * @param registry The registry ID of the <b>first</b> registry to handle this event. 
     * If this implementation has delegates, the value of <code>registry</code> should be passed directly without modifications.
     * @param menuID The ID of the requested menu. It's the implementor's job to decide which menu would be opened by it or to completly ignore it.
     * @param player The instance of the player which caused the event or <code>null</code> if none.
     * @param infos The infos about where the player was aiming when creating the event. <b>Warning</b>: might be <code>null</code>
     */
    public abstract void openGui(String registry, int menuID, EntityPlayer player, CollisionInfos infos);
}
