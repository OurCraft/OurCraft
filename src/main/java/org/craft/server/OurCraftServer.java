package org.craft.server;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;

import javax.swing.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.commands.*;
import org.craft.items.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.modding.events.state.*;
import org.craft.network.*;
import org.craft.network.packets.*;
import org.craft.resources.*;
import org.craft.server.commands.*;
import org.craft.server.network.*;
import org.craft.utils.*;
import org.craft.utils.crash.*;
import org.craft.world.*;
import org.craft.world.loaders.*;

public class OurCraftServer implements OurCraftInstance, ICommandSender
{

    private static OurCraftServer          instance;
    private NettyServerWrapper             serverWrapper;
    private GlobalRegistry                 gameRegistry;
    private EventBus                       eventBus;
    private int                            maxPlayers;

    private AddonsLoader                   addonsLoader;
    private boolean                        nogui;
    private ServerGui                      serverGui;
    private org.craft.world.World          serverWorld;

    private int                            frame;
    private int                            ups;
    private double                         expectedFrameRate  = 60.0;
    private double                         timeBetweenUpdates = 1000000000 / expectedFrameRate;
    private double                         lastUpdateTime     = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.

    private int                            lastSecondTime     = (int) (lastUpdateTime / 1000000000);
    private boolean                        running;
    private AssetLoader                    assetsLoader;
    private WorldLoader                    worldLoader;
    private HashMap<String, GuiDispatcher> guiMap;

    public OurCraftServer()
    {
        this(20);
    }

    public OurCraftServer(int maxPlayers)
    {
        guiMap = Maps.newHashMap();
        EnumVanillaGuis.register(guiMap, NetworkSide.SERVER);
        instance = this;
        assetsLoader = new AssetLoader(new ClasspathSimpleResourceLoader("assets"));
        this.maxPlayers = maxPlayers;
        WorldGenerator gen = new WorldGenerator();
        worldLoader = new VanillaWorldLoader(new ResourceLocation("worlds"), new DiskSimpleResourceLoader());
        serverWorld = new ServerWorld("remote-world", new BaseChunkProvider(worldLoader), gen, worldLoader);
    }

    public void start(Map<String, String> properties)
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
            //Log.addHandler(serverGui.getLogHandler());
        }
        Log.message("Loading game data");
        Commands.init();
        Commands.register(new StopCommand(), "stop");
        Blocks.init();
        BlockStates.init();
        Items.init();
        gameRegistry = new GlobalRegistry();
        try
        {
            I18n.init(assetsLoader);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PacketRegistry.init();

        Log.message("Loading addons...");
        List<Class<? extends Annotation>> annots = Lists.newArrayList();
        annots.add(ModEventHandler.class);
        eventBus = new EventBus(new Class<?>[]
        {
                ModEvent.class
        }, annots);
        addonsLoader = new AddonsLoader(this, eventBus);
        File modsFolder = new File(SystemUtils.getGameFolder(), "mods");
        if(!modsFolder.exists())
            modsFolder.mkdirs();
        addonsLoader.loadAll(modsFolder);

        Log.message("Starting server");

        eventBus.fireEvent(new ModInitEvent(this), null, null);
        serverWrapper = new NettyServerWrapper(this, eventBus, Integer.parseInt(properties.get("port")));

        Log.message("Starting server connexion");
        eventBus.fireEvent(new ModServerAboutStartingEvent(this), null, null);
        new Thread(serverWrapper).start();

        eventBus.fireEvent(new ModPostInitEvent(this), null, null);
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

    @Override
    public EventBus getEventBus()
    {
        return eventBus;
    }

    @Override
    public GlobalRegistry getRegistry()
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

    @Override
    public void sendMessage(String text)
    {
        Log.message("[COMMAND] " + text);
    }

    @Override
    public HashMap<String, GuiDispatcher> getGuiMap()
    {
        return guiMap;
    }

    @Override
    public void registerGuiHandler(String registry, GuiDispatcher dispatcher)
    {
        guiMap.put(registry, dispatcher);
    }

    @Override
    public void registerBlock(Block block)
    {
        AddonContainer<?> container = CommonHandler.getCurrentContainer();
        if(container != null)
            container.registerBlock(block);
        block.setContainer(container);
        Blocks.register(block);
    }

    @Override
    public void registerItem(Item item)
    {
        AddonContainer<?> container = CommonHandler.getCurrentContainer();
        if(container != null)
            container.registerItem(item);
        item.setContainer(container);
        Items.register(item);
    }

    @Override
    public AssetLoader getAssetsLoader()
    {
        return assetsLoader;
    }

    @Override
    public void crash(CrashReport crashReport)
    {
        crashReport.printStack();
        System.exit(-1);
    }
}
