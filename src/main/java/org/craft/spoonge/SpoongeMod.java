package org.craft.spoonge;

import java.io.*;

import com.google.common.base.*;

import org.craft.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.spoonge.events.*;
import org.craft.spoonge.events.state.*;
import org.craft.spoonge.events.world.*;
import org.craft.spoonge.plugin.*;
import org.craft.spoonge.service.*;
import org.craft.spoonge.service.command.*;
import org.craft.spoonge.util.scheduler.*;
import org.craft.utils.*;
import org.spongepowered.api.*;
import org.spongepowered.api.Platform;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.*;
import org.spongepowered.api.service.command.*;
import org.spongepowered.api.service.event.*;
import org.spongepowered.api.service.scheduler.*;
import org.spongepowered.api.util.event.*;
import org.spongepowered.api.world.*;

@Mod(id = "spoonge", version = "1.0", name = "Sponge implementation", author = "OurCraftTeam")
public class SpoongeMod implements Game
{

    @Instance("spoonge")
    public static SpoongeMod          instance;
    private SpoongePluginManager      pluginManager;
    private OurCraftInstance          gameInstance;
    private SpoongeEventManager       eventManager;
    private GameRegistry              gameRegistry;
    private SpoongeScheduler          scheduler;
    private SpoongeCommandsDispatcher commandDispatcher;
    private SpoongeServiceManager     serviceManager;
    private Server                    server;
    private OurCraftInstance          ocInstance;

    public SpoongeMod()
    {
        ;
    }

    @SuppressWarnings("unchecked")
    @OurModEventHandler
    public void onPreInit(ModPreInitEvent event)
    {
        ocInstance = event.getOurCraftInstance();
        if(event.getOurCraftInstance().isServer())
            server = new SpoongeServer(this);
        serviceManager = new SpoongeServiceManager();
        commandDispatcher = new SpoongeCommandsDispatcher();
        try
        {
            serviceManager.setProvider(this, CommandService.class, commandDispatcher);
        }
        catch(ProviderExistsException e)
        {
            e.printStackTrace();
        }
        scheduler = new SpoongeScheduler();
        gameRegistry = new SpoongeGameRegistry();
        this.gameInstance = event.getOurCraftInstance();
        pluginManager = new SpoongePluginManager();
        gameInstance.getEventBus().addEventClass(Event.class);
        gameInstance.getEventBus().addAnnotationClass(Subscribe.class);
        eventManager = new SpoongeEventManager(gameInstance.getEventBus());
        AddonsLoader addonsLoader = gameInstance.getAddonsLoader();
        addonsLoader.registerAddonAnnotation(Plugin.class, pluginManager);
        File pluginsFolder = new File(SystemUtils.getGameFolder(), "plugins");
        if(!pluginsFolder.exists())
            pluginsFolder.mkdirs();
        addonsLoader.exclude(Mod.class);
        addonsLoader.loadAll(pluginsFolder);
        addonsLoader.exclude();

        gameInstance.getEventBus().register(new SpoongeBlockEventsListener(this));
    }

    @OurModEventHandler
    public void onInit(ModInitEvent event)
    {
        eventManager.post(new SpoongeInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onPostInit(ModPostInitEvent event)
    {
        eventManager.post(new SpoongePostInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        eventManager.post(new SpoongeWorldLoadEvent(event.getOurCraftInstance(), (World) event.getWorld()));
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
    public String getAPIVersion()
    {
        return "Release 1.0";
    }

    @Override
    public String getImplementationVersion()
    {
        return "Spoonge v0.5";
    }

    @Override
    public ServiceManager getServiceManager()
    {
        return serviceManager;
    }

    @Override
    public CommandService getCommandDispatcher()
    {
        return commandDispatcher;
    }

    @Override
    public Optional<Server> getServer()
    {
        return Optional.of(server);
    }

    public OurCraftInstance getOurCraftInstance()
    {
        return ocInstance;
    }

}
