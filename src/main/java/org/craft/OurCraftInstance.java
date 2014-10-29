package org.craft;

import org.craft.modding.*;
import org.craft.modding.events.*;
import org.spongepowered.api.*;

public interface OurCraftInstance
{

    GameRegistry getRegistry();

    void broadcastMessage(String message);

    EventBus getEventBus();

    AddonsLoader getAddonsLoader();

    boolean isClient();

    boolean isServer();
}
