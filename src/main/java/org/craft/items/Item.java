package org.craft.items;

import org.craft.client.render.*;
import org.craft.inventory.*;

public class Item implements IStackable
{

    private String      id;
    private int         uid;
    private TextureIcon icon;

    public Item(String id)
    {
        this.id = id;
    }

    @Override
    public String getID()
    {
        return "ourcraft:" + id;
    }

    @Override
    public int getMaxStackQuantity()
    {
        return 64;
    }

    public void setUID(int uid)
    {
        this.uid = uid;
    }

    public int getUID()
    {
        return uid;
    }

    public TextureIcon getIcon(short damage)
    {
        return icon;
    }

    public TextureIcon getIcon(Stack stack)
    {
        return icon;
    }

    public void registerIcons(IconGenerator register)
    {
        icon = register.generateIcon(id + ".png");
    }
}
