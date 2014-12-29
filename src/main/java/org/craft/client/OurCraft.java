package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.lang.annotation.*;
import java.nio.*;
import java.util.*;

import javax.imageio.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.gui.*;
import org.craft.client.models.*;
import org.craft.client.network.*;
import org.craft.client.render.*;
import org.craft.client.render.entity.*;
import org.craft.client.render.fonts.*;
import org.craft.client.sound.*;
import org.craft.entity.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.craft.modding.events.state.*;
import org.craft.network.*;
import org.craft.resources.*;
import org.craft.sound.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.Log.NonLoggable;
import org.craft.utils.crash.*;
import org.craft.world.*;
import org.craft.world.biomes.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;

public class OurCraft implements Runnable, OurCraftInstance
{

    private int                            displayWidth                = 960;
    private int                            displayHeight               = 540;
    private boolean                        running                     = true;
    private RenderEngine                   renderEngine                = null;
    private AssetLoader                    assetsLoader;
    private RenderBlocks                   renderBlocks;
    private World                          clientWorld;
    private MouseHandler                   mouseHandler;
    private EntityPlayer                   player;
    private static OurCraft                instance;
    private CollisionInfos                 objectInFront               = null;
    private OpenGLBuffer                   crosshairBuffer;
    private ResourceLocation               crosshairLocation;
    private FallbackRender                 fallbackRenderer;
    private Runtime                        runtime;
    private FontRenderer                   fontRenderer;
    private String                         username;

    private Gui                            currentMenu;
    private Gui                            newMenu;
    private OpenGLBuffer                   selectionBoxBuffer;
    private DiskSimpleResourceLoader       gameFolderLoader;
    private PlayerController               playerController;
    private GlobalRegistry                 gameRegistry;
    private EventBus                       eventBus;
    private Session                        session;
    private RenderItems                    renderItems;
    private AddonsLoader                   addonsLoader;

    private int                            frame;
    private int                            fps;
    private double                         expectedFrameRate           = 60.0;
    private double                         timeBetweenUpdates          = 1000000000 / expectedFrameRate;
    private final int                      maxUpdatesBeforeRender      = 3;
    private double                         lastUpdateTime              = System.nanoTime();
    private double                         lastRenderTime              = System.nanoTime();

    // If we are able to get as high as this FPS, don't render again.
    private final double                   TARGET_FPS                  = 60;
    private final double                   TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private int                            lastSecondTime              = (int) (lastUpdateTime / 1000000000);
    private ClientNetHandler               netHandler;
    private WorldLoader                    worldLoader;
    private GameSettings                   settings;
    private ModelLoader                    modelLoader;
    private HashMap<String, GuiDispatcher> guiMap;
    private ScreenTitle                    screenTitle;
    private ParticleRenderer               particleRenderer;
    private DirectSoundProducer            sndProducer;

    public OurCraft()
    {
        guiMap = Maps.newHashMap();
        EnumVanillaGuis.register(guiMap, NetworkSide.CLIENT);
        instance = this;
        this.assetsLoader = new AssetLoader(new ClasspathSimpleResourceLoader("assets"));
        this.gameFolderLoader = new DiskSimpleResourceLoader(SystemUtils.getGameFolder().getAbsolutePath());
        runtime = Runtime.getRuntime();
        crosshairLocation = new ResourceLocation("ourcraft", "textures/crosshair.png");
    }

    public void start(Map<String, String> properties)
    {
        username = properties.get("username");
        I18n.setCurrentLanguage(properties.get("lang"));
        run();
    }

