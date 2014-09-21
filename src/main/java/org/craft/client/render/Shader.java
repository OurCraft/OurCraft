package org.craft.client.render;

import static org.lwjgl.opengl.GL20.*;

import java.nio.*;
import java.util.*;

import org.craft.maths.*;
import org.craft.utils.*;
import org.lwjgl.*;

public class Shader
{

    private int                      program;
    private HashMap<String, Integer> locsMap;

    public Shader(String vert, String frag)
    {
        locsMap = new HashMap<String, Integer>();
        program = glCreateProgram();
        int vertexId = glCreateShader(GL_VERTEX_SHADER);
        int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexId, vert);
        glShaderSource(fragmentId, frag);

        glCompileShader(vertexId);
        if(glGetShaderi(vertexId, GL_COMPILE_STATUS) == 0)
        {
            System.err.println("Compilation of vertex shader failed: ");
            System.err.println(glGetShaderInfoLog(vertexId, 1024));
            return;
        }
        glCompileShader(fragmentId);
        if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) == 0)
        {
            System.err.println("Compilation of fragment shader failed: ");
            System.err.println(glGetShaderInfoLog(fragmentId, 1024));
            return;
        }

        glAttachShader(program, vertexId);
        glAttachShader(program, fragmentId);

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) == 0)
        {
            System.err.println("Linking of program failed: ");
            System.err.println(glGetProgramInfoLog(program, 1024));
            return;
        }
    }

    /**
     * Binds this program to OpenGL
     */
    public void bind()
    {
        glUseProgram(program);
    }

    /**
     * Sets the given uniform to the value of the given matrix
     */
    public Shader setUniform(String uniform, Matrix4 m)
    {
        int l = getLocation(uniform);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        m.write(buffer);
        buffer.flip();
        glUniformMatrix4(l, true, buffer);
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
}
