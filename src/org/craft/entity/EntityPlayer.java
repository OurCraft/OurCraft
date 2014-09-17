package org.craft.entity;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.world.*;
import org.lwjgl.input.*;

public class EntityPlayer extends Entity
{

    public EntityPlayer(World world)
    {
        super(world);
    }

    public void onEntityUpdate()
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_Z))
        {
            this.moveForward(1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            this.moveBackwards(1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            this.moveLeft(1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.moveRight(1);
        }

        rotate(Vector3.yAxis, (float)Math.toRadians(OurCraft.getOurCraft().getMouseHandler().getDX()));
        rotate(getRotation().getRight(), (float)Math.toRadians(-OurCraft.getOurCraft().getMouseHandler().getDY()));
    }

}
