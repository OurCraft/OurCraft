package org.craft.client.render;


public enum EnumRenderPass
{
    NORMAL(0), ALPHA(1);

    private int id;

    private EnumRenderPass(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public static EnumRenderPass getFromId(int id)
    {
        for(EnumRenderPass pass : values())
        {
            if(pass.id == id)
                return pass;
        }
        return null;
    }
}
