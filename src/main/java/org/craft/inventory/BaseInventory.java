package org.craft.inventory;

public class BaseInventory implements IInventory
{

    private Stack[] stacks;
    private int     size;
    private String  invName;

    public BaseInventory(String invName, int size)
    {
        this.invName = invName;
        this.size = size;
        stacks = new Stack[size];
    }

    @Override
    public int getSizeInventory()
    {
        return size;
    }

    @Override
    public Stack getStackInSlot(int slot)
    {
        if(slot < 0 || slot >= size)
            return null;
        return stacks[slot];
    }

    @Override
    public Stack decrStackSize(int slot, int number)
    {
        Stack stack = getStackInSlot(slot);
        if(stack != null)
        {
            stack.setQuantity(stack.getQuantity() - number);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, Stack stack)
    {
        if(slot < 0 || slot >= size)
        {
            return;
        }
        stacks[slot] = stack;
    }

    @Override
    public String getInventoryName()
    {
        return invName;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

}
