package org.craft.client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.util.*;

import org.craft.blocks.*;
import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.entity.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.world.*;
import org.craft.world.populators.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

public class OurCraft implements Runnable
{

    private int                      displayWidth  = 960;
    private int                      displayHeight = 540;
    private long                     lastTime      = 0;
    private boolean                  running       = true;
    private RenderEngine             renderEngine  = null;
    private AssetLoader              assetsLoader;
    private RenderBlocks             renderBlocks;
    private World                    clientWorld;
    private MouseHandler             mouseHandler;
    private EntityPlayer             player;
    private static OurCraft          instance;
    private CollisionInfos           objectInFront = null;
    private OpenGLBuffer             crosshairBuffer;
    private FallbackRender<Entity>   fallbackRenderer;
    private Runtime                  runtime;
    private FontRenderer             fontRenderer;
    private String                   username;

    private Gui                      currentMenu;
    private Gui                      newMenu;
    private OpenGLBuffer             selectionBoxBuffer;
    private DiskSimpleResourceLoader gameFolderLoader;

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
            System.setProperty("org.lwjgl.util.Debug", "true");
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
            ContextAttribs context = new ContextAttribs(3, 3).withProfileCompatibility(true).withDebug(true);
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            Display.setResizable(true);
            Display.create(new PixelFormat(), context);
            mouseHandler = new MouseHandler();

            renderEngine = new RenderEngine(assetsLoader);
            renderEngine.enableGLCap(GL_BLEND);
            renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            renderEngine.switchToOrtho();
            renderEngine.renderSplashScreen();

            Display.update();
            fontRenderer = new BaseFontRenderer();

            Blocks.init();
            I18n.init(assetsLoader);

            Log.message("==== IN en_US.lang ====");
            Log.message("lang.test1 is " + I18n.format("lang.test1"));
            Log.message("lang.test2 is " + I18n.format("lang.test2", 0.2));
            Log.message("lang.test3 is " + I18n.format("lang.test3", "Test"));
            Log.message("lang.test4 is " + I18n.format("lang.test4", 12));

            I18n.setCurrentLanguage("fr_FR");
            Log.message("==== IN fr_FR.lang ====");
            Log.message("lang.test1 is " + I18n.format("lang.test1"));
            Log.message("lang.test2 is " + I18n.format("lang.test2", 0.2));
            Log.message("lang.test3 is " + I18n.format("lang.test3", "Test"));
            Log.message("lang.test4 is " + I18n.format("lang.test4", 12));

            boolean debugNoGui = false;
            renderBlocks = new RenderBlocks(renderEngine);
            fallbackRenderer = new FallbackRender<Entity>();

            if(debugNoGui)
            {
                WorldGenerator generator = new WorldGenerator();
                generator.addPopulator(new RockPopulator());
                generator.addPopulator(new GrassPopulator());
                generator.addPopulator(new TreePopulator());
                clientWorld = new World(new BaseChunkProvider(), generator);
                renderBlocks = new RenderBlocks(renderEngine);

                player = new EntityPlayer(clientWorld);
                player.setLocation(0, 160 + 17, 0);
                clientWorld.spawn(player);
                renderEngine.setRenderViewEntity(player);

                fallbackRenderer = new FallbackRender<Entity>();
                new ThreadGetChunksFromCamera(this).start();
                openMenu(new GuiIngame(fontRenderer));
            }
            else
            {
                openMenu(new GuiMainMenu(fontRenderer));
            }

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

            while(running && !Display.isCloseRequested())
            {
                tick(1000 / 60);
                Display.sync(60);
                Display.update();
                Thread.yield();
                Thread.sleep(1);
            }
            Log.error("CLEANUP");
            renderEngine.dispose();
            Display.destroy();
            Log.error("BYE");
            System.exit(0);
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

    private void tick(final int time)
    {
        render();
        update(time);

    }

    private void update(final int time)
    {
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
                currentMenu.handleClick(x, y, mouseButton);
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
        if(player != null)
        {
            objectInFront = player.getObjectInFront(5f);
        }
        boolean canUpdate = (System.currentTimeMillis() - lastTime) >= time;
        if(canUpdate)
        {
            if(clientWorld != null)
            {
                if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    player.jump();
                }
            }
            this.resetTime();
        }
        if(clientWorld != null)
        {
            if(currentMenu != null && !currentMenu.pausesGame())
            {
                clientWorld.update(time, canUpdate);
            }
            else if(currentMenu == null)
                clientWorld.update(time, canUpdate);
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

    private void render()
    {
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
                fallbackRenderer.render(renderEngine, e, e.getX(), e.getY(), e.getZ());
            }
            glClear(GL_DEPTH_BUFFER_BIT);
            renderEngine.disableGLCap(GL_DEPTH_TEST);
            if(objectInFront != null && objectInFront.type == CollisionType.BLOCK)
            {
                renderEngine.bindLocation(null);
                Matrix4 modelView = renderEngine.getModelviewMatrix();
                renderEngine.setModelviewMatrix(new Matrix4().initTranslation(objectInFront.x, objectInFront.y, objectInFront.z));
                renderEngine.renderBuffer(selectionBoxBuffer, GL_LINES);
                renderEngine.setModelviewMatrix(modelView);
            }
            renderEngine.switchToOrtho();

            renderEngine.enableGLCap(GL_COLOR_LOGIC_OP);
            glLogicOp(GL_XOR);
            renderEngine.bindLocation(new ResourceLocation("ourcraft", "textures/crosshair.png"));
            renderEngine.renderBuffer(crosshairBuffer);
            renderEngine.disableGLCap(GL_COLOR_LOGIC_OP);
        }
        else
        {
            glClear(GL_DEPTH_BUFFER_BIT);
            renderEngine.disableGLCap(GL_DEPTH_TEST);
            renderEngine.switchToOrtho();
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
     * Convinience method that disposes of the world and change the current screen
     */
    public void quitToMainScreen()
    {
        clientWorld = null;
        player = null;
        openMenu(new GuiMainMenu(fontRenderer));
    }

    public ResourceLoader getGameFolderLoader()
    {
        return gameFolderLoader;
    }
}
