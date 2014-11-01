package org.craft.modding;

import java.util.*;

public class ModManager implements IAddonManager
{

    private HashMap<String, ModContainer> mods;
    private ModHandler                    handler;

    public ModManager()
    {
        this.handler = new ModHandler();
        mods = new HashMap<String, ModContainer>();
    }

    @Override
    public AddonContainer getAddon(String id)
    {
        return mods.get(id);
    }

    @Override
    public Collection<AddonContainer> getAddons()
    {
        ArrayList<AddonContainer> containers = new ArrayList<AddonContainer>();
        for(ModContainer plugin : mods.values())
        {
            containers.add((AddonContainer) plugin);
        }
        return containers;
    }

    public IAddonHandler getHandler()
    {
        return handler;
    }

    @Override
    public void loadAddon(AddonContainer container)
    {
        mods.put(container.getId(), (ModContainer) container);
    }

}
