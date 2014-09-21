package org.craft.world;

import java.util.*;

public interface IWorldPopulator
{

    /**
     * Populates given chunk
     */
    public boolean populate(World world, Chunk c, Random rng);
}
