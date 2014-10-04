package org.craft.client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.*;
import java.util.*;

import org.craft.blocks.*;
import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.blocks.*;
import org.craft.client.render.entity.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.entity.Entity;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.network.*;
import org.craft.resources.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.events.world.*;
import org.craft.spongeimpl.game.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.crash.*;
import org.craft.world.*;
import org.lwjgl.input.*;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;

public class OurCraft implements Runnable, Game
{

    private int                      displayWidth                = 960;
    private int                      displayHeight               = 540;
    private long                     lastTime                    = 0;
    private boolean                  running                     = true;
    private RenderEngine             renderEngine                = null;
    private AssetLoader              assetsLoader;
    private RenderBlocks             renderBlocks;
    private World                    clientWorld;
    private MouseHandler             mouseHandler;
    private EntityPlayer             player;
    private static OurCraft          instance;
    private CollisionInfos           objectInFront               = null;
    private OpenGLBuffer             crosshairBuffer;
    private FallbackRender<Entity>   fallbackRenderer;
    private Runtime                  runtime;
    private FontRenderer             fontRenderer;
    private String                   username;

    private Gui                      currentMenu;
    private Gui                      newMenu;
    private OpenGLBuffer             selectionBoxBuffer;
    private DiskSimpleResourceLoader gameFolderLoader;
    private PlayerController         playerController;
    private SpongeGameRegistry       gameRegistry;
    private EventBus                 eventBus;
    private SpongePluginManager      pluginManager;
    private Session                  session;
    private RenderItems              renderItems;
    private AddonsLoader             addonsLoader;

    private int                      frame;
    private int                      fps;
    private double                   expectedFrameRate           = 60.0;
    private double                   timeBetweenUpdates          = 1000000000 / expectedFrameRate;
    private final int                maxUpdatesBeforeRender      = 2;
    private double                   lastUpdateTime              = System.nanoTime();
    private double                   lastRenderTime              = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.
    private final double             TARGET_FPS                  = 60;
    private final double             TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private int                      lastSecondTime              = (int) (lastUpdateTime / 1000000000);

    public OurCraft()
    {
        instance = this;

        this.assetsLoader = new AssetLoader(new ClasspathSimpleResourceLoader("assets"));
        this.gameFolderLoader = new DiskSimpleResourceLoader(SystemUtils.getGameFolder().getAbsolutePath());
        runtime = Runtime.getRuntime();
    }

    public void start(HashMap<String, String> properties)
    {
        username = properties.get("username");
        new Thread(this).start();
    }

