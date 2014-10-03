package org.craft.entity;

import java.util.*;

import org.craft.blocks.*;
import org.craft.inventory.*;
import org.craft.inventory.Stack;
import org.craft.utils.*;
import org.craft.world.*;
import org.spongepowered.api.component.attribute.*;
import org.spongepowered.api.entity.*;

public class EntityPlayer extends EntityLiving implements Player, LevelProgressable, Feedable
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
        inventory.setInventorySlotContents(0, new Stack(Blocks.dirtSlab));
        setSize(0.75f, 1.80f, 0.75f);
    }

    public float getEyeOffset()
    {
        return 1.7f;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
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

    @Override
    public double getExperience()
    {
        return experience;
    }

    @Override
    public int getLevel()
    {
        return xpLevel;
    }

    @Override
    public void setExperience(double experience)
    {
        this.experience = experience;
    }

    @Override
    public void setLevel(int level)
    {
        this.xpLevel = level;
    }

    @Override
    public double getHunger()
    {
        return hunger;
    }

    @Override
    public double getSaturation()
    {
        return saturation;
    }

    @Override
    public void setHunger(double hunger)
    {
        this.hunger = hunger;
    }

    @Override
    public void setSaturation(double saturation)
    {
        this.saturation = saturation;
    }
}
