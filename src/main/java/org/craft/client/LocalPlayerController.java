package org.craft.client;

import org.craft.blocks.*;
import org.craft.entity.*;
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
        player.yaw += (float) Math.toRadians(OurCraft.getOurCraft().getMouseHandler().getDX());
        player.pitch += (float) Math.toRadians(-OurCraft.getOurCraft().getMouseHandler().getDY());
    }

    @Override
    public void onLeftClick(CollisionInfos infos)
    {
        if(infos.type == CollisionInfos.CollisionType.BLOCK)
            player.worldObj.setBlock((int) Math.round(infos.x), (int) Math.round(infos.y), (int) Math.round(infos.z), Blocks.air);
    }

    @Override
    public void onRightClick(CollisionInfos infos)
    {
        if(infos.type != CollisionInfos.CollisionType.BLOCK)
            return;
        int x = (int) infos.x + infos.side.getTranslationX();
        int y = (int) (infos.y) + infos.side.getTranslationY();
        int z = (int) (infos.z + infos.side.getTranslationZ());
        player.worldObj.setBlock(x, y, z, Blocks.log);
        Blocks.log.onBlockAdded(player.worldObj, x, y, z, infos.side, player);
    }
}
