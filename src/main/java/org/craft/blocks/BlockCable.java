package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockCable extends Block
{

    private TextureIcon allIcon;
    private TextureIcon nsIcon;

    public BlockCable(String id)
    {
        super(id);
    }

    public boolean isSolid()
    {
        return false;
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return null;
    }

    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        Vector3 translation = Vector3.get(x, y, z);
        AABB result = normalCubeAABB.translate(translation);
        translation.dispose();
        return result;
    }

    public boolean shouldRenderInPass(EnumRenderPass pass)
    {
        return pass == EnumRenderPass.ALPHA;
    }

    public boolean letLightGoThrough()
    {
        return true;
    }

    public void onBlockUpdate(World world, int x, int y, int z)
    {
        Block northBlock = world.getBlockNextTo(x, y, z, EnumSide.NORTH);
        Block southBlock = world.getBlockNextTo(x, y, z, EnumSide.SOUTH);
        Block eastBlock = world.getBlockNextTo(x, y, z, EnumSide.EAST);
        Block westBlock = world.getBlockNextTo(x, y, z, EnumSide.WEST);

        int northFlag = northBlock == this ? 1 << 0 : 0;
        int southFlag = southBlock == this ? 1 << 1 : 0;
        int eastFlag = eastBlock == this ? 1 << 2 : 0;
        int westFlag = westBlock == this ? 1 << 3 : 0;
        int fullFlag = (northFlag | southFlag | eastFlag | westFlag);
        world.setBlockState(x, y, z, BlockStates.cableConnexions, EnumConnexionStates.fromFlag(fullFlag), false);
    }

    public TextureIcon getBlockIcon(World w, int x, int y, int z, EnumSide side)
    {
        IBlockStateValue value = w.getBlockState(x, y, z, BlockStates.cableConnexions);
        if(value == null)
            return allIcon;
        if(value instanceof EnumConnexionStates)
        {
            switch((EnumConnexionStates) value)
            {
                case ALL:
                    return allIcon;
                case NONE:
                    return allIcon;
                case NORTH_SOUTH:
                    return nsIcon;
                default:
                    return allIcon;
            }
        }
        return allIcon;
    }

    public void registerIcons(IconGenerator register)
    {
        allIcon = register.generateIcon(getID() + "_four.png");
        nsIcon = register.generateIcon(getID() + "_ns.png");
    }
}
