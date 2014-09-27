package org.craft.spongeimpl.game;

import org.craft.blocks.*;
import org.spongepowered.api.*;
import org.spongepowered.api.item.*;

public class SpongeGameRegistry implements GameRegistry
{

    @Override
    public Block getBlock(String id)
    {
        return Blocks.get(id);
    }

    @Override
    public Item getItem(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getID(Object obj)
    {
        if(obj instanceof Block)
        {
            return ((Block) obj).getID();
        }
        return null;
    }
}
