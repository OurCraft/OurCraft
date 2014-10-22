package org.craft.client.render;

public enum EnumRenderPass
{
    NORMAL(0), ALPHA(1);

    private int id;

    private EnumRenderPass(int id)
    {
        this.id = id;
    }

    /**
     * Gets id of pass
     */
    public int id()
    {
        return id;
    }

    /**
     * Returns pass from id
     */
    public static EnumRenderPass fromID(int id)
    {
        for(EnumRenderPass pass : values())
        {
            if(pass.id == id)
                return pass;
        }
        return null;
    }
}
