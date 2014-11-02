package org.craft.client;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.inventory.*;
import org.craft.utils.*;

public class LocalPlayerController extends PlayerController
{

    public LocalPlayerController(EntityPlayer player)
    {
        super(player);
    }

    @Override
    public void onSneakRequested()
    {

    }

    @Override
    public void onJumpRequested()
    {
        player.jump();
    }

    @Override
    public void onMoveLeftRequested()
    {
        float speed = 1f / 10f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveLeft(speed);
    }

    @Override
    public void onMoveRightRequested()
    {
        float speed = 1f / 10f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveRight(speed);
    }

    @Override
    public void onMoveForwardRequested()
    {
        float speed = 1f / 10f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveForward(speed);
    }

    @Override
    public void onMoveBackwardsRequested()
    {
        float speed = 1f / 10f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveBackwards(speed);
    }

    public void update()
    {
        if(!OurCraft.getOurCraft().getCurrentMenu().requiresMouse())
        {
            player.yaw += (float) Math.toRadians(OurCraft.getOurCraft().getMouseHandler().getDX()) * OurCraft.getOurCraft().getGameSettings().sensitivity.getValueAsFloat();
            player.pitch += (float) Math.toRadians(-OurCraft.getOurCraft().getMouseHandler().getDY()) * OurCraft.getOurCraft().getGameSettings().sensitivity.getValueAsFloat();
        }
    }

    @Override
    public void onLeftClick(CollisionInfos infos)
    {
        if(infos.type == CollisionInfos.CollisionType.BLOCK)
        {
            Blocks.air.onBlockAdded(player.worldObj, (int) Math.round(infos.x), (int) Math.round(infos.y), (int) Math.round(infos.z), infos.side, player);
            player.worldObj.setBlock((int) Math.round(infos.x), (int) Math.round(infos.y), (int) Math.round(infos.z), Blocks.air);
        }
    }

    @Override
    public void onRightClick(CollisionInfos infos)
    {
        org.craft.inventory.Stack s = player.getHeldItem();
        if(s != null)
        {
            s.getStackable().onUse(player, infos.x, infos.y, infos.z, infos.side, infos.type);
        }
    }

    @Override
    public void onMouseWheelMoved(int amount)
    {
        PlayerInventory inv = ((PlayerInventory) player.getInventory());
        byte newIndex = (byte) (inv.getSelectedIndex() - Math.signum(amount));
        if(newIndex < 0)
            newIndex = (byte) (10 + newIndex);
        if(newIndex >= 10)
            newIndex = (byte) (newIndex - 10);
        inv.setSelectedIndex(newIndex);
    }
}
