package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;
import java.util.*;

import javax.imageio.*;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class OurCraft
{

    private File                          gameFolder;
    private int                           displayWidth  = 960;
    private int                           displayHeight = 540;
    private boolean                       running       = false;
    private Texture                       openglTexture;
    private RenderEngine                  renderEngine  = null;
    private OpenGLBuffer                  testBuffer;
    private Matrix4                       modelMatrix;
    private Shader                        basicShader;
    private Matrix4                       projectionMatrix;
    private ClasspathSimpleResourceLoader classpathLoader;
    private RenderBlocks                  renderBlocks;
    private World                         testWorld;
    private static OurCraft               instance;

    public OurCraft()
    {
        instance = this;
        classpathLoader = new ClasspathSimpleResourceLoader();
    }

    public void start() throws LWJGLException, IOException
    {
        Display.setResizable(true);
        Display.setTitle("OurCraft");
        Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        Display.create();
        running = true;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        openglTexture = OpenGLHelper.loadTexture(ImageIO.read(OurCraft.class.getResourceAsStream("/assets/textures/terrain.png")));
        renderEngine = new RenderEngine();

        Blocks.init();

        testWorld = new World();
        testWorld.addChunk(new Chunk(new ChunkCoord(0, 0, 0)));
        testWorld.addChunk(new Chunk(new ChunkCoord(0, 1, 0)));
        testWorld.addChunk(new Chunk(new ChunkCoord(-1, 0, 0)));
        for(int y = 0; y < 4; y++ )
        {
            Block block = null;
            if(y == 3)
                block = Blocks.grass;
            else
                block = Blocks.dirt;
            testWorld.setBlock(0, y, 0, block);
            testWorld.setBlock(0, y, 1, block);
            testWorld.setBlock(0, y, 2, block);
            testWorld.setBlock(0, y, 3, block);
            testWorld.setBlock(1, y, 3, block);
            testWorld.setBlock(2, y, 3, block);
            testWorld.setBlock(3, y, 3, block);

            testWorld.setBlock(0, y, 0, block);
            testWorld.setBlock(1, y, 0, block);
            testWorld.setBlock(2, y, 0, block);
            testWorld.setBlock(3, y, 0, block);
        }

        testWorld.setBlock(-1, 0, 0, Blocks.grass);

        Log.message("Block at (0,0,0) is " + testWorld.getBlock(0, 0, 0).getID());
        Log.message("Block at (0,1,0) is " + testWorld.getBlock(0, 1, 0).getID());
        Log.message("Block at (-1,0,0) is " + testWorld.getBlock(-1, 0, 0).getID());
        renderBlocks = new RenderBlocks(renderEngine);

        ArrayList<Chunk> visiblesChunks = new ArrayList<>();
        visiblesChunks.add(testWorld.getChunk(0, 0, 0));
        visiblesChunks.add(testWorld.getChunk(0, 1, 0));
        visiblesChunks.add(testWorld.getChunk(-1, 0, 0));
        renderBlocks.prepare(testWorld, visiblesChunks);

        basicShader = new Shader(IOUtils.readString(OurCraft.class.getResourceAsStream("/assets/shaders/base.vsh"), "UTF-8"), IOUtils.readString(OurCraft.class.getResourceAsStream("/assets/shaders/base.fsh"), "UTF-8"));
        projectionMatrix = new Matrix4().initPerspective((float)Math.toRadians(90), 16f / 9f, 0.0001f, 100);
        modelMatrix = new Matrix4().initIdentity();
        modelMatrix.translate(0, 0, 8);

        while(running)
        {
            tick();
            Display.sync(60);
            Display.update();

            if(Display.isCloseRequested()) running = false;
        }
    }

    private void tick()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        modelMatrix.rotate(Vector3.yAxis, (float)Math.toRadians(1));
        modelMatrix.rotate(Vector3.xAxis, (float)Math.toRadians(2));
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        basicShader.bind();
        basicShader.setUniform("modelview", this.modelMatrix);
        basicShader.setUniform("projection", this.projectionMatrix);

        renderBlocks.render(openglTexture);
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
}
