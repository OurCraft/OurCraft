package org.craft.server;

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.commands.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.network.*;
import org.craft.network.packets.*;
import org.craft.resources.*;
import org.craft.server.commands.*;
import org.craft.server.network.*;
import org.craft.spongeimpl.game.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.craft.world.loaders.*;
import org.craft.world.populators.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.plugin.*;

public class OurCraftServer implements OurCraftInstance
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
    private int                   ups;
    private double                expectedFrameRate  = 60.0;
    private double                timeBetweenUpdates = 1000000000 / expectedFrameRate;
    private double                lastUpdateTime     = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.

    private int                   lastSecondTime     = (int) (lastUpdateTime / 1000000000);
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
        worldLoader = new VanillaWorldLoader(new ResourceLocation("worlds"), new DiskSimpleResourceLoader());
        serverWorld = new ServerWorld("remote-world", new BaseChunkProvider(worldLoader), gen, worldLoader);
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
        BlockStates.init();
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

        // TODO:  eventBus.fireEvent(new SpongeInitEvent(this), null, null);
        serverWrapper = new NettyServerWrapper(this, eventBus, Integer.parseInt(properties.get("port")));

        Log.message("Starting server connexion");
        // TODO:    eventBus.fireEvent(new SpongeServerAboutToStartEvent(this), null, null);
        new Thread(serverWrapper).start();

        // TODO:   eventBus.fireEvent(new SpongePostInitEvent(this), null, null);
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
        eventBus = new EventBus(new Class<?>[]
        {
            ModEvent.class
        }, OurModEventHandler.class);
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

    public EventBus getEventBus()
    {
        return eventBus;
    }

    @Override
    public GameRegistry getRegistry()
    {
        return gameRegistry;
    }

    @Override
    public void broadcastMessage(String message)
    {
        Log.message("[CHAT] " + message);
        serverWrapper.sendPacketToAll(new S1ChatMessage(message));
    }

    public static String getVersion()
    {
        return "OurCraft:BuildNumber";
    }

    private final void tick()
    {
        double now = System.nanoTime();

        {
            double delta = timeBetweenUpdates / 1000000000;
            while(now - lastUpdateTime > timeBetweenUpdates)
            {
                update(delta);
                lastUpdateTime += timeBetweenUpdates;
            }

            if(now - lastUpdateTime > timeBetweenUpdates)
            {
                lastUpdateTime = now - timeBetweenUpdates;
            }

            // Update the frames we got.
            int thisSecond = (int) (lastUpdateTime / 1000000000);
            frame++ ;
            if(thisSecond > lastSecondTime)
            {
                ups = frame;
                frame = 0;
                lastSecondTime = thisSecond;
            }

            while(now - lastUpdateTime < timeBetweenUpdates)
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
        serverWorld.update(delta);
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

    public void shutdown()
    {
        running = false;
    }

    @Override
    public AddonsLoader getAddonsLoader()
    {
        return addonsLoader;
    }

    @Override
    public boolean isClient()
    {
        return false;
    }

    @Override
    public boolean isServer()
    {
        return true;
    }

}
