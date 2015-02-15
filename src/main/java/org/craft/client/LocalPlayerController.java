package org.craft.client;

import org.craft.blocks.Block;
import org.craft.blocks.Blocks;
import org.craft.entity.EntityPlayer;
import org.craft.inventory.PlayerInventory;
import org.craft.inventory.Stack;
import org.craft.utils.CollisionInfos;
import org.craft.utils.CollisionInfos.CollisionType;

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
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveLeft(speed);
    }

    @Override
    public void onMoveRightRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveRight(speed);
    }

    @Override
    public void onMoveForwardRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveForward(speed);
    }

    @Override
    public void onMoveBackwardsRequested()
    {
        float speed = 1f / 15f;
        if(!player.isOnGround())
            speed /= 1.25f;
        player.moveBackwards(speed);
    }

    @Override
    public void update()
    {
        if(!OurCraft.getOurCraft().getCurrentMenu().requiresMouse())
        {
            player.yaw += (float) Math.toRadians(OurCraft.getOurCraft().getMouseHandler().getDX()) * OurCraft.getOurCraft().getGameSettings().sensitivity.getValue();
            player.pitch += (float) Math.toRadians(-OurCraft.getOurCraft().getMouseHandler().getDY()) * OurCraft.getOurCraft().getGameSettings().sensitivity.getValue();
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
        if(infos.type == CollisionType.BLOCK)
        {
            Block b = player.worldObj.getBlockAt((int) Math.floor(infos.x), (int) Math.floor(infos.y), (int) Math.floor(infos.z));
            if(b != null && b.onBlockClicked(player.worldObj, (int) Math.floor(infos.x), (int) Math.floor(infos.y), (int) Math.floor(infos.z), player, player.getHeldItem()))
                return;
        }
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

    @Override
    public void onMouseWheelClicked()
    {
        OurCraft ourCraft = OurCraft.getOurCraft();
        CollisionInfos infos = ourCraft.getObjectInFront();
        if(infos != null && infos.type == CollisionType.BLOCK)
        {
            Block block = ((Block) infos.value);
            PlayerInventory inventory = (PlayerInventory) player.getInventory();

            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                Stack stack = inventory.getStackInSlot(i);
                if (stack != null)
                    if (block.getClass().isInstance(stack.getStackable())) inventory.setSelectedIndex((byte) i);
            }

            player.getInventory().setInventorySlotContents(((PlayerInventory) player.getInventory()).getSelectedIndex(), new Stack(block));
        }
    }
}
