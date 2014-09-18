package org.craft.entity;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.util.*;
import org.craft.world.*;
import org.lwjgl.input.*;

public class EntityPlayer extends Entity
{

    public EntityPlayer(World world)
    {
        super(world);
        setSize(0.85f, 1.80f, 0.85f);
    }

    public void onEntityUpdate()
    {
        float speed = 1f / 10f;
        if(!isOnGround()) speed /= 3f;
        if(Keyboard.isKeyDown(Keyboard.KEY_Z))
        {
            this.moveForward(speed);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            this.moveBackwards(speed);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            this.moveLeft(speed);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.moveRight(speed);
        }

        rotate(Vector3.yAxis, (float)Math.toRadians(OurCraft.getOurCraft().getMouseHandler().getDX()));
        rotate(getRotation().getRight(), (float)Math.toRadians(-OurCraft.getOurCraft().getMouseHandler().getDY()));

        CollisionInfos infos = getObjectInFront(5f);
        System.out.println(infos.type + " at " + infos.x + "," + infos.y + "," + infos.z + " " + infos.value);
    }

    public float getEyeOffset()
    {
        return 1.8f;
    }

}
