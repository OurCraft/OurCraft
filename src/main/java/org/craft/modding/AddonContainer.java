package org.craft.modding;

import java.lang.annotation.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.items.*;

public abstract class AddonContainer<T extends Annotation>
{

    private T                addonAnnot;
    private Object           addonInstance;
    private List<Block> addonBlocks;
    private List<Item>  addonItems;

    public AddonContainer(T addonAnnot, Object instance)
    {
        this.addonAnnot = addonAnnot;
        this.addonInstance = instance;
        addonBlocks = Lists.newArrayList();
        addonItems = Lists.newArrayList();
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getVersion();

    public T getAddonAnnotation()
    {
        return addonAnnot;
    }

    public Object getInstance()
    {
        return addonInstance;
    }

    public void registerBlock(Block block)
    {
        addonBlocks.add(block);
    }

    public void registerItem(Item item)
    {
        addonItems.add(item);
    }
}
