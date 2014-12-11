package org.craft.blocks;

import org.craft.entity.*;
import org.craft.world.*;

public class BlockTnT extends Block
{

    public BlockTnT(String id)
    {
        super(id);
    }

    public void onDestroyedByExplosion(Explosion explosion, World world, int blockX, int blockY, int blockZ)
    {
        EntityPrimedTNT tnt = new EntityPrimedTNT(world);
        tnt.setLocation(blockX, blockY, blockZ);
        tnt.velY = 0.1f;
        tnt.setFuse(15L);
        world.spawn(tnt);
    }

}
