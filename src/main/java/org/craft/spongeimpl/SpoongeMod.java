package org.craft.spongeimpl;

import java.io.*;
import java.util.*;

import com.google.common.base.Optional;

import org.craft.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.events.world.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.spongeimpl.util.scheduler.*;
import org.craft.utils.*;
import org.spongepowered.api.*;
import org.spongepowered.api.Platform;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.scheduler.*;
import org.spongepowered.api.world.*;

@Mod(id = "spongeimpl", version = "1.0", name = "Sponge implementation")
public class SpoongeMod implements Game
{

    @Instance("spongeimpl")
    public static SpoongeMod    instance;
    private SpongePluginManager pluginManager;
    private OurCraftInstance    gameInstance;
    private SpoongeEventManager eventManager;
    private GameRegistry        gameRegistry;
    private SpoongeScheduler    scheduler;

    public SpoongeMod()
    {
    }

    @OurModEventHandler
    public void onPreInit(ModPreInitEvent event)
    {
        scheduler = new SpoongeScheduler();
        gameRegistry = new SpoongeGameRegistry();
        this.gameInstance = event.getOurCraftInstance();
        pluginManager = new SpongePluginManager();
        gameInstance.getEventBus().addEventClass(Event.class);
        gameInstance.getEventBus().addAnnotationClass(SpongeEventHandler.class);
        eventManager = new SpoongeEventManager(gameInstance.getEventBus());
        AddonsLoader addonsLoader = gameInstance.getAddonsLoader();
        addonsLoader.registerAddonAnnotation(Plugin.class, pluginManager);
        File pluginsFolder = new File(SystemUtils.getGameFolder(), "plugins");
        if(!pluginsFolder.exists())
            pluginsFolder.mkdirs();
        addonsLoader.loadAll(pluginsFolder);
    }

    @OurModEventHandler
    public void onInit(ModInitEvent event)
    {
        eventManager.call(new SpongeInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onPostInit(ModPostInitEvent event)
    {
        eventManager.call(new SpongePostInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        eventManager.call(new SpongeWorldLoadEvent(event.getOurCraftInstance(), (World) event.getWorld()));
    }

    @Override
    public Platform getPlatform()
    {
        return gameInstance.isClient() ? Platform.CLIENT : Platform.SERVER;
    }

    @Override
    public PluginManager getPluginManager()
    {
        return pluginManager;
    }

    @Override
    public EventManager getEventManager()
    {
        return eventManager;
    }

    @Override
    public GameRegistry getRegistry()
    {
        return gameRegistry;
    }

    @Override
    public Scheduler getScheduler()
    {
        return scheduler;
    }

    @Override
    public Collection<Player> getOnlinePlayers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxPlayers()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Optional<Player> getPlayer(UUID uniqueId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Player> getPlayer(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<World> getWorlds()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public World getWorld(UUID uniqueId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public World getWorld(String worldName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void broadcastMessage(String message)
    {
        gameInstance.broadcastMessage(message);
    }

    @Override
    public String getAPIVersion()
    {
        return "1.0";
    }

    @Override
    public String getImplementationVersion()
    {
        return "Spoonge v0.1";
    }

}
