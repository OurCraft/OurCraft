package org.craft;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import javax.imageio.*;

import org.craft.client.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class OurCraftMain
{

    private File    gameFolder;
    private int     displayWidth  = 960;
    private int     displayHeight = 540;
    private boolean running       = false;
    private Texture openglTexture;

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
        openglTexture.bind();
        glBegin(GL_QUADS);
        glTexCoord2d(0, 0);
        glVertex2d(0, 0);

        glTexCoord2d(1, 0);
        glVertex2d(500, 0);

        glTexCoord2d(1, 1);
        glVertex2d(500, 100);

        glTexCoord2d(0, 1);
        glVertex2d(0, 100);
        glEnd();
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
