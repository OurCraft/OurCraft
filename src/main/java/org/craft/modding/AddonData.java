package org.craft.modding;

import org.craft.resources.*;

public class AddonData
{

    private String           id;
    private String           name;
    private String           version;
    private String           author;

    private String           description;
    private ResourceLocation logoPath;

    public AddonData(AddonContainer<?> container)
    {
        id = container.getId();
        name = container.getName();
        version = container.getVersion();
        author = container.getAuthor();
        description = "";
    }

    public void setDescription(String desc)
    {
        this.description = desc;
    }

    public void setLogoPath(ResourceLocation logo)
    {
        this.logoPath = logo;
    }

    public String getDescription()
    {
        return description;
    }

    public ResourceLocation getLogoPath()
    {
        return logoPath;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getName()
    {
        return name;
    }

    public String getID()
    {
        return id;
    }

    public String getVersion()
    {
        return version;
    }

    public String getAuthor()
    {
        return author;
    }
}
