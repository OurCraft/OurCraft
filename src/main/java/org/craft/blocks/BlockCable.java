package org.craft.blocks;

import java.util.*;

import org.craft.blocks.states.*;
import org.craft.client.render.*;
import org.craft.entity.*;
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

    public void onBlockUpdate(World world, int x, int y, int z, ArrayList<Vector3> visited)
    {
        EnumConnexionStates conState = handleConnections(world, x, y, z);

        EnumPowerStates oldPower = ((EnumPowerStates) world.getBlockState(x, y, z, BlockStates.electricPower));
        int oldValue = -1;
        if(oldPower != null)
            oldValue = oldPower.powerValue();
        int powerValue = world.getDirectElectricPowerAt(x, y, z) - 1;
        if(oldValue == powerValue)
            return;
        if(conState == EnumConnexionStates.NONE && powerValue > 0)
            powerValue = 0;
        world.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.fromPowerValue(powerValue), false);
        world.setBlockState(x, y, z, BlockStates.powered, BlockStates.getValue(BlockStates.powered, powerValue > 0 ? "true" : "false"), false);

        world.updateBlockNeighbors(x, y, z, false, visited);

        int newPowerValue = world.getDirectElectricPowerAt(x, y, z) - 1;
        if(powerValue > newPowerValue)
        {
            world.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.POWER_0, false);
            world.setBlockState(x, y, z, BlockStates.powered, BlockStates.getValue(BlockStates.powered, "false"), true);
        }
    }

    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        super.onBlockAdded(w, x, y, z, side, placer);
        handleConnections(w, x, y, z);
        w.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.POWER_0, false);
        w.setBlockState(x, y, z, BlockStates.powered, BlockStates.getValue(BlockStates.powered, "false"), true);
        onBlockUpdate(w, x, y, z, null);
    }

    private EnumConnexionStates handleConnections(World world, int x, int y, int z)
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
        EnumConnexionStates conState = EnumConnexionStates.fromFlag(fullFlag);
        world.setBlockState(x, y, z, BlockStates.cableConnexions, conState, false);
        return conState;
    }

}
