package org.craft.spoonge;

import org.craft.modding.*;
import org.craft.modding.events.entity.*;
import org.craft.spoonge.events.entity.*;

public class SpoongeEntityEventsListener
{

    private SpoongeMod mod;

    public SpoongeEntityEventsListener(SpoongeMod mod)
    {
        this.mod = mod;
    }

    @ModEventHandler
    public void onEntityDeath(ModEntityDeathEvent evt)
    {
        evt.setCancelled(mod.getEventManager().post(new SpoongeEntityDeathEvent(mod.getOurCraftInstance(), (org.spongepowered.api.entity.Entity) evt.getEntity())));
    }

    @ModEventHandler
    public void onEntitySpawn(ModEntitySpawnEvent evt)
    {
        evt.setCancelled(mod.getEventManager().post(new SpoongeEntitySpawnEvent(mod.getOurCraftInstance(), (org.spongepowered.api.entity.Entity) evt.getEntity(), evt.getEntity())));
    }
}
