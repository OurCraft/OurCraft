package org.craft.client.render;


public class OffsettedOpenGLBuffer extends OpenGLBuffer
{

    private int offset;
    private int max = 0;

    public void addIndex(int index)
    {
        super.addIndex(this.offset + index);
        if(index > max)
            max = index;
    }

    public void setOffset(int index)
    {
        this.offset = index;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffsetToEnd()
    {
        setOffset(offset + max + 1);
        max = 0;
    }

    public void incrementOffset(int i)
    {
        offset += i;
    }

}
