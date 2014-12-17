package org.craft.spoonge.events.entity;

import com.flowpowered.math.vector.*;
import com.google.common.collect.*;

import org.craft.*;
import org.craft.spoonge.events.entity.*;
import org.craft.spoonge.world.*;
import org.craft.world.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.entity.*;
import org.spongepowered.api.world.*;

public class SpoongeEntitySpawnEvent extends SpoongeEntityEvent implements EntitySpawnEvent
{

    private Location location;

    public SpoongeEntitySpawnEvent(OurCraftInstance instance, Entity e, ILocatable loc)
    {
        this(instance, e, loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public SpoongeEntitySpawnEvent(OurCraftInstance instance, Entity e, float x, float y, float z)
    {
        super(instance, e);
        location = new Location(new SpoongeExtent(new BlockLoc[0], Lists.newArrayList(e)), new Vector3d(x, y, z));
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
