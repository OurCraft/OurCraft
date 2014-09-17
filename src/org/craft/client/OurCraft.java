package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import javax.imageio.*;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.resources.*;
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
        Display.setTitle("OurCraft");
        Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        Display.create();
        running = true;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        openglTexture = OpenGLHelper.loadTexture(ImageIO.read(OurCraft.class.getResourceAsStream("/assets/textures/terrain.png")));
        renderEngine = new RenderEngine();

        Blocks.init();

        testWorld = new World();
        testWorld.setChunk(0, 0, 0, new Chunk());
        testWorld.setBlock(0, 0, 0, Blocks.dirt);
        testWorld.setBlock(0, 1, 0, Blocks.grass);
        renderBlocks = new RenderBlocks(renderEngine);

        renderBlocks.drawAllFaces(Blocks.dirt, testWorld, 0, 0, 0);
        renderBlocks.drawAllFaces(Blocks.grass, testWorld, 0, 1, 0);

        renderBlocks.flush();

        basicShader = new Shader("" + //
                "#version 120" + "\n" + //
                "attribute vec3 pos;" + "\n" + //
                "attribute vec2 texCoords;" + "\n" + //
                "varying vec2 texCoord0;" + "\n" + //
                "uniform mat4 modelview;" + "\n" + //
                "uniform mat4 projection;" + "\n" + //
                "void main(){" + "\n" + //
                "texCoord0 = texCoords;" + "\n" + //
                "gl_Position = projection * modelview * vec4(pos,1);" + "\n" + //
                "}" + "\n" + //
                "" + "\n"//
        , "" + //
                "#version 120" + "\n" + //
                "uniform sampler2D diffuse;" + "\n" + //
                "varying vec2 texCoord0;" + "\n" + //
                "void main(){" + "\n" + //
                "gl_FragColor = texture2D(diffuse, texCoord0);" + "\n" + //
                "}" + "\n" + //
                "" + "\n"//
        );
        projectionMatrix = new Matrix4().initPerspective((float)Math.toRadians(90), 16f / 9f, 0.0001f, 100);
        modelMatrix = new Matrix4().initIdentity();
        modelMatrix.translate(0, 0, 5);

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
