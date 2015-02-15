package org.craft.client.network;

import org.craft.client.*;
import org.craft.entity.*;
import org.craft.network.packets.*;
import org.craft.utils.*;

public class RemotePlayerController extends PlayerController
{

    private OurCraft oc;

    public RemotePlayerController(EntityPlayer player, OurCraft oc)
    {
        super(player);
        this.oc = oc;
    }

    @Override
    public void onSneakRequested()
    {
        ;
    }

    @Override
    public void onJumpRequested()
    {
        oc.sendPacket(new C3PlayerJump());
    }

    @Override
    public void onMoveLeftRequested()
    {
        oc.sendPacket(new C2MovePlayer(C2MovePlayer.LEFT));
    }

    @Override
    public void onMoveRightRequested()
    {
        oc.sendPacket(new C2MovePlayer(C2MovePlayer.RIGHT));
    }

    @Override
    public void onMoveForwardRequested()
    {
        oc.sendPacket(new C2MovePlayer(C2MovePlayer.FORWARD));
    }

    @Override
    public void onMoveBackwardsRequested()
    {
        oc.sendPacket(new C2MovePlayer(C2MovePlayer.BACKWARDS));
    }

    @Override
    public void onLeftClick(CollisionInfos infos)
    {
        ;
    }

    @Override
    public void onRightClick(CollisionInfos infos)
    {
        ;
    }

    @Override
    public void onMouseWheelMoved(int amount)
    {
        ;
    }

    @Override
    public void onMouseWheelClicked()
    {
        ;
    }

    @Override
    public void update()
    {
        ;
    }

}
