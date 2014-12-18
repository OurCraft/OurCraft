package org.craft.world.biomes;

import org.craft.blocks.*;
import org.craft.world.populators.*;

public class BiomeMountains extends Biome
{

    private Block surfaceBlock;
    private int   maxHeight;

    public BiomeMountains(String id, float temp, int maxHeight, Block surfaceBlock)
    {
        super(id, temp);
        this.maxHeight = maxHeight;
        this.surfaceBlock = surfaceBlock;
    }

    @Override
    public void initPopulators()
    {
        addPopulator(new RockPopulator());
        addPopulator(new MountainsPopulator(Blocks.stone, Blocks.dirt, surfaceBlock, maxHeight, 11));
        addPopulator(new TreePopulator());
    }

}
