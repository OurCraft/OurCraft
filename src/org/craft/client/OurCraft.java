package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import org.craft.blocks.*;
import org.craft.client.render.*;
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

    private File                          gameFolder;
    private int                           displayWidth  = 960;
    private int                           displayHeight = 540;
    private boolean                       running       = true;
    private RenderEngine                  renderEngine  = null;
    private Matrix4                       modelMatrix;
    private Shader                        basicShader;
    private ClasspathSimpleResourceLoader classpathLoader;
    private RenderBlocks                  renderBlocks;
    private World                         clientWorld;
    private MouseHandler                  mouseHandler;
    private EntityPlayer                  player;
    private static OurCraft               instance;
    private CollisionInfos                objectInFront = null;
    private Matrix4                       projectionHud;
    private Texture                       crosshairTexture;
    private OpenGLBuffer                  crosshairBuffer;

    public OurCraft()
    {
        instance = this;
        classpathLoader = new ClasspathSimpleResourceLoader();
    }

    public void start()
    {
        new Thread(this).start();
    }

    public void run()
    {
        try
        {
            JFrame frame = new JFrame();
            frame.setTitle("OurCraft");
            Canvas canvas = new Canvas();
            frame.add(canvas);
            canvas.setPreferredSize(new Dimension(displayWidth, displayHeight));
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            Display.setParent(canvas);
            Display.create();
            mouseHandler = new MouseHandler();
            mouseHandler.grab();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            GL11.glLoadIdentity();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            renderEngine = new RenderEngine();

            Blocks.init();

            WorldGenerator generator = new WorldGenerator()
            {

            };
            generator.addPopulator(new RockPopulator());
            generator.addPopulator(new GrassPopulator());
            generator.addPopulator(new TreePopulator());
            clientWorld = new World(new BaseChunkProvider(), generator);
            renderBlocks = new RenderBlocks(renderEngine);

            basicShader = new Shader(IOUtils.readString(OurCraft.class.getResourceAsStream("/assets/shaders/base.vsh"), "UTF-8"), IOUtils.readString(OurCraft.class.getResourceAsStream("/assets/shaders/base.fsh"), "UTF-8"));
            modelMatrix = new Matrix4().initIdentity();

            player = new EntityPlayer(clientWorld);
            player.setLocation(0, 160 + 17, 0);
            clientWorld.spawn(player);
            renderEngine.setRenderViewEntity(player);

            this.crosshairTexture = OpenGLHelper.loadTexture(ImageIO.read(OurCraft.class.getResourceAsStream("/assets/textures/crosshair.png")));
            projectionHud = new Matrix4().initOrthographic(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
            crosshairBuffer = new OpenGLBuffer();
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 - 8, 0), new Vector2(0, 0)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 - 8, 0), new Vector2(1, 0)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 + 8, Display.getHeight() / 2 + 8, 0), new Vector2(1, 1)));
            crosshairBuffer.addVertex(new Vertex(Vector3.get(Display.getWidth() / 2 - 8, Display.getHeight() / 2 + 8, 0), new Vector2(0, 1)));

            crosshairBuffer.addIndex(0);
            crosshairBuffer.addIndex(1);
            crosshairBuffer.addIndex(2);

            crosshairBuffer.addIndex(2);
            crosshairBuffer.addIndex(3);
            crosshairBuffer.addIndex(0);
            crosshairBuffer.upload();

            new ThreadGetChunksFromCamera(this).start();
            running = true;
            while(running && !Display.isCloseRequested())
            {
                tick();
                Display.sync(60);
                Display.update();
            }
            Display.destroy();
            frame.dispose();
            Log.error("BYE");
            System.exit(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void tick()
    {
        render();
        update();
    }

    private void update()
    {
        mouseHandler.update();
        if(player != null)
        {
            objectInFront = player.getObjectInFront(5f);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            running = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            player.jump();
        }
        clientWorld.update();
    }

    private void render()
    {
        ArrayList<Chunk> visiblesChunks = new ArrayList<>();
        if(player != null)
        {
            int renderDistance = 8;
            int ox = (int)renderEngine.getRenderViewEntity().getPos().x;
            int oy = (int)renderEngine.getRenderViewEntity().getPos().y;
            int oz = (int)renderEngine.getRenderViewEntity().getPos().z;
            for(int x = -renderDistance; x < renderDistance; x++ )
            {
                for(int y = -renderDistance; y < renderDistance; y++ )
                {
                    for(int z = -renderDistance; z < renderDistance; z++ )
                    {
                        int fx = x * 16 + ox;
                        int fy = y * 16 + oy;
                        int fz = z * 16 + oz;
                        if(fy < 0) continue;
                        synchronized(clientWorld)
                        {
                            Chunk c = clientWorld.getChunkProvider().get(clientWorld, (int)Math.floor((float)fx / 16f), (int)Math.floor((float)fy / 16f), (int)Math.floor((float)fz / 16f));
                            if(c != null) visiblesChunks.add(c);
                        }
                    }
                }
            }
        }
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glClearColor(0, 0.6666667f, 1, 1);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        renderEngine.enableGLCap(GL_DEPTH_TEST);
        basicShader.bind();
        basicShader.setUniform("modelview", this.modelMatrix);
        basicShader.setUniform("projection", this.renderEngine.getProjectionMatrix());
        renderBlocks.render(clientWorld, visiblesChunks);

        glClear(GL_DEPTH_BUFFER_BIT);
        renderEngine.disableGLCap(GL_DEPTH_TEST);
        if(objectInFront != null && objectInFront.type == CollisionType.BLOCK)
        {
            glBindTexture(GL_TEXTURE_2D, 0);
            glBegin(GL_LINES);
            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z);
            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z);

            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z);
            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z);

            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z + 1);
            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z + 1);

            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z + 1);
            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z + 1);

            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z);
            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z + 1);

            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z);
            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z + 1);

            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z);
            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z + 1);

            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z);
            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z + 1);

            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z);
            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z);

            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z);
            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z);

            glVertex3d(objectInFront.x, objectInFront.y, objectInFront.z + 1);
            glVertex3d(objectInFront.x, objectInFront.y + 1, objectInFront.z + 1);

            glVertex3d(objectInFront.x + 1, objectInFront.y, objectInFront.z + 1);
            glVertex3d(objectInFront.x + 1, objectInFront.y + 1, objectInFront.z + 1);

            glEnd();
        }

        basicShader.setUniform("projection", projectionHud);

        renderEngine.enableGLCap(GL_COLOR_LOGIC_OP);
        glLogicOp(GL_XOR);
        renderEngine.renderBuffer(crosshairBuffer, crosshairTexture);
        renderEngine.disableGLCap(GL_COLOR_LOGIC_OP);
    }

    public File getGameFolder()
    {
        if(gameFolder == null)
        {
            String appdata = System.getenv("APPDATA");
            if(appdata != null)
                gameFolder = new File(appdata, ".ourcraft");
            else
                gameFolder = new File(System.getProperty("user.home"), ".ourcraft");
        }
        return gameFolder;
    }

    public static OurCraft getOurCraft()
    {
        return instance;
    }

    public ResourceLoader getBaseLoader()
    {
        return classpathLoader;
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
}
