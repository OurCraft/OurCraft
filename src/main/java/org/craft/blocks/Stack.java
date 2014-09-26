package org.craft.blocks;

public class Stack
{
    private int   stackSize, damage;
    private Block block;

    public Stack(Block block)
    {
        this(block, 1);
    }

    public Stack(Block block, int size)
    {
        this(block, size, 0);
    }

    public Stack(Block block, int size, int damage)
    {
        this.block = block;
        this.stackSize = size;
        this.damage = damage;

        if(this.damage < 0)
            this.damage = 0;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Block getBlock()
    {
        return block;
    }
    
    /**
     * Return the stackSize
     * @return The actual size of the stack
     */
    public int getStackSize()
    {
        return stackSize;
    }
    
    /**
     * Return maximum size of the stack.
     * @return maximum size of the stack.
     */
    public int getMaxStackSize()
    {
        return block.getStackLimit();
    }
}
