package org.craft.modding;

import java.lang.reflect.Constructor;
import java.util.*;

import com.google.common.collect.*;

public class ModManager implements IAddonManager<Mod>
{

    private HashMap<String, ModContainer> mods;
    private ModHandler                    handler;

    public ModManager()
    {
        this.handler = new ModHandler();
        mods = new HashMap<String, ModContainer>();
    }

    @Override
    public AddonContainer<Mod> getAddon(String id)
    {
        return mods.get(id);
    }

    @Override
    public Collection<AddonContainer<Mod>> getAddons()
    {
        List<AddonContainer<Mod>> containers = Lists.newArrayList();
        for(ModContainer plugin : mods.values())
        {
            containers.add(plugin);
        }
        return containers;
    }

    @Override
    public IAddonHandler<Mod> getHandler()
    {
        return handler;
    }

    @Override public Constructor getAddonConstructor(Class<?> clazz) throws NoSuchMethodException
    {
        return clazz.getConstructor();
    }

    @Override public Object[] getConstructorArgs()
    {
        return new Object[0];
    }

    @Override
    public void loadAddon(AddonContainer<Mod> container)
    {
        mods.put(container.getId(), (ModContainer) container);
    }

}
