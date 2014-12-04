package org.craft.modding.events.block;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.modding.events.*;
import org.craft.world.*;

public abstract class ModBlockEvent extends ModEvent
{

    private Block block;
    private World w;
    private int   x;
    private int   y;
    private int   z;

    public ModBlockEvent(OurCraftInstance instance, World w, int x, int y, int z, Block block)
    {
        super(instance);
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public World getWorld()
    {
        return w;
    }

    public Block getBlock()
    {
        return block;
    }

}
