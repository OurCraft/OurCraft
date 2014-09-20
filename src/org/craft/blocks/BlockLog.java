package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockLog extends Block
{

    private TextureIcon sideIcon;
    private TextureIcon topAndBottomIcon;

    public BlockLog(String id)
    {
        super(id);
    }

    public TextureIcon getBlockIcon(World world, int x, int y, int z, EnumSide side)
    {
        if(side == EnumSide.BOTTOM || side == EnumSide.TOP) return topAndBottomIcon;
        return sideIcon;
    }

    public void registerIcons(IconGenerator register)
    {
        sideIcon = register.generateIcon(getID() + ".png");
        topAndBottomIcon = register.generateIcon(getID() + "_bottom_and_top.png");
    }

}
