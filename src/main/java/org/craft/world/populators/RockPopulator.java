package org.craft.world.populators;

import java.util.*;

import org.craft.blocks.*;
import org.craft.world.*;

public class RockPopulator implements IWorldPopulator
{

    @Override
    public boolean populate(World world, Chunk c, Random rng)
    {
        if(c.getCoords().y <= 10)
        {
            c.fill(Blocks.stone);
            return true;
        }
        return false;
    }

}