    public void run()
    {
        try
        {
            AL.create();
            objectInFront = new CollisionInfos();
            objectInFront.type = CollisionType.NONE;
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
            ContextAttribs context = new ContextAttribs(3, 3).withProfileCompatibility(true).withDebug(true);
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            Display.setResizable(true);
            Display.setTitle("OurCraft - " + getVersion());
            Display.create(new PixelFormat(), context);
            mouseHandler = new MouseHandler();

            renderEngine = new RenderEngine(assetsLoader);
            renderEngine.enableGLCap(GL_BLEND);
            renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            renderEngine.switchToOrtho();
            renderEngine.renderSplashScreen();
            Display.update();

            session = SessionManager.getInstance().registerPlayer(UUID.randomUUID(), username, username);
            initSponge();

            fontRenderer = new BaseFontRenderer();
            OpenGLHelper.loadCapNames();

            Blocks.init();
            Items.init();
            PacketRegistry.init();
            I18n.init(assetsLoader);
            eventBus.fireEvent(new SpongeInitEvent(this), null, null);

            Log.message("==== IN en_US.lang ====");
            Log.message("lang.test1 is " + I18n.format("lang.test1"));
            Log.message("lang.test2 is " + I18n.format("lang.test2", 0.2));
            Log.message("lang.test3 is " + I18n.format("lang.test3", "Test"));
            Log.message("lang.test4 is " + I18n.format("lang.test4", 12));

            I18n.setCurrentLanguage("en_US");
            Log.message("==== IN fr_FR.lang ====");
            Log.message("lang.test1 is " + I18n.format("lang.test1"));
            Log.message("lang.test2 is " + I18n.format("lang.test2", 0.2));
            Log.message("lang.test3 is " + I18n.format("lang.test3", "Test"));
            Log.message("lang.test4 is " + I18n.format("lang.test4", 12));

            renderBlocks = new RenderBlocks(renderEngine);
            renderBlocks.registerBlockRenderer(BlockFlower.class, new BlockFlowerRenderer());
            renderBlocks.registerBlockRenderer(BlockHalfSlab.class, new BlockHalfSlabRenderer());
            renderItems = new RenderItems(renderEngine);
            fallbackRenderer = new FallbackRender<Entity>();
            openMenu(new GuiMainMenu(fontRenderer));

            crosshairBuffer = new OpenGLBuffer();
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 - 8, 0), Vector2.get(0, 0)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 - 8, 0), Vector2.get(1, 0)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 + 8, 0), Vector2.get(1, 1)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 + 8, 0), Vector2.get(0, 1)));

            crosshairBuffer.addIndex(0);
            crosshairBuffer.addIndex(1);
            crosshairBuffer.addIndex(2);

            crosshairBuffer.addIndex(2);
            crosshairBuffer.addIndex(3);
            crosshairBuffer.addIndex(0);
            crosshairBuffer.upload();
            crosshairBuffer.clearAndDisposeVertices();

            selectionBoxBuffer = new OpenGLBuffer();
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(0, 0, 0))); //0
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(0, 0, 1))); //1
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(0, 1, 0))); //2
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(0, 1, 1))); //3
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(1, 0, 0))); //4
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(1, 0, 1))); //5
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(1, 1, 0))); //6
            selectionBoxBuffer.addVertex(new Vertex(Vector3.get(1, 1, 1))); //7

            selectionBoxBuffer.addIndex(0);
            selectionBoxBuffer.addIndex(1);
            selectionBoxBuffer.addIndex(2);
            selectionBoxBuffer.addIndex(3);
            selectionBoxBuffer.addIndex(4);
            selectionBoxBuffer.addIndex(5);
            selectionBoxBuffer.addIndex(6);
            selectionBoxBuffer.addIndex(7);

            selectionBoxBuffer.addIndex(0);
            selectionBoxBuffer.addIndex(4);
            selectionBoxBuffer.addIndex(2);
            selectionBoxBuffer.addIndex(6);
            selectionBoxBuffer.addIndex(1);
            selectionBoxBuffer.addIndex(5);
            selectionBoxBuffer.addIndex(3);
            selectionBoxBuffer.addIndex(7);

            selectionBoxBuffer.addIndex(0);
            selectionBoxBuffer.addIndex(2);
            selectionBoxBuffer.addIndex(4);
            selectionBoxBuffer.addIndex(6);
            selectionBoxBuffer.addIndex(5);
            selectionBoxBuffer.addIndex(7);
            selectionBoxBuffer.addIndex(1);
            selectionBoxBuffer.addIndex(3);

            selectionBoxBuffer.upload();
            selectionBoxBuffer.clearAndDisposeVertices();
            running = true;

            eventBus.fireEvent(new SpongePostInitEvent(this), null, null);

            expectedFrameRate = 60;
            timeBetweenUpdates = 1000000000 / expectedFrameRate;
            while(running && !Display.isCloseRequested())
            {
                tick();
            }
            cleanup();
            System.exit(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    public void openMenu(Gui gui)
    {
        this.newMenu = gui;
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

            render(delta);
            Display.update();

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

    private void update(final double delta)
    {
        if(player != null)
        {
            objectInFront = player.getObjectInFront(5f);
        }
        if(newMenu != currentMenu)
        {
            currentMenu = newMenu;
            if(currentMenu != null)
                currentMenu.init();
        }
        while(Mouse.next())
        {
            int mouseButton = Mouse.getEventButton();
            boolean state = Mouse.getEventButtonState();
            int x = Mouse.getEventX();
            int y = displayHeight - Mouse.getEventY();
            if(currentMenu != null && state && mouseButton != -1)
            {
                currentMenu.handleButtonReleased(x, y, mouseButton);

                if(playerController != null)
                {
                    if(mouseButton == 0)
                    {
                        playerController.onLeftClick(getObjectInFront());
                    }
                    else
                    {
                        playerController.onRightClick(getObjectInFront());
                    }
                }
            }

            // TODO: Event queue
        }
        while(Keyboard.next())
        {
            int id = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();
            boolean state = Keyboard.getEventKeyState();
            if(currentMenu != null)
            {
                if(!state)
                {
                    currentMenu.keyPressed(id, c);
                }
                else
                {
                    if(id == Keyboard.KEY_ESCAPE)
                    {
                        if(clientWorld != null && !(currentMenu instanceof GuiPauseMenu))
                        {
                            openMenu(new GuiPauseMenu(fontRenderer));
                        }
                        else if(clientWorld == null)
                            running = false;
                    }
                    if(id == Keyboard.KEY_BACK)
                    {
                        Log.fatal("Testing fatal crashes");
                    }
                    if(id == Keyboard.KEY_RETURN)
                    {
                        try
                        {
                            setResourcesPack("test.zip");
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    currentMenu.keyReleased(id, c);
                }
            }
        }
        if(currentMenu != null)
        {
            currentMenu.update();
            if(currentMenu.requiresMouse())
                mouseHandler.ungrab();
            else
                mouseHandler.grab();
        }
        else
            mouseHandler.grab();
        mouseHandler.update();
        boolean canUpdate = (System.currentTimeMillis() - lastTime) >= delta;
        if(canUpdate)
        {
            if(playerController != null)
            {
                if(Keyboard.isKeyDown(Keyboard.KEY_Z))
                {
                    playerController.onMoveForwardRequested();
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_S))
                {
                    playerController.onMoveBackwardsRequested();
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_Q))
                {
                    playerController.onMoveLeftRequested();
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_D))
                {
                    playerController.onMoveRightRequested();
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    playerController.onJumpRequested();
                }
                playerController.update();
            }
            this.resetTime();
        }
        if(clientWorld != null)
        {
            if(currentMenu != null && !currentMenu.pausesGame())
            {
                clientWorld.update(delta, canUpdate);
            }
            else if(currentMenu == null)
                clientWorld.update(delta, canUpdate);
        }
    }

    public void resetTime()
    {
        lastTime = System.currentTimeMillis();
    }

    public long getLastTime()
    {
        return lastTime;
    }

    private void render(double delta)
    {
        renderEngine.begin();
        ArrayList<Chunk> visiblesChunks = new ArrayList<Chunk>();
        if(player != null)
        {
            int renderDistance = 8;
            int ox = (int) renderEngine.getRenderViewEntity().getX();
            int oy = (int) renderEngine.getRenderViewEntity().getY();
            int oz = (int) renderEngine.getRenderViewEntity().getZ();
            for(int x = -renderDistance; x < renderDistance; x++ )
            {
                for(int y = -renderDistance; y < renderDistance; y++ )
                {
                    for(int z = -renderDistance; z < renderDistance; z++ )
                    {
                        int fx = x * 16 + ox;
                        int fy = y * 16 + oy;
                        int fz = z * 16 + oz;
                        if(fy < 0)
                            continue;
                        synchronized(clientWorld)
                        {
                            Chunk c = clientWorld.getChunkProvider().get(clientWorld, (int) Math.floor((float) fx / 16f), (int) Math.floor((float) fy / 16f), (int) Math.floor((float) fz / 16f));
                            if(c != null)
                                visiblesChunks.add(c);
                        }
                    }
                }
            }
        }
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glClearColor(0, 0.6666667f, 1, 1);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        renderEngine.enableGLCap(GL_DEPTH_TEST);
        renderEngine.switchToPerspective();
        if(clientWorld != null)
        {
            renderBlocks.render(clientWorld, visiblesChunks);
            for(Entity e : clientWorld.getEntitiesList())
            {
                if(e != renderEngine.getRenderViewEntity())
                {
                    fallbackRenderer.render(renderEngine, e, (float) e.getX(), (float) e.getY(), (float) e.getZ());
                }
            }
            glClear(GL_DEPTH_BUFFER_BIT);
            renderEngine.disableGLCap(GL_DEPTH_TEST);
            if(objectInFront != null && objectInFront.type == CollisionType.BLOCK)
            {
                renderEngine.bindLocation(null);
                Matrix4 modelView = renderEngine.getModelviewMatrix();
                AABB blockSelectBB = ((Block) objectInFront.value).getSelectionBox(clientWorld, 0, 0, 0);
                Vector3 ratio = blockSelectBB.getMaxExtents().sub(blockSelectBB.getMinExtents());
                float sx = ratio.getX();
                float sy = ratio.getY();
                float sz = ratio.getZ();
                Matrix4 selectionBoxMatrix = new Matrix4().initTranslation(objectInFront.x + blockSelectBB.getMinExtents().getX(), objectInFront.y + blockSelectBB.getMinExtents().getY(), objectInFront.z + blockSelectBB.getMinExtents().getZ()).mul(new Matrix4().initScale(sx, sy, sz));
                renderEngine.setModelviewMatrix(selectionBoxMatrix);
                renderEngine.renderBuffer(selectionBoxBuffer, GL_LINES);
                renderEngine.setModelviewMatrix(modelView);
            }
            renderEngine.switchToOrtho();
        }
        else
        {
            glClear(GL_DEPTH_BUFFER_BIT);
            renderEngine.disableGLCap(GL_DEPTH_TEST);
            renderEngine.switchToOrtho();
        }
        renderEngine.end();
        if(clientWorld != null)
        {
            renderEngine.enableGLCap(GL_COLOR_LOGIC_OP);
            glLogicOp(GL_XOR);
            renderEngine.bindLocation(new ResourceLocation("ourcraft", "textures/crosshair.png"));
            renderEngine.renderBuffer(crosshairBuffer);
            renderEngine.disableGLCap(GL_COLOR_LOGIC_OP);
        }

        int mx = Mouse.getX();
        int my = Mouse.getY();
        if(currentMenu != null)
            currentMenu.draw(mx, displayHeight - my, renderEngine);

        printIfGLError();
    }

    public void setResourcesPack(String fileName) throws Exception
    {
        ResourceLocation location = new ResourceLocation("resourcepacks", fileName);
        ZipSimpleResourceLoader loader = new ZipSimpleResourceLoader(gameFolderLoader.getResource(location), "assets");
        this.assetsLoader.setResourcePackLoader(loader);
        renderEngine.loadShaders();
        renderEngine.reloadLocations();
    }

    public static void printIfGLError()
    {
        int errorFlag = glGetError();
        // If an error has occurred...
        if(errorFlag != GL_NO_ERROR)
        {
            // Print the error to System.err.
            Log.error("[GL ERROR] " + gluErrorString(errorFlag));
        }
    }

    public String getClientUsername()
    {
        return username;
    }

    public static OurCraft getOurCraft()
    {
        return instance;
    }

    public ResourceLoader getAssetsLoader()
    {
        return assetsLoader;
    }

    public MouseHandler getMouseHandler()
    {
        return mouseHandler;
    }

    public CollisionInfos getObjectInFront()
    {
        return objectInFront;
    }

    public boolean isRunning()
    {
        return running;
    }

    public World getClientWorld()
    {
        return clientWorld;
    }

    public RenderEngine getRenderEngine()
    {
        return renderEngine;
    }

    public static String getVersion()
    {
        return "OurCraft:BuildNumber";
    }

    public int getDisplayWidth()
    {
        return displayWidth;
    }

    public int getDisplayHeight()
    {
        return displayHeight;
    }

    public Runtime getRuntimeInfos()
    {
        return runtime;
    }

    public long getFreeMemory()
    {
        return runtime.freeMemory();
    }

    public long getMaxMemory()
    {
        return runtime.maxMemory();
    }

    public long getTotalMemory()
    {
        return runtime.totalMemory();
    }

    public long getUsedMemory()
    {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public void setWorld(World world)
    {
        if(world == null)
        {
            if(clientWorld != null)
                eventBus.fireEvent(new SpongeWorldUnloadEvent(this, clientWorld), null, null);
        }
        else
        {
            try
            {
                world.getLoader().loadWorldConstants(world);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            eventBus.fireEvent(new SpongeWorldLoadEvent(this, world), null, null);
        }
        this.clientWorld = world;
    }

    public void setPlayer(EntityPlayer player)
    {
        this.player = player;
    }

    public void shutdown()
    {
        running = false;
    }

    /**
     * Convenience method that disposes of the world and change the current screen
     */
    public void quitToMainScreen()
    {
        saveWorld();
        setWorld(null);
        setPlayer(null);
        openMenu(new GuiMainMenu(fontRenderer));
    }

    public void saveWorld()
    {
        WorldLoader loader = clientWorld.getLoader();
        ByteDataBuffer buffer = new ByteDataBuffer();
        try
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds/" + clientWorld.getName());
            if(!worldFolder.exists())
                worldFolder.mkdirs();
            File chunkFolder = new File(worldFolder, "chunkData");
            if(!chunkFolder.exists())
                chunkFolder.mkdirs();
            loader.writeWorldConstants(buffer, clientWorld);
            buffer.flush();
            buffer.close();
            File worldData = new File(worldFolder, "world.data");
            if(!worldData.exists())
                worldData.createNewFile();
            FileOutputStream worldDataOut = new FileOutputStream(worldData);
            worldDataOut.write(buffer.toBytes());
            worldDataOut.flush();
            worldDataOut.close();
            Iterator<Chunk> chunks = clientWorld.getChunkProvider().iterator();
            while(chunks.hasNext())
            {
                ByteDataBuffer regionBuffer = new ByteDataBuffer();
                Chunk chunk = chunks.next();
                loader.writeChunk(regionBuffer, chunk, chunk.getCoords().x, chunk.getCoords().y, chunk.getCoords().z);
                regionBuffer.flush();
                regionBuffer.close();
                byte[] regionRawData = regionBuffer.toBytes();
                File regionFile = new File(worldFolder, "chunkData/chunk" + chunk.getCoords().x + "." + chunk.getCoords().y + "." + chunk.getCoords().z + ".data");
                FileOutputStream out = new FileOutputStream(regionFile);
                out.write(regionRawData);
                out.flush();
                out.close();
            }
        }
        catch(Exception e)
        {
            crash(new CrashReport(e));
        }
    }

    public ResourceLoader getGameFolderLoader()
    {
        return gameFolderLoader;
    }

    public void crash(CrashReport crashReport)
    {
        crashReport.printStack();
        cleanup();
        System.exit(-1);
    }

    public void cleanup()
    {
        renderEngine.dispose();
        Display.destroy();
    }

    public PlayerController getPlayerController()
    {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController)
    {
        this.playerController = playerController;
    }

    public Gui getCurrentMenu()
    {
        return currentMenu;
    }

    @Override
    public Platform getPlatform()
    {
        return Platform.CLIENT;
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
        return null;
    }

    @Override
    public int getMaxPlayers()
    {
        return 0;
    }

    @Override
    public Player getPlayer(UUID uniqueId)
    {
        return player;
    }

    @Override
    public Collection<org.spongepowered.api.world.World> getWorlds()
    {
        return null;
    }

    @Override
    public org.spongepowered.api.world.World getWorld(UUID uniqueId)
    {
        return clientWorld;
    }

    @Override
    public org.spongepowered.api.world.World getWorld(String worldName)
    {
        return clientWorld;
    }

    @Override
    public void broadcastMessage(String message)
    {
        Log.message(message);
    }

    @Override
    public String getAPIVersion()
    {
        return "1.0";
    }

    @Override
    public String getImplementationVersion()
    {
        return "OurCraft's Unofficial Sponge Implementation - 1.0";
    }

    public Session getSession()
    {
        return session;
    }
}
