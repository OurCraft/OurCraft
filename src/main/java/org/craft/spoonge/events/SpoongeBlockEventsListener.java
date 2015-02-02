package org.craft.spoonge.events;

import org.craft.modding.*;
import org.craft.modding.events.block.*;
import org.craft.spoonge.*;
import org.craft.spoonge.block.*;
import org.craft.spoonge.events.block.*;
import org.craft.spoonge.modifiers.*;
import org.spongepowered.api.block.*;

public class SpoongeBlockEventsListener
{

    private SpoongeMod mod;

    public SpoongeBlockEventsListener(SpoongeMod mod)
    {
        this.mod = mod;
    }

    @ModEventHandler
    public void onBlockChange(ModBlockChangeEvent changeEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(changeEvent.getBlock(), changeEvent.getX(), changeEvent.getY(), changeEvent.getZ(), changeEvent.getWorld());
        SpoongeBlockState state = new SpoongeBlockState();
        state.setType((BlockType) changeEvent.getReplacementBlock());
        changeEvent.setCancelled(mod.getEventManager().post(new SpoongeBlockChangeEvent(mod.getOurCraftInstance(), blockWrapper, new SpoongeBlockSnapshot(state))));
    }

    @ModEventHandler
    public void onBlockInteract(ModBlockInteractEvent interactEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(interactEvent.getBlock(), interactEvent.getX(), interactEvent.getY(), interactEvent.getZ(), interactEvent.getWorld());
        interactEvent.setCancelled(mod.getEventManager().post(new SpoongeBlockInteractEvent(mod.getOurCraftInstance(), blockWrapper)));
    }

    @ModEventHandler
    public void onBlockUpdate(ModBlockUpdateEvent updateEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(updateEvent.getBlock(), updateEvent.getX(), updateEvent.getY(), updateEvent.getZ(), updateEvent.getWorld());
        updateEvent.setCancelled(mod.getEventManager().post(new SpoongeBlockUpdateEvent(mod.getOurCraftInstance(), blockWrapper, (BlockType) updateEvent.getCauseBlock())));
    }
}
