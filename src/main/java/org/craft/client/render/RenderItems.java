package org.craft.client.render;

import java.util.*;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.render.items.*;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.resources.*;

public class RenderItems
{

    public static ResourceLocation            itemMapLoc;
    private RenderEngine                      renderEngine;
    private HashMap<IStackable, ItemRenderer> renderers;
    private OffsettedOpenGLBuffer             buffer;

    public RenderItems(RenderEngine engine)
    {
        renderers = Maps.newHashMap();
        this.renderEngine = engine;
        buffer = new OffsettedOpenGLBuffer();
        if(itemMapLoc == null)
            createItemMap(renderEngine);
    }

    public static void createItemMap(RenderEngine engine)
    {
        TextureMap itemMap = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft/textures", "items"), true);
        for(Item b : Items.ITEM_REGISTRY.values())
        {
            b.registerIcons(itemMap);
        }
        try
        {
            itemMap.compile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        itemMapLoc = new ResourceLocation("ourcraft", "textures/atlases/items.png");
        engine.registerLocation(itemMapLoc, itemMap);
    }

    /**
     * Gets a renderer from given stack
     */
    public ItemRenderer getRenderer(Stack s)
    {
        if(!renderers.containsKey(s.getItem()))
        {
            renderers.put(s.getItem(), new FallbackItemRenderer());
        }
        return renderers.get(s.getItem());
    }

    /**
     * Renders given item
     */
    public void renderItem(RenderEngine engine, Stack item, float x, float y, float z)
    {
        buffer.clear();
        ItemRenderer renderer = getRenderer(item);
        renderer.renderItem(engine, buffer, item.getItem(), item, x, y, z);
        buffer.upload();
        engine.bindLocation(itemMapLoc);
        engine.renderBuffer(buffer);
    }
}
