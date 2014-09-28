package org.craft.entity;

import java.util.*;

import org.craft.utils.*;
import org.craft.world.*;
import org.spongepowered.api.entity.*;

public class EntityPlayer extends EntityLiving implements Player
{
    private UUID   uuid;
    private String name;
    private String displayName;

    public EntityPlayer(World world, UUID uuid)
    {
        super(world);
        this.uuid = uuid;
        this.name = SessionManager.getInstance().getName(uuid);
        this.displayName = SessionManager.getInstance().getDisplayName(uuid);
        setSize(0.75f, 1.80f, 0.75f);
    }

    public float getEyeOffset()
    {
        return 1.7f;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public UUID generateUUID()
    {
        return uuid;
    }
}
