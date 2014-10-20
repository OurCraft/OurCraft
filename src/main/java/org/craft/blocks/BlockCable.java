package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockCable extends Block implements IPowerableBlock
{

    private static AABB selectionBB;

    static
    {
        selectionBB = new AABB(Vector3.get(0f, 0f, 0f), Vector3.get(1f, 2f / 16f, 1f));
    }

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
        AABB result = selectionBB.translate(translation);
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

        int northFlag = northBlock instanceof IPowerableBlock ? 1 << 0 : 0;
        int southFlag = southBlock instanceof IPowerableBlock ? 1 << 1 : 0;
        int eastFlag = eastBlock instanceof IPowerableBlock ? 1 << 2 : 0;
        int westFlag = westBlock instanceof IPowerableBlock ? 1 << 3 : 0;
        int fullFlag = (northFlag | southFlag | eastFlag | westFlag);
        world.setBlockState(x, y, z, BlockStates.cableConnexions, EnumConnexionStates.fromFlag(fullFlag), false);
        world.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.getFromValue(world.getDirectElectricPowerAt(x, y, z) - 1), false);
    }

    public void registerIcons(IconGenerator register)
    {
        register.generateIcon(getID() + "_four.png");
        for(EnumConnexionStates state : EnumConnexionStates.values())
        {
            register.generateIcon(getID() + "_" + state.toString() + ".png");
        }
    }
}
