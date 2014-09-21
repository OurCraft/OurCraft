package org.craft.world;

import java.util.*;

public interface IWorldPopulator
{

    public boolean populate(World world, Chunk c, Random rng);
}
