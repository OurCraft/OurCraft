package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockGrass extends Block
{

    private TextureIcon topIcon;
    private TextureIcon sideIcon;
    private TextureIcon bottomIcon;

    public BlockGrass(String id)
    {
        super(id);
    }

    public TextureIcon getBlockIcon(World w, int x, int y, int z, EnumSide side)
    {
        if(side == EnumSide.TOP)
        {
            return topIcon;
        }
        else
            if(side == EnumSide.BOTTOM)
            {
                return bottomIcon;
            }
            else
                return sideIcon;
    }

    public void registerIcons(IconGenerator register)
    {
        this.topIcon = register.generateIcon("grass_top.png");
        this.sideIcon = register.generateIcon("grass_side.png");
        this.bottomIcon = register.generateIcon("dirt.png");
    }

}
