package org.craft.blocks;

import org.craft.client.render.*;

public class BlockGrass extends Block
{

    public BlockGrass(String id)
    {
        super(id);
    }

    public void registerIcons(IconGenerator register)
    {
        register.generateIcon("grass_top.png");
        register.generateIcon("grass_side.png");
        register.generateIcon("dirt.png");
    }

}
