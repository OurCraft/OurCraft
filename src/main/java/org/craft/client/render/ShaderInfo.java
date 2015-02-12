package org.craft.client.render;

public class ShaderInfo
{

    private Shader shader;
    private int    width;
    private int    height;

    public ShaderInfo(Shader shader)
    {
        this(shader, -1, -1);
    }

    public ShaderInfo(Shader shader, int width, int height)
    {
        this.shader = shader;
        this.width = width;
        this.height = height;
    }

    public Shader getShader()
    {
        return shader;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public ShaderInfo setWidth(int width)
    {
        this.width = width;
        return this;
    }

    public ShaderInfo setHeight(int height)
    {
        this.height = height;
        return this;
    }

    public ShaderInfo setShader(Shader shader)
    {
        this.shader = shader;
        return this;
    }

    public ShaderInfo copy()
    {
        return new ShaderInfo(getShader(), width, height);
    }

}