    @Override
    public void run()
    {
        try
        {
            objectInFront = new CollisionInfos();
            objectInFront.type = CollisionType.NONE;
            //LWJGL Properties
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");

            //Init OpenGL context and settings up the display
            ContextAttribs context = new ContextAttribs(3, 2).withProfileCompatibility(true).withDebug(true);
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            Display.setIcon(new ByteBuffer[]
            {
                    ImageUtils.getPixels(ImageUtils.getFromClasspath("/assets/ourcraft/textures/favicon_128.png")),
                    ImageUtils.getPixels(ImageUtils.getFromClasspath("/assets/ourcraft/textures/favicon_32.png"))
            });
            Display.setResizable(true);
            Display.setTitle("OurCraft - " + getVersion());
            try
            {
                Display.create(new PixelFormat(), context);
            }
            catch(LWJGLException e)
            {
                if(e.getMessage().contains("Could not create context"))
                    Display.create();
            }
            if(!GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
            {
                Log.fatal("Sorry, but this game only works with Vertex Buffer Objects and it seems your graphical card can't support it. :(");
            }

            //Init the RenderEngine
            renderEngine = new RenderEngine(assetsLoader);
            renderEngine.enableGLCap(GL_BLEND);
            renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            renderEngine.switchToOrtho();
            renderEngine.renderSplashScreen();
            Display.update();

            mouseHandler = new MouseHandler();
            //Init OpenGL CapNames for crash report system
            OpenGLHelper.loadCapNames();

            //Init Game Content
            session = SessionManager.getInstance().registerPlayer(new UUID(username.length() << 24, username.length() << 10), username, username);
            Log.message("Loading Mods...");
            gameRegistry = new GlobalRegistry();
            List<Class<? extends Annotation>> annots = Lists.newArrayList();
            annots.add(OurModEventHandler.class);
            eventBus = new EventBus(new Class<?>[]
            {
                    ModEvent.class
            }, annots);
            addonsLoader = new AddonsLoader(this, eventBus);
            File modsFolder = new File(SystemUtils.getGameFolder(), "mods");
            if(!modsFolder.exists())
                modsFolder.mkdirs();
            addonsLoader.loadAll(modsFolder);

            sndProducer = new DirectSoundProducer();
            settings = new GameSettings();
            File propsFile = new File(SystemUtils.getGameFolder(), "properties.txt");
            settings.loadFrom(propsFile);
            settings.saveTo(propsFile);

            AudioRegistry.init();
            if(settings.font.getValue().equals("default"))
                fontRenderer = new BaseFontRenderer();
            else
                fontRenderer = new TrueTypeFontRenderer(settings.font.getValue());

            EntityRegistry.init();
            Blocks.init();
            BlockStates.init();
            Items.init();
            PacketRegistry.init();
            I18n.init(assetsLoader);
            I18n.setCurrentLanguage(settings.lang.getValue());
            ParticleRegistry.init();
            Biomes.init();
            eventBus.fireEvent(new ModInitEvent(this), null, null);

            modelLoader = new ModelLoader();
            renderBlocks = new RenderBlocks(renderEngine, modelLoader, new ResourceLocation("ourcraft", "models/block/cube_all.json"));
            renderItems = new RenderItems(renderEngine, modelLoader);
            renderEngine.createBlockAndItemMap(renderBlocks, renderItems);
            fallbackRenderer = new FallbackRender();

            AbstractRender.registerVanillaRenderers();

            particleRenderer = new ParticleRenderer(20000);
            openMenu(new GuiMainMenu(this));

            loadCrosshairBuffer();

            selectionBoxBuffer = new OpenGLBuffer();
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(0, 0, 0))); //0
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(0, 0, 1))); //1
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(0, 1, 0))); //2
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(0, 1, 1))); //3
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(1, 0, 0))); //4
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(1, 0, 1))); //5
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(1, 1, 0))); //6
            selectionBoxBuffer.addVertex(Vertex.get(Vector3.get(1, 1, 1))); //7

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

            eventBus.fireEvent(new ModPostInitEvent(this), null, null);

            CommonHandler.setCurrentContainer(null);

            running = true;
            while(running)
            {
                tick();
            }
            cleanup();
            System.exit(0);
        }
        catch(Exception e)
        {
            crash(new CrashReport(e));
        }
    }

    /**
     * Loads crosshair buffer
     */
    private void loadCrosshairBuffer()
    {
        if(crosshairBuffer != null)
            crosshairBuffer.dispose();
        crosshairBuffer = new OpenGLBuffer();
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 - 8, 0), Vector2.get(0, 0)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 - 8, 0), Vector2.get(1, 0)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 + 8, 0), Vector2.get(1, 1)));
        crosshairBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 + 8, 0), Vector2.get(0, 1)));

        crosshairBuffer.addIndex(0);
        crosshairBuffer.addIndex(1);
        crosshairBuffer.addIndex(2);

        crosshairBuffer.addIndex(2);
        crosshairBuffer.addIndex(3);
        crosshairBuffer.addIndex(0);
        crosshairBuffer.upload();
        crosshairBuffer.clearAndDisposeVertices();
    }

    /**
     * Opens given gui
     */
    public void openMenu(Gui gui)
    {
        this.newMenu = gui;
    }

    /**
     * Performs one tick of the game
     */
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

            if(Display.isCloseRequested())
                running = false;

            if(Display.wasResized())
            {
                int w = Display.getWidth();
                int h = Display.getHeight();
                displayWidth = w;
                displayHeight = h;

                renderEngine.loadMatrices();
                try
                {
                    renderEngine.loadShaders();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                loadCrosshairBuffer();
                currentMenu.build();
            }

            lastRenderTime = now;
            // Update the frames we got.
            int thisSecond = (int) (lastUpdateTime / 1000000000);
            frame++ ;
            if(thisSecond > lastSecondTime)
            {
                fps = frame;
                Display.setTitle("OurCraft - " + getVersion() + " - " + fps + " FPS");
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

    /**
     * Update the game (world, menus, etc.)
     */
    private void update(final double delta)
    {
        renderEngine.blocksAndItemsMap.tick();
        particleRenderer.getMap().tick();
        sndProducer.setMasterVolume(settings.masterVolume.getValue());
        sndProducer.setSoundVolume(settings.soundVolume.getValue());
        sndProducer.setMusicVolume(settings.musicVolume.getValue());
        if(player != null)
        {
            sndProducer.setListenerLocation(player.posX, player.posY, player.posZ);
            sndProducer.setListenerOrientation(player.getQuaternionRotation());
            objectInFront = player.getObjectInFront(5f);
        }
        if(newMenu != currentMenu)
        {
            currentMenu = newMenu;
            fontRenderer.disposeCache();
            if(currentMenu != null)
            {
                currentMenu.build();
            }
        }
        while(Mouse.next())
        {
            int mouseButton = Mouse.getEventButton();
            boolean state = Mouse.getEventButtonState();
            int x = Mouse.getEventX();
            int y = displayHeight - Mouse.getEventY();
            int dx = Mouse.getEventDX();
            int dy = Mouse.getEventDY();
            int deltaWheel = Mouse.getEventDWheel();
            if(currentMenu != null)
            {
                if(mouseButton != -1)
                {
                    if(state)
                        currentMenu.onButtonPressed(x, y, mouseButton);
                    else
                        currentMenu.onButtonReleased(x, y, mouseButton);
                }

                if(deltaWheel != 0)
                {
                    currentMenu.handleMouseWheelMovement(x, y, deltaWheel);
                }
                else
                {
                    currentMenu.handleMouseMovement(x, y, dx, dy);
                }
            }
            if(playerController != null && (currentMenu == null || !currentMenu.requiresMouse()))
            {
                if(!state)
                {
                    if(mouseButton == 0)
                    {
                        playerController.onLeftClick(getObjectInFront());
                    }
                    else if(mouseButton == 1)
                    {
                        playerController.onRightClick(getObjectInFront());
                    }
                }
                if(deltaWheel != 0)
                {
                    playerController.onMouseWheelMoved(deltaWheel);
                }
            }
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
                    if(id == Keyboard.KEY_F2)
                    {
                        File out = new File(SystemUtils.getGameFolder(), "screenshots/" + System.currentTimeMillis() + ".png");
                        try
                        {
                            if(!out.getParentFile().exists())
                                out.getParentFile().mkdirs();
                            out.createNewFile();
                            ImageIO.write(takeScreenshot(), "png", out);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(id == Keyboard.KEY_F)
                    {
                        if(clientWorld != null)
                        {
                            EntityPrimedTNT tnt = new EntityPrimedTNT(clientWorld);
                            tnt.setFuse(120L);
                            tnt.setSize(1, 1, 1);
                            tnt.setLocation(player.posX, player.posY, player.posZ);
                            clientWorld.spawn(tnt);
                        }
                    }
                }
                else
                {
                    if(id == Keyboard.KEY_ESCAPE)
                    {
                        if(clientWorld != null && !(currentMenu instanceof GuiPauseMenu))
                        {
                            openMenu(new GuiPauseMenu(this));
                        }
                        else if(clientWorld == null)
                            running = false;
                    }
                    /*                    if(id == Keyboard.KEY_RETURN)
                                        {
                                            try
                                            {
                                                setResourcesPack("test.zip");
                                            }
                                            catch(Exception e)
                                            {
                                                e.printStackTrace();
                                                try
                                                {
                                                    modelLoader.clearModels();
                                                    renderEngine.loadShaders();
                                                    renderEngine.reloadLocations();
                                                }
                                                catch(Exception e1)
                                                {
                                                    ;
                                                }
                                            }
                                        }*/
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
        if(playerController != null && (currentMenu == null || !currentMenu.pausesGame()))
        {
            if(Keyboard.isKeyDown(settings.forwardKey.getValue()))
            {
                playerController.onMoveForwardRequested();
            }
            if(Keyboard.isKeyDown(settings.backwardsKey.getValue()))
            {
                playerController.onMoveBackwardsRequested();
            }
            if(Keyboard.isKeyDown(settings.leftKey.getValue()))
            {
                playerController.onMoveLeftRequested();
            }
            if(Keyboard.isKeyDown(settings.rightKey.getValue()))
            {
                playerController.onMoveRightRequested();
            }
            if(Keyboard.isKeyDown(settings.jumpKey.getValue()))
            {
                playerController.onJumpRequested();
            }
            playerController.update();
        }
        if(clientWorld != null)
        {
            if(currentMenu != null && !currentMenu.pausesGame())
            {
                clientWorld.update(delta);
                clientWorld.updateAllParticles();
            }
            else if(currentMenu == null)
            {
                clientWorld.update(delta);
                clientWorld.updateAllParticles();
            }
        }
    }

    /**
     * Renders the game
     */
    private void render(double delta)
    {
        render(delta, true);
    }

    /**
     * Renders the game with or without the gui
     */
    private void render(double delta, boolean drawGui)
    {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        renderEngine.begin();
        List<Chunk> visiblesChunks = getVisibleChunks();
        glViewport(0, 0, displayWidth, displayHeight);
        glClearColor(0, 0.6666667f, 1, 1);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        renderEngine.enableGLCap(GL_DEPTH_TEST);
        renderEngine.switchToPerspective();
        if(clientWorld != null)
        {
            particleRenderer.renderAll(renderEngine);
            renderWorld(visiblesChunks, delta, drawGui);
            if(player != null)
            {
                if(player.getHeldItem() != null && player.getHeldItem().getStackable() != null)
                {
                    renderEngine.enableGLCap(GL_DEPTH_TEST);
                    renderEngine.enableGLCap(GL_ALPHA_TEST);
                    renderEngine.setProjectFromEntity(false);
                    boolean isBlock = player.getHeldItem().getStackable() instanceof Block;
                    Quaternion q = new Quaternion(Vector3.yAxis, (float) Math.toRadians(75));
                    q = q.mul(new Quaternion(Vector3.xAxis, (float) Math.toRadians(10)));
                    q = q.mul(new Quaternion(Vector3.zAxis, (float) Math.toRadians(5)));
                    float ratio = (float) displayWidth / (float) displayHeight;
                    float d = ratio / (16.f / 9.f);
                    Matrix4 m = isBlock ? Matrix4.get().initTranslation(d * 1.05f, -1.65f, 0.5f) : Matrix4.get().initTranslation(d * 1.05f, -1.05f, 0.5f);
                    m = m.mul(Matrix4.get().initRotation(q.getForward(), q.getUp()));
                    renderEngine.setModelviewMatrix(m);
                    renderItems.renderItem(renderEngine, player.getHeldItem(), clientWorld, 0, 0, 0);

                    renderEngine.setProjectFromEntity(true);
                    m.dispose();
                }
            }
        }
        renderEngine.setModelviewMatrix(Matrix4.get().initIdentity());
        renderEngine.end();
        renderEngine.switchToOrtho();
        renderEngine.enableGLCap(GL_BLEND);
        glClear(GL_DEPTH_BUFFER_BIT);
        renderEngine.switchToOrtho();
        renderEngine.disableGLCap(GL_DEPTH_TEST);

        if(drawGui)
        {
            if(clientWorld != null)
            {
                renderEngine.enableGLCap(GL_COLOR_LOGIC_OP);
                glLogicOp(GL_XOR);
                renderEngine.bindLocation(crosshairLocation);
                renderEngine.renderBuffer(crosshairBuffer);
                renderEngine.disableGLCap(GL_COLOR_LOGIC_OP);
            }

            int mx = Mouse.getX();
            int my = Mouse.getY();
            if(currentMenu != null)
            {
                currentMenu.render(mx, displayHeight - my, renderEngine);
            }
        }
        printIfGLError();
    }

    /**
     * Gets a list of visible chunks
     */
    private List<Chunk> getVisibleChunks()
    {
        List<Chunk> visibleChunks = Lists.newArrayList();
        if(player != null)
        {
            //AABB chunkBB = new AABB(Vector3.NULL, Vector3.get(16, 16, 16));
            int renderDistance = 8;
            int ox = (int) renderEngine.getRenderViewEntity().getPosX();
            int oy = (int) renderEngine.getRenderViewEntity().getPosY();
            int oz = (int) renderEngine.getRenderViewEntity().getPosZ();
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

                        if(clientWorld == null)
                            continue;
                        Chunk c = clientWorld.getChunkProvider().get(clientWorld, (int) Math.floor((float) fx / 16f), (int) Math.floor((float) fy / 16f), (int) Math.floor((float) fz / 16f));
                        if(c != null)
                        {
                            //  AABB chunkBox = chunkBB.translate(x, y, z);
                            //  if(renderEngine.getFrustum().boxIn(chunkBox))
                            {
                                visibleChunks.add(c);
                            }
                            // chunkBox.dispose();
                        }

                    }
                }
            }
        }
        return visibleChunks;
    }

    /**
     * Renders world
     */
    private void renderWorld(List<Chunk> visiblesChunks, double delta, boolean drawGui)
    {
        renderBlocks.render(clientWorld, visiblesChunks);
        for(Entity e : clientWorld.getEntitiesList())
        {
            if(e != renderEngine.getRenderViewEntity())
            {
                AbstractRender renderer = AbstractRender.getRenderer(e);
                if(renderer == null)
                    renderer = fallbackRenderer;
                renderer.render(renderEngine, e, (float) e.getPosX(), (float) e.getPosY(), (float) e.getPosZ());
            }
        }
        glClear(GL_DEPTH_BUFFER_BIT);
        renderEngine.disableGLCap(GL_DEPTH_TEST);
        if(objectInFront != null && objectInFront.type == CollisionType.BLOCK && drawGui)
        {
            renderEngine.bindLocation(null);
            Matrix4 modelView = renderEngine.getModelviewMatrix();
            AABB blockSelectBB = ((Block) objectInFront.value).getSelectionBox(clientWorld, 0, 0, 0);
            Vector3 ratio = blockSelectBB.getMaxExtents().sub(blockSelectBB.getMinExtents());
            float sx = ratio.getX();
            float sy = ratio.getY();
            float sz = ratio.getZ();
            Matrix4 selectionBoxMatrix = Matrix4.get().initTranslation(objectInFront.x + blockSelectBB.getMinExtents().getX(), objectInFront.y + blockSelectBB.getMinExtents().getY(), objectInFront.z + blockSelectBB.getMinExtents().getZ()).mul(Matrix4.get().initScale(sx, sy, sz));
            renderEngine.setModelviewMatrix(selectionBoxMatrix);
            renderEngine.renderBuffer(selectionBoxBuffer, GL_LINES);
            renderEngine.setModelviewMatrix(modelView);
            ratio.dispose();
            blockSelectBB.dispose();
            modelView.dispose();
            selectionBoxMatrix.dispose();
        }
    }

    /**
     * Loads and sets current resources pack. Throws an exception if given pack does not exist or is invalid
     */
    public void setResourcesPack(String fileName) throws IOException
    {
        ResourceLocation location = new ResourceLocation("resourcepacks", fileName);
        ZipSimpleResourceLoader loader = new ZipSimpleResourceLoader(gameFolderLoader.getResource(location), "assets");
        this.assetsLoader.setResourcePackLoader(loader);
        modelLoader.clearModels();
        renderEngine.loadShaders();
        renderEngine.reloadLocations();
    }

    /**
     * Print an error log only if OpenGL <code>getError()</code> returns an error
     */
    @NonLoggable
    public static void printIfGLError()
    {
        int errorFlag = glGetError();
        // If an error has occurred...
        if(errorFlag != GL_NO_ERROR)
        {
            // Print the error to System.err.
            Log.error("[GL ERROR] " + GLU.gluErrorString(errorFlag));
        }
    }

    /**
     * Gets the username used by the player
     */
    public String getClientUsername()
    {
        return username;
    }

    /**
     * Returns the instance of the game
     */
    public static OurCraft getOurCraft()
    {
        return instance;
    }

    /**
     * Returns the game's assets loader
     */
    @Override
    public AssetLoader getAssetsLoader()
    {
        return assetsLoader;
    }

    public MouseHandler getMouseHandler()
    {
        return mouseHandler;
    }

    /**
     * Gets the object in front of the player. Updated each tick
     */
    public CollisionInfos getObjectInFront()
    {
        return objectInFront;
    }

    /**
     * Returns true if the game is currently running
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Gets the client world
     */
    public World getClientWorld()
    {
        return clientWorld;
    }

    /**
     * Returns render engine
     */
    public RenderEngine getRenderEngine()
    {
        return renderEngine;
    }

    /**
     * Returns the version of the game attributed during the build process
     */
    public static String getVersion()
    {
        return "OurCraft:BuildNumber";
    }

    /**
     * Gets the display width
     */
    public int getDisplayWidth()
    {
        return displayWidth;
    }

    /**
     * Gets the display height
     */
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

    /**
     * Gets the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    /**
     * Loads and sets given world
     */
    public void setWorld(World world)
    {
        if(world == null)
        {
            if(clientWorld != null)
                eventBus.fireEvent(new WorldUnloadEvent(this, clientWorld), null, null);
        }
        else
        {
            world.setDelegateParticleHandler(particleRenderer);
            world.setDelegateSoundProducer(sndProducer);
            try
            {
                world.getLoader().loadWorldConstants(world);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            eventBus.fireEvent(new WorldLoadEvent(this, world), null, null);
        }
        this.clientWorld = world;
    }

    /**
     * Sets client player
     */
    public void setPlayer(EntityPlayer player)
    {
        this.player = player;
    }

    /**
     * Shutdowns gracefully the game
     */
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
        openMenu(new GuiMainMenu(this));
    }

    /**
     * Saves the current world
     */
    public void saveWorld()
    {
        if(clientWorld.isRemote)
            return;
        WorldLoader loader = clientWorld.getLoader();
        try
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds/" + clientWorld.getName());
            if(!worldFolder.exists())
                worldFolder.mkdirs();

            render(0, false);

            ImageIO.write(takeScreenshot(), "png", new File(worldFolder, "worldSnapshot.png"));

            File chunkFolder = new File(worldFolder, "chunkData");
            if(!chunkFolder.exists())
                chunkFolder.mkdirs();
            File worldData = new File(worldFolder, "world.data");
            if(!worldData.exists())
                worldData.createNewFile();
            loader.writeWorldConstants(worldData, clientWorld);

            Iterator<Chunk> chunks = clientWorld.getChunkProvider().iterator();
            Chunk bottomCorner = null;
            Chunk topCorner = null;
            while(chunks.hasNext())
            {
                Chunk chunk = chunks.next();
                if(bottomCorner == null)
                    bottomCorner = chunk;
                else if(bottomCorner.getCoords().x > chunk.getCoords().x || bottomCorner.getCoords().z > chunk.getCoords().z)
                {
                    bottomCorner = chunk;
                }

                if(topCorner == null)
                    topCorner = chunk;
                else if(topCorner.getCoords().x < chunk.getCoords().x || topCorner.getCoords().z < chunk.getCoords().z)
                {
                    topCorner = chunk;
                }
                if(chunk.isModified())
                    loader.writeChunk(new File(worldFolder, "chunkData/chunk" + chunk.getCoords().x + "." + chunk.getCoords().y + "." + chunk.getCoords().z + ".data"), chunk, chunk.getCoords().x, chunk.getCoords().y, chunk.getCoords().z);
            }

            chunks = clientWorld.getChunkProvider().iterator();
            int w = Math.max(1, topCorner.getCoords().x - bottomCorner.getCoords().x);
            int h = Math.max(1, topCorner.getCoords().z - bottomCorner.getCoords().z);
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            while(chunks.hasNext())
            {
                Chunk chunk = chunks.next();
                int px = chunk.getCoords().x - bottomCorner.getCoords().x;
                int py = chunk.getCoords().z - bottomCorner.getCoords().z;
                float tempNormalized = Biomes.normalizeTemperature(chunk.getBiome().getTemperature());
                int red = (int) (tempNormalized * 255f);
                int blue = (int) (255 - tempNormalized * 255f);
                int color = 0xFF000000 | (red << 16) | (blue);
                if(px >= image.getWidth() || py >= image.getHeight() || px < 0 || py < 0)
                    continue;
                image.setRGB(px, py, color);
            }
            ImageIO.write(image, "png", new File(worldFolder, "temperatures.png"));
            loader.writeEntities(clientWorld.getEntitiesList());
        }
        catch(Exception e)
        {
            crash(new CrashReport(e));
        }
    }

    private static int[]     screenshotBufferArray;
    private static IntBuffer pixelBuffer;

    public static BufferedImage takeScreenshot()
    {
        return takeScreenshot(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static BufferedImage takeScreenshot(int x, int y, int w, int h)
    {
        int n = w * h;
        if(pixelBuffer == null || pixelBuffer.capacity() < n)
        {
            pixelBuffer = BufferUtils.createIntBuffer(n);
            screenshotBufferArray = new int[n];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();
        GL11.glReadPixels(x, y, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
        pixelBuffer.get(screenshotBufferArray);
        int[] finalArray = new int[n];
        for(int index = 0; index < n; index++ )
        {
            int color = screenshotBufferArray[index];
            int alpha = color >> 24 & 0xFF;
            int red = color >> 16 & 0xFF;
            int green = color >> 8 & 0xFF;
            int blue = color >> 0 & 0xFF;
            int x1 = index % w;
            int y1 = h - (index / w) - 1;
            finalArray[x1 + y1 * w] = (alpha << 24) | (blue << 16) | (green << 8) | red; // We invert the colors given by OpenGL
        }
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, finalArray, 0, w);
        return image;
    }

    public ResourceLoader getGameFolderLoader()
    {
        return gameFolderLoader;
    }

    /**
     * Forces a crash with given crash report
     */
    public void crash(CrashReport crashReport)
    {
        crashReport.printStack();
        cleanup();
        System.exit(-1);
    }

    /**
     * Disposes all resources held by the game
     */
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
    public EventBus getEventBus()
    {
        return eventBus;
    }

    @Override
    public GlobalRegistry getRegistry()
    {
        return gameRegistry;
    }

    public EntityPlayer getClientPlayer()
    {
        return player;
    }

    @Override
    public void broadcastMessage(String message)
    {
        Log.message(message);
    }

    /**
     * Gets current session
     */
    public Session getSession()
    {
        return session;
    }

    public void setNetHandler(ClientNetHandler handler)
    {
        this.netHandler = handler;
    }

    public ClientNetHandler getNetHandler()
    {
        return netHandler;
    }

    /**
     * Send given packet to the server
     */
    public void sendPacket(AbstractPacket packet)
    {
        netHandler.send(packet);
    }

    public WorldLoader getWorldLoader()
    {
        return worldLoader;
    }

    public void setWorldLoader(WorldLoader loader)
    {
        this.worldLoader = loader;
    }

    public RenderBlocks getRenderBlocks()
    {
        return renderBlocks;
    }

    public GameSettings getGameSettings()
    {
        return settings;
    }

    public void saveSettings()
    {
        File file = new File(SystemUtils.getGameFolder(), "properties.txt");
        try
        {
            settings.saveTo(file);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public RenderItems getRenderItems()
    {
        return renderItems;
    }

    @Override
    public AddonsLoader getAddonsLoader()
    {
        return addonsLoader;
    }

    @Override
    public boolean isClient()
    {
        return true;
    }

    @Override
    public boolean isServer()
    {
        return false;
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
        {
            container.registerBlock(block);
        }
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

    public void setScreenTitle(ScreenTitle screenTitle)
    {
        this.screenTitle = screenTitle;
    }

    public ScreenTitle getTitle()
    {
        return screenTitle;
    }

    public DirectSoundProducer getSoundProducer()
    {
        return sndProducer;
    }
}
