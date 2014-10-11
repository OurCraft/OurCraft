package org.craft.client;

import org.craft.entity.*;
import org.craft.utils.*;

public abstract class PlayerController
{

    protected EntityPlayer player;

    public PlayerController(EntityPlayer player)
    {
        this.player = player;
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public abstract void onSneakRequested();

    public abstract void onJumpRequested();

    public abstract void onMoveLeftRequested();

    public abstract void onMoveRightRequested();

    public abstract void onMoveForwardRequested();

    public abstract void onMoveBackwardsRequested();

    public abstract void onLeftClick(CollisionInfos infos);

    public abstract void onRightClick(CollisionInfos infos);

    public abstract void onMouseWheelMoved(int amount);

    public abstract void update();
}
