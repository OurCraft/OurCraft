package org.craft.modding.test;

import org.craft.blocks.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.modding.events.state.*;
import org.slf4j.*;

@Mod(id = "modtest", name = "Test mod", version = "1.0")
public class ModTest
{

    private Logger logger;

    public ModTest()
    {
    }

    @ModEventHandler
    public void onPreInit(ModPreInitEvent evt)
    {
        logger = evt.getLogger();
        logger.debug("PreInit!!!");
        Block block = new Block("testModding");
        evt.getOurCraftInstance().registerBlock(block);
    }

    @ModEventHandler
    public void onPostInit(ModPostInitEvent evt)
    {
        logger.debug("PostInit!!! " + getClass());
    }

    @ModEventHandler
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("Loading world " + evt.getWorld().getName());
    }

    @ModEventHandler
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("Unloading world " + evt.getWorld().getName());
    }

    @ModEventHandler
    public void onInit(ModInitEvent evt)
    {
        logger.debug("Init!!!");
    }
}
