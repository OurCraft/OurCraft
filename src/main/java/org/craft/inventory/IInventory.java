package org.craft.inventory;

import org.craft.blocks.Stack;

public interface IInventory
{
    /**
     * Returns the number of slots in the inventory.
     */
    int getSizeInventory();

    /**
     * Returns the stack in slot i
     */
    Stack getStackInSlot(int slot);

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    Stack decrStackSize(int slot, int number);


    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    void setInventorySlotContents(int slot, Stack stack);

    /**
     * Returns the name of the inventory
     */
    String getInventoryName();
    
    void openInventory();

    void closeInventory();
}
