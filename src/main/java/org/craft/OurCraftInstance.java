package org.craft;

import java.util.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.resources.*;
import org.spongepowered.api.*;

public interface OurCraftInstance
{

    String REGISTRIES_ID = "ourcraft";

    GameRegistry getRegistry();

    void broadcastMessage(String message);

    EventBus getEventBus();

    AddonsLoader getAddonsLoader();

    boolean isClient();

    boolean isServer();

    HashMap<String, GuiDispatcher> getGuiMap();

    void registerGuiHandler(String registry, GuiDispatcher dispatcher);

    void registerBlock(Block block);

    void registerItem(Item item);

    AssetLoader getAssetsLoader();
}
