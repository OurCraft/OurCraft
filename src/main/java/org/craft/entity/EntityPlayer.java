package org.craft.entity;

import java.util.*;

import org.craft.blocks.*;
import org.craft.inventory.*;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.nbt.*;
import org.craft.utils.*;
import org.craft.world.*;

public class EntityPlayer extends EntityLiving
{
    private UUID   uuid;
    private String name;
    private String displayName;
    private double experience;
    private int    xpLevel;
    private double saturation;
    private double hunger;

    public EntityPlayer(World world, UUID uuid)
    {
        super(world);
        this.stepHeight = 0.75f;
        this.uuid = uuid;
        this.name = SessionManager.getInstance().getName(uuid);
        this.displayName = SessionManager.getInstance().getDisplayName(uuid);
        this.inventory = new PlayerInventory(name, 36);
        inventory.setInventorySlotContents(0, new Stack(Blocks.log));
        inventory.setInventorySlotContents(1, new Stack(Blocks.dirt));
        inventory.setInventorySlotContents(2, new Stack(Blocks.grass));
        inventory.setInventorySlotContents(3, new Stack(Blocks.powerSource));
        inventory.setInventorySlotContents(4, new Stack(Items.test));
        //        inventory.setInventorySlotContents(5, new Stack(Items.test2));
        inventory.setInventorySlotContents(6, new Stack(Blocks.glass));
        inventory.setInventorySlotContents(7, new Stack(Blocks.leaves));
        inventory.setInventorySlotContents(8, new Stack(Blocks.rose));
        inventory.setInventorySlotContents(9, new Stack(Blocks.cable));
        setSize(0.75f, 1.80f, 0.75f);
    }

    public float getEyeOffset()
    {
        return 1.7f;
    }

    public String getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public UUID generateUUID()
    {
        return uuid;
    }

    public org.craft.inventory.Stack getHeldItem()
    {
        return inventory.getStackInSlot(((PlayerInventory) inventory).getSelectedIndex());
    }

    public double getExperience()
    {
        return experience;
    }

    public int getLevel()
    {
        return xpLevel;
    }

    public void setExperience(double experience)
    {
        this.experience = experience;
    }

    public void setLevel(int level)
    {
        this.xpLevel = level;
    }

    public double getHunger()
    {
        return hunger;
    }

    public double getSaturation()
    {
        return saturation;
    }

    public void setHunger(double hunger)
    {
        this.hunger = hunger;
    }

    public void setSaturation(double saturation)
    {
        this.saturation = saturation;
    }

    public void sendMessage(String message)
    {
        // TODO: Implementation
    }

    public void readFromNBT(NBTCompoundTag compound)
    {
        super.readFromNBT(compound);
        xpLevel = compound.getInt("xpLevel");
        experience = compound.getDouble("experience");

        saturation = compound.getDouble("saturation");
        hunger = compound.getDouble("hunger");
        displayName = compound.getString("displayName");
    }

    public void writeToNBT(NBTCompoundTag compound)
    {
        super.writeToNBT(compound);
        compound.putInt("xpLevel", xpLevel);
        compound.putDouble("experience", experience);

        compound.putDouble("saturation", saturation);
        compound.putDouble("hunger", hunger);
        compound.putString("displayName", displayName);
    }
}
