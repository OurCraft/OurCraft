package org.craft.spongeimpl;

import java.io.*;

import com.google.common.base.*;

import org.craft.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.events.world.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.spongeimpl.service.*;
import org.craft.spongeimpl.service.command.*;
import org.craft.spongeimpl.util.scheduler.*;
import org.craft.spongeimpl.util.title.*;
import org.craft.utils.*;
import org.spongepowered.api.*;
import org.spongepowered.api.Platform;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.*;
import org.spongepowered.api.service.command.*;
import org.spongepowered.api.service.event.*;
import org.spongepowered.api.service.scheduler.*;
import org.spongepowered.api.text.title.*;
import org.spongepowered.api.util.event.*;
import org.spongepowered.api.world.*;

@Mod(id = "spongeimpl", version = "1.0", name = "Sponge implementation")
public class SpoongeMod implements Game
{

    @Instance("spongeimpl")
    public static SpoongeMod          instance;
    private SpongePluginManager       pluginManager;
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
            serviceManager.setProvider(this, TitleBuilder.class, new SpoongeTitleBuilder());
        }
        catch(ProviderExistsException e)
        {
            e.printStackTrace();
        }
        scheduler = new SpoongeScheduler();
        gameRegistry = new SpoongeGameRegistry();
        this.gameInstance = event.getOurCraftInstance();
        pluginManager = new SpongePluginManager();
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
    }

    @OurModEventHandler
    public void onInit(ModInitEvent event)
    {
        eventManager.post(new SpongeInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onPostInit(ModPostInitEvent event)
    {
        eventManager.post(new SpongePostInitEvent(event.getOurCraftInstance()));
    }

    @OurModEventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        eventManager.post(new SpongeWorldLoadEvent(event.getOurCraftInstance(), (World) event.getWorld()));
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
