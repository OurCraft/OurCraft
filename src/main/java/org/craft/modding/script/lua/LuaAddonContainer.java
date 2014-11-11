package org.craft.modding.script.lua;

import java.lang.annotation.*;

import org.craft.modding.*;

public class LuaAddonContainer extends AddonContainer<Mod>
{

    private String id;
    private String name;
    private String author;
    private String mainClass;
    private String version;

    public LuaAddonContainer(final String id, final String name, final String version, final String author, final String mainClass)
    {
        /**
         * "Hacks, Hacks everywhere" -jglrxavpok
         */
        super(new Mod()
        {

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return Mod.class;
            }

            @Override
            public String id()
            {
                return id;
            }

            @Override
            public String name()
            {
                return name;
            }

            @Override
            public String version()
            {
                return version;
            }

            @Override
            public String author()
            {
                return author;
            }
        }, null);
        this.id = id;
        this.name = name;
        this.author = author;
        this.version = version;
        this.mainClass = mainClass;
    }

    public String getAuthor()
    {
        return author;
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

    public Object getInstance()
    {
        return this;
    }

}
