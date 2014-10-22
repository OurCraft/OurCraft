package org.craft.client.render;

public class OffsettedOpenGLBuffer extends OpenGLBuffer
{

    /**
     * Offset in indices list
     */
    private int offset;

    /**
     * Max index registred
     */
    private int max = 0;

    public void addIndex(int index)
    {
        super.addIndex(this.offset + index);
        if(index > max)
            max = index;
    }

    /**
     * Sets offset of this buffer
     */
    public void setOffset(int index)
    {
        this.offset = index;
    }

    /**
     * Gets current offset in indices list
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * Sets current offset at the end of the indices list
     */
    public void setOffsetToEnd()
    {
        setOffset(offset + max + 1);
        max = 0;
    }

    /**
     * Increments the offset by given amount
     */
    public void incrementOffset(int amount)
    {
        offset += amount;
    }

}
