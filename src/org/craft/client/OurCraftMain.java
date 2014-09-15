package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import javax.imageio.*;

import org.craft.client.render.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

public class OurCraftMain
{

    private File         gameFolder;
    private int          displayWidth  = 960;
    private int          displayHeight = 540;
    private boolean      running       = false;
    private Texture      openglTexture;
    private RenderEngine renderEngine  = null;
    private OpenGLBuffer testBuffer;

    public OurCraftMain()
    {

    }

    public void start() throws LWJGLException, IOException
    {
        Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        Display.create();
        running = true;
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glEnable(GL_TEXTURE_2D);
        openglTexture = OpenGLHelper.loadTexture(ImageIO.read(OurCraftMain.class.getResourceAsStream("/assets/textures/OpenGL_Logo.png")));
        renderEngine = new RenderEngine();

        testBuffer = new OpenGLBuffer();
        testBuffer.addVertex(new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)));
        testBuffer.addVertex(new Vertex(new Vector3f(300, 0, 0), new Vector2f(1, 0)));
        testBuffer.addVertex(new Vertex(new Vector3f(300, 100, 0), new Vector2f(1, 1)));
        testBuffer.addVertex(new Vertex(new Vector3f(0, 100, 0), new Vector2f(0, 1)));
        testBuffer.addIndex(0);
        testBuffer.addIndex(1);
        testBuffer.addIndex(2);
        testBuffer.addIndex(3);
        testBuffer.upload();

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
        renderEngine.renderBuffer(testBuffer, openglTexture);
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
}
