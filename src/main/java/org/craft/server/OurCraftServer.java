package org.craft.server;

import java.io.*;
import java.util.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.network.*;
import org.craft.server.network.*;
import org.craft.spongeimpl.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.game.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.spongeimpl.tests.*;
import org.craft.utils.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.world.*;

public class OurCraftServer implements Game
{

    private NettyServerWrapper  serverWrapper;
    private SpongeGameRegistry  gameRegistry;
    private EventBus            eventBus;
    private SpongePluginManager pluginManager;
    private int                 maxPlayers;

    private ArrayList<Player>   onlinePlayers;
    private AddonsLoader        addonsLoader;

    public OurCraftServer()
    {
        this(20);
    }

    public OurCraftServer(int maxPlayers)
    {
        onlinePlayers = new ArrayList<Player>();
        this.maxPlayers = maxPlayers;
    }

    public void start(HashMap<String, String> properties)
    {
        serverWrapper = new NettyServerWrapper(Integer.parseInt(properties.get("port")));
        Log.message("Loading game data");
        Blocks.init();
        Items.init();
        PacketRegistry.init();

        Log.message("Loading SpongeAPI implementation");
        initSponge();
        eventBus.call(new SpongeInitEvent(this));

        Log.message("Starting server connexion");
        new Thread(serverWrapper).start();

        eventBus.call(new SpongePostInitEvent(this));
        // TODO: Thog, it's your turn to code! :D
    }

    private void initSponge()
    {
        gameRegistry = new SpongeGameRegistry();
        eventBus = new EventBus();
        pluginManager = new SpongePluginManager();
        addonsLoader = new AddonsLoader(this, new File(SystemUtils.getGameFolder(), "mods"), eventBus);
        addonsLoader.registerAddonAnnotation(Plugin.class, pluginManager);
        try
        {
            addonsLoader.loadAddon(SpongeTestPlugin.class);
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Platform getPlatform()
    {
        return Platform.SERVER;
    }

    @Override
    public PluginManager getPluginManager()
    {
        return pluginManager;
    }

    @Override
    public EventManager getEventManager()
    {
        return eventBus;
    }

    @Override
    public GameRegistry getRegistry()
    {
        return gameRegistry;
    }

    @Override
    public Collection<Player> getOnlinePlayers()
    {
        return onlinePlayers;
    }

    @Override
    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    @Override
    public Player getPlayer(UUID uniqueId)
    {
        for(Player player : onlinePlayers)
        {
            if(player.getUniqueId().equals(uniqueId))
                return player;
        }
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
        // TODO Auto-generated method stub

    }

    @Override
    public String getAPIVersion()
    {
        return "UNKNOWN";
    }

    @Override
    public String getImplementationVersion()
    {
        return "UNKNOWN";
    }

}
