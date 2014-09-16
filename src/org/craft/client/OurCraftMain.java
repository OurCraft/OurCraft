package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import javax.imageio.*;

import org.craft.client.render.*;
import org.craft.maths.*;
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
    private Matrix4      modelMatrix;
    private Shader       basicShader;

    public OurCraftMain()
    {

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
        openglTexture = OpenGLHelper.loadTexture(ImageIO.read(OurCraftMain.class.getResourceAsStream("/assets/textures/OpenGL_Logo.png")));
        renderEngine = new RenderEngine();

        testBuffer = new OpenGLBuffer();
        testBuffer.addVertex(new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)));
        testBuffer.addVertex(new Vertex(new Vector3f(300, 0, 0), new Vector2f(1, 0)));
        testBuffer.addVertex(new Vertex(new Vector3f(300, 100, 0), new Vector2f(1, 1)));
        testBuffer.addVertex(new Vertex(new Vector3f(0, 100, 0), new Vector2f(0, 1)));

        testBuffer.addIndex(0);
        testBuffer.addIndex(2);
        testBuffer.addIndex(3);

        testBuffer.addIndex(0);
        testBuffer.addIndex(1);
        testBuffer.addIndex(2);

        testBuffer.upload();

        basicShader = new Shader("" + //
                "#version 120" + "\n" + //
                "attribute vec3 pos;" + "\n" + //
                "attribute vec2 texCoords;" + "\n" + //
                "varying vec2 texCoord0;" + "\n" + //
                "uniform mat4 modelview;" + "\n" + //
                "void main(){" + "\n" + //
                "texCoord0 = texCoords;" + "\n" + //
                "gl_Position = modelview * vec4(pos,1);" + "\n" + //
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
        modelMatrix = new Matrix4().initOrthographic(0, displayWidth, displayHeight, 0, -1, 1);

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
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        basicShader.bind();
        basicShader.setUniform("modelview", this.modelMatrix);
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
