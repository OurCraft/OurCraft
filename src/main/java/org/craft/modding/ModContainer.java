package org.craft.modding;

public class ModContainer extends AddonContainer<Mod>
{

    private String id;
    private String name;
    private String version;
    private String author;

    public ModContainer(Object instance, Mod addonAnnot)
    {
        super(addonAnnot, instance);
        try
        {
            id = addonAnnot.id();
            name = addonAnnot.name();
            version = addonAnnot.version();
            author = addonAnnot.author();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    @Override
    public String getAuthor()
    {
        return author;
    }

}
