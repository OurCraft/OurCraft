package org.craft.inventory;

public class PlayerInventory extends BaseInventory
{

    public Stack keptByMouse;
    private byte selectedIndex;

    public PlayerInventory(String invName, int size)
    {
        super(invName, size);
    }

    public byte getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(byte index)
    {
        this.selectedIndex = index;
    }

}
