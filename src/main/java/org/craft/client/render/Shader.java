package org.craft.client.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.utils.io.*;

public class Shader implements IDisposable
{

    private int                      program;
    private HashMap<String, Integer> locsMap;
    private ShaderInfo               infos;
    private String                   name;
    private static FloatBuffer       floatBuffer8;
    private static FloatBuffer       floatBuffer12;
    private static FloatBuffer       floatBuffer16;

    public Shader(ResourceLocation vertexShader, ResourceLocation fragmentShader) throws IOException
    {
        this(OurCraft.getOurCraft().getAssetsLoader(), vertexShader, fragmentShader);
    }

    public Shader(ResourceLoader loader, ResourceLocation vertexShader, ResourceLocation fragmentShader) throws IOException
    {
        AbstractResource vertRes = loader.getResource(vertexShader);
        this.name = vertRes.getResourceLocation().getName().replace("." + vertRes.getResourceLocation().getExtension(), "");
        AbstractResource fragRes = loader.getResource(fragmentShader);
        String vert = new String(vertRes.getData(), "UTF-8");
        String frag = new String(fragRes.getData(), "UTF-8");
        init(vert, frag);
    }

    public Shader(String vert, String frag)
    {
        name = vert.substring(20, 80).replace("\n", "$%$").replace("\r", "$%$");
        init(vert, frag);
    }

    public String getName()
    {
        return name;
    }

    private void init(String vert, String frag)
    {
        infos = new ShaderInfo(this);
        locsMap = new HashMap<String, Integer>();
        program = glCreateProgram();
        int vertexId = glCreateShader(GL_VERTEX_SHADER);
        int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexId, vert);
        glShaderSource(fragmentId, frag);

        glCompileShader(vertexId);
        if(glGetShaderi(vertexId, GL_COMPILE_STATUS) == 0)
        {
            Log.error("[Shader " + name + "] Compilation of vertex shader failed: ");
            Log.error(glGetShaderInfoLog(vertexId, 1024));
            return;
        }
        glCompileShader(fragmentId);
        if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) == 0)
        {
            Log.error("[Shader " + name + "] Compilation of fragment shader failed: ");
            Log.error(glGetShaderInfoLog(fragmentId, 1024));
            return;
        }

        glAttachShader(program, vertexId);
        glAttachShader(program, fragmentId);

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) == 0)
        {
            Log.error("[Shader " + name + "] Linking of program failed: ");
            Log.error(glGetProgramInfoLog(program, 1024));
            return;
        }
    }

    public Shader(Shader other)
    {
        infos = other.getInfos().copy().setShader(this);
        locsMap = other.locsMap;
        program = other.program;
    }

    public Shader(String name, String vert, String frag)
    {
        this.name = name;
        init(vert, frag);
    }

    /**
     * Binds this program to OpenGL
     */
    public void bind()
    {
        glUseProgram(program);
    }

    /**
     * Sets the given uniform to the value of the given vec2
     */
    public Shader setUniform(String uniform, Vector2 v)
    {
        int l = getLocation(uniform);
        if(l == -1)
            return this;
        glUniform2f(l, v.x, v.y);
        return this;
    }

    /**
     * Sets the given uniform to the value of the given vec3
     */
    public Shader setUniform(String uniform, Vector3 v)
    {
        int l = getLocation(uniform);
        if(l == -1)
            return this;
        glUniform3f(l, v.getX(), v.getY(), v.getZ());
        return this;
    }

    /**
     * Sets the given uniform to the value of the given matrix
     */
    public Shader setUniform(String uniform, Matrix4 m)
    {
        int l = getLocation(uniform);
        if(l == -1)
            return this;
        if(floatBuffer16 == null)
        {
            floatBuffer16 = IOUtils.createFloatBuffer(4 * 4);
        }
        floatBuffer16.clear();
        FloatBuffer buffer = floatBuffer16;
        m.write(buffer);
        buffer.flip();
        glUniformMatrix4fv(l, true, buffer);
        return this;
    }

    private int getLocation(String uniform)
    {
        if(!locsMap.containsKey(uniform))
        {
            int loc = glGetUniformLocation(program, uniform);
            if(loc == -1)
            {
                Log.error("Uniform named " + uniform + " not found in shader");
            }
            locsMap.put(uniform, loc);
        }
        return locsMap.get(uniform);
    }

    @Override
    public void dispose()
    {
        glDeleteProgram(program);
    }

    public ShaderInfo getInfos()
    {
        return infos;
    }
}
