package org.craft.resources;

import org.craft.utils.*;

public class ResourceLocation
{

    private String           section;
    private String           path;
    private ResourceLocation parent;

    public ResourceLocation(String location)
    {
        if(location.contains(":"))
        {
            section = location.split(":")[0];
            path = location.split(":")[1];
        }
        else
        {
            section = "";
            path = location;
        }
    }

    public ResourceLocation(String section, String path)
    {
        this.section = section;
        this.path = path;
    }

    public ResourceLocation(ResourceLocation parent, String path)
    {
        this(parent.getSection(), parent.getPath() + "/" + path);
        this.parent = parent;
    }

    public String getSection()
    {
        return section;
    }

    public String getPath()
    {
        return path;
    }

    public String getFullPath()
    {
        String prefix = "";
        if(!section.equals(""))
            prefix = section + "/";
        return prefix + path;
    }

    public String getExtension()
    {
        if(path.contains("/"))
        {
            String tokens[] = path.split("/");
            String token = tokens[tokens.length - 1];
            if(token.contains("."))
            {
                tokens = token.split("\\.");
                return tokens[tokens.length - 1];
            }
            return token;
        }
        else
        {
            if(path.contains("."))
            {
                String tokens[] = path.split("\\.");
                return tokens[tokens.length - 1];
            }
        }
        return path;
    }

    /**
     * Returns direct parent
     */
    public ResourceLocation getDirectParent()
    {
        if(parent == null)
        {
            if(path.contains("/"))
            {
                String tokens[] = path.split("/");
                parent = new ResourceLocation(section, ArraysUtils.sum(tokens, 0, tokens.length - 1, "/"));

            }
            else
            {
                parent = new ResourceLocation(section);
            }
        }
        return parent;
    }

    public String getName()
    {
        if(path.contains("/"))
        {
            String tokens[] = path.split("/");
            return tokens[tokens.length - 1];
        }
        else
        {
            return path;
        }
    }

    public boolean equals(Object o)
    {
        if(o instanceof ResourceLocation)
        {
            ResourceLocation res = (ResourceLocation) o;
            return res.getFullPath().equals(getFullPath());
        }
        return false;
    }

    public ResourceLocation getChild(String child)
    {
        return new ResourceLocation(getFullPath() + "/" + child);
    }
}
