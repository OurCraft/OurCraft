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

    /**
     * Called when the sneak key is pressed
     */
    public abstract void onSneakRequested();

    /**
     * Called when the jump key is pressed
     */
    public abstract void onJumpRequested();

    /**
     * Called when the strafe left key is pressed
     */
    public abstract void onMoveLeftRequested();

    /**
     * Called when the strafe right key is pressed
     */
    public abstract void onMoveRightRequested();

    /**
     * Called when the move forward key is pressed
     */
    public abstract void onMoveForwardRequested();

    /**
     * Called when the move backwards key is pressed
     */
    public abstract void onMoveBackwardsRequested();

    /**
     * Called when the left click key is pressed
     */
    public abstract void onLeftClick(CollisionInfos infos);

    /**
     * Called when the right click key is pressed
     */
    public abstract void onRightClick(CollisionInfos infos);

    /**
     * Called when the mouse wheel is moved
     */
    public abstract void onMouseWheelMoved(int amount);

    /**
     * Called when the mouse wheel is clicked
     */
    public abstract void onMouseWheelClicked();

    /**
     * Called each tick
     */
    public abstract void update();
}
