package org.craft.server;

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.commands.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.network.*;
import org.craft.resources.*;
import org.craft.server.commands.*;
import org.craft.server.network.*;
import org.craft.server.network.packets.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.game.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.craft.world.loaders.*;
import org.craft.world.populators.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.util.command.*;

public class OurCraftServer implements Game, CommandSource
{

    private static OurCraftServer instance;
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

    private int                   frame;
    private int                   fps;
    private double                expectedFrameRate           = 60.0;
    private double                timeBetweenUpdates          = 1000000000 / expectedFrameRate;
    private final int             maxUpdatesBeforeRender      = 3;
    private double                lastUpdateTime              = System.nanoTime();
    private double                lastRenderTime              = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.
    private final double          TARGET_FPS                  = 60;
    private final double          TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private int                   lastSecondTime              = (int) (lastUpdateTime / 1000000000);
    private boolean               running;
    private AssetLoader           assetsLoader;
    private WorldLoader           worldLoader;

    public OurCraftServer()
    {
        this(20);
    }

    public OurCraftServer(int maxPlayers)
    {
        instance = this;
        assetsLoader = new AssetLoader(new ClasspathSimpleResourceLoader("assets"));
        onlinePlayers = new ArrayList<Player>();
        this.maxPlayers = maxPlayers;
        WorldGenerator gen = new WorldGenerator();
        gen.addPopulator(new RockPopulator());
        gen.addPopulator(new GrassPopulator());
        gen.addPopulator(new FlowerPopulator());
        gen.addPopulator(new TreePopulator());
        worldLoader = new VanillaWorldLoader(new ResourceLocation("world"), new DiskSimpleResourceLoader());
        serverWorld = new org.craft.world.World("test-world", new BaseChunkProvider(worldLoader), gen, worldLoader);
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
        Commands.init();
        Commands.register(new StopCommand(), "stop");
        Blocks.init();
        Items.init();
        try
        {
            I18n.init(assetsLoader);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PacketRegistry.init();

        Log.message("Loading SpongeAPI implementation");
        initSponge();

        Log.message("Starting server");

        eventBus.fireEvent(new SpongeInitEvent(this), null, null);
        serverWrapper = new NettyServerWrapper(this, eventBus, Integer.parseInt(properties.get("port")));

        Log.message("Starting server connexion");
        eventBus.fireEvent(new SpongeServerAboutToStartEvent(this), null, null);
        new Thread(serverWrapper).start();

        eventBus.fireEvent(new SpongePostInitEvent(this), null, null);
        running = true;

        expectedFrameRate = 60;
        timeBetweenUpdates = 1000000000 / expectedFrameRate;
        while(running)
        {
            tick();
        }
        if(serverGui != null)
            serverGui.dispose();
        serverWrapper.stop();
    }

    @SuppressWarnings("unchecked")
    private void initSponge()
    {
        gameRegistry = new SpongeGameRegistry();
        eventBus = new EventBus(SpongeEventHandler.class, OurModEventHandler.class);
        pluginManager = new SpongePluginManager();
        addonsLoader = new AddonsLoader(this, eventBus);
        addonsLoader.registerAddonAnnotation(Plugin.class, pluginManager);
        try
        {
            File modsFolder = new File(SystemUtils.getGameFolder(), "mods");
            if(!modsFolder.exists())
                modsFolder.mkdirs();
            File pluginsFolder = new File(SystemUtils.getGameFolder(), "plugins");
            if(!pluginsFolder.exists())
                pluginsFolder.mkdirs();
            addonsLoader.loadAll(modsFolder, pluginsFolder);
        }
        catch(Exception e)
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
            if(player.getDisplayName().equals(SessionManager.getInstance().getDisplayName(uniqueId)))
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
        Log.message("[CHAT] " + message);
        serverWrapper.sendPacketToAll(new S1ChatMessage(message));
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

    private final void tick()
    {
        double now = System.nanoTime();
        int updateCount = 0;

        {
            double delta = timeBetweenUpdates / 1000000000;
            while(now - lastUpdateTime > timeBetweenUpdates && updateCount < maxUpdatesBeforeRender)
            {

                update(delta);
                lastUpdateTime += timeBetweenUpdates;
                updateCount++ ;
            }

            if(now - lastUpdateTime > timeBetweenUpdates)
            {
                lastUpdateTime = now - timeBetweenUpdates;
            }

            lastRenderTime = now;
            // Update the frames we got.
            int thisSecond = (int) (lastUpdateTime / 1000000000);
            frame++ ;
            if(thisSecond > lastSecondTime)
            {
                fps = frame;
                frame = 0;
                lastSecondTime = thisSecond;
            }

            while(now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < timeBetweenUpdates)
            {
                Thread.yield();

                try
                {
                    Thread.sleep(1);
                }
                catch(Exception e)
                {
                }

                now = System.nanoTime();
            }
        }

    }

    private void update(double delta)
    {
        serverWorld.update(delta, true);
    }

    public static OurCraftServer getServer()
    {
        return instance;
    }

    public NettyServerWrapper getNettyWrapper()
    {
        return serverWrapper;
    }

    public World getServerWorld()
    {
        return serverWorld;
    }

    public WorldLoader getWorldLoader()
    {
        return worldLoader;
    }

    @Override
    public void sendMessage(String message)
    {
        Log.message("[Command] " + message);
    }

    public void shutdown()
    {
        running = false;
    }
}
