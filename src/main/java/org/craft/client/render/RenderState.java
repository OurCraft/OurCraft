package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

public class RenderState implements Cloneable
{

    private HashMap<Integer, Boolean> glCaps = new HashMap<Integer, Boolean>();
    private int                       blendFuncSrc;
    private int                       blendFuncDst;
    private int                       alphaFunc;
    private float                     alphaRef;
    private float                     clearColorR;
    private float                     clearColorG;
    private float                     clearColorB;
    private float                     clearColorA;

    @Override
    public RenderState clone()
    {
        RenderState copy = new RenderState();
        copy.blendFuncSrc = blendFuncSrc;
        copy.blendFuncDst = blendFuncDst;
        copy.alphaFunc = alphaFunc;
        copy.alphaRef = alphaRef;
        copy.glCaps.putAll(glCaps);
        copy.clearColorA = clearColorA;
        copy.clearColorR = clearColorR;
        copy.clearColorG = clearColorG;
        copy.clearColorB = clearColorB;
        return copy;
    }

    public float getAlphaRef()
    {
        return alphaRef;
    }

    public int getAlphaFunc()
    {
        return alphaFunc;
    }

    public int getBlendFuncSrc()
    {
        return blendFuncSrc;
    }

    public int getBlendFuncDst()
    {
        return blendFuncDst;
    }

    public HashMap<Integer, Boolean> getGLCaps()
    {
        return glCaps;
    }

    public RenderState setGLCap(int cap, boolean enabled)
    {
        glCaps.put(cap, enabled);
        return this;
    }

    public void apply(RenderEngine renderEngine)
    {
        RenderState current = renderEngine.getRenderState();
        if(current != this)
        {
            Iterator<Integer> itCurrent = current.glCaps.keySet().iterator();
            while(itCurrent.hasNext())
            {
                int cap = itCurrent.next();
                if(current.glCaps.get(cap) && !glCaps.containsKey(cap))
                    glCaps.put(cap, false);
            }
            Iterator<Integer> it = glCaps.keySet().iterator();
            while(it.hasNext())
            {
                int cap = it.next();
                if(glCaps.get(cap))
                    glEnable(cap);
                else
                    glDisable(cap);
            }
            glBlendFunc(blendFuncSrc, blendFuncDst);
            glAlphaFunc(alphaFunc, alphaRef);

            glClearColor(clearColorR, clearColorG, clearColorG, clearColorA);
        }
    }

    public void setBlendFunc(int src, int dst)
    {
        this.blendFuncSrc = src;
        this.blendFuncDst = dst;
    }

    public void setAlphaFunc(int func, float ref)
    {
        this.alphaFunc = func;
        this.alphaRef = ref;
    }

    public void setClearColor(float r, float g, float b, float a)
    {
        this.clearColorR = r;
        this.clearColorG = g;
        this.clearColorB = b;
        this.clearColorA = a;
    }
}
