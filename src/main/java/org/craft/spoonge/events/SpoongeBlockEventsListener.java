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

    @OurModEventHandler
    public void onBlockUpdate(ModBlockChangeEvent changeEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(changeEvent.getBlock(), changeEvent.getX(), changeEvent.getY(), changeEvent.getZ(), changeEvent.getWorld());
        SpoongeBlockState state = new SpoongeBlockState();
        state.setType((BlockType) changeEvent.getReplacementBlock());
        mod.getEventManager().post(new SpoongeBlockChangeEvent(mod.getOurCraftInstance(), blockWrapper, new SpoongeBlockSnapshot(state)));
    }

    @OurModEventHandler
    public void onBlockInteract(ModBlockInteractEvent changeEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(changeEvent.getBlock(), changeEvent.getX(), changeEvent.getY(), changeEvent.getZ(), changeEvent.getWorld());
        mod.getEventManager().post(new SpoongeBlockInteractEvent(mod.getOurCraftInstance(), blockWrapper));
    }

    @OurModEventHandler
    public void onBlockUpdate(ModBlockUpdateEvent changeEvent)
    {
        SpoongeBlockWrapper blockWrapper = new SpoongeBlockWrapper(changeEvent.getBlock(), changeEvent.getX(), changeEvent.getY(), changeEvent.getZ(), changeEvent.getWorld());
        mod.getEventManager().post(new SpoongeBlockUpdateEvent(mod.getOurCraftInstance(), blockWrapper, (BlockType) changeEvent.getCauseBlock()));
    }
}
