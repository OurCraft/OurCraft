package org.craft.server;

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.modding.test.*;
import org.craft.network.*;
import org.craft.server.network.*;
import org.craft.spongeimpl.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.game.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.spongeimpl.tests.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.craft.world.populators.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;

public class OurCraftServer implements Game
{

    private NettyServerWrapper    serverWrapper;
    private SpongeGameRegistry    gameRegistry;
    private EventBus              eventBus;
    private SpongePluginManager   pluginManager;
    private int                   maxPlayers;

    private ArrayList<Player>     onlinePlayers;
    private AddonsLoader          addonsLoader;
    private boolean               nogui;
    private ServerGui             serverGui;
    private org.craft.world.World serverWorld;

    public OurCraftServer()
    {
        this(20);
    }

    public OurCraftServer(int maxPlayers)
    {
        onlinePlayers = new ArrayList<Player>();
        this.maxPlayers = maxPlayers;
        WorldGenerator gen = new WorldGenerator();
        gen.addPopulator(new RockPopulator());
        gen.addPopulator(new GrassPopulator());
        gen.addPopulator(new FlowerPopulator());
        gen.addPopulator(new TreePopulator());
        serverWorld = new org.craft.world.World("test-world", new BaseChunkProvider(), gen);
    }

    public void start(HashMap<String, String> properties)
    {
        nogui = Boolean.parseBoolean(properties.get("nogui"));
        if(!nogui)
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            serverGui = new ServerGui(this, "OurCraft server - " + getVersion());
            serverGui.pack();
            serverGui.setLocationRelativeTo(null);
            serverGui.setVisible(true);
            Log.addHandler(serverGui.getLogHandler());
        }
        Log.message("Loading game data");
        Blocks.init();
        Items.init();
        PacketRegistry.init();

        Log.message("Loading SpongeAPI implementation");
        initSponge();

        Log.message("Starting server");

        eventBus.call(new SpongeInitEvent(this));
        serverWrapper = new NettyServerWrapper(this, eventBus, Integer.parseInt(properties.get("port")));

        Log.message("Starting server connexion");
        eventBus.call(new SpongeServerAboutToStartEvent(this));
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
            addonsLoader.loadAddon(ModTest.class);
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
    public Collection<org.spongepowered.api.world.World> getWorlds()
    {
        ArrayList<org.spongepowered.api.world.World> worlds = new ArrayList<org.spongepowered.api.world.World>();
        worlds.add(serverWorld);
        return worlds;
    }

    @Override
    public World getWorld(UUID uniqueId)
    {
        return serverWorld;
    }

    @Override
    public World getWorld(String worldName)
    {
        return serverWorld;
    }

    @Override
    public void broadcastMessage(String message)
    {
        // TODO Packets!
        Log.message("[CHAT] " + message);
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

    public static String getVersion()
    {
        return "OurCraft:BuildNumber";
    }
}
