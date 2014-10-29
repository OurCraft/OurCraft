package org.craft.client.render;

import java.util.*;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.models.*;
import org.craft.client.render.items.*;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;

public class RenderItems
{

    private RenderEngine                      renderEngine;
    private HashMap<IStackable, ItemRenderer> renderers;
    private OffsettedOpenGLBuffer             buffer;
    private ModelLoader                       modelLoader;

    public RenderItems(RenderEngine engine, ModelLoader modelLoader)
    {
        this.modelLoader = modelLoader;
        renderers = Maps.newHashMap();
        this.renderEngine = engine;
        buffer = new OffsettedOpenGLBuffer();
    }

    /**
     * Gets a renderer from given stack
     */
    public ItemRenderer getRenderer(IStackable item)
    {
        if(renderers.containsKey(item))
        {
            ItemRenderer renderer = renderers.get(item);
            return renderer;
        }
        try
        {
            ResourceLocation res = new ResourceLocation("ourcraft", "models/items/" + item.getId().split(":")[1] + ".json");
            if(OurCraft.getOurCraft().getAssetsLoader().doesResourceExists(res))
            {
                renderers.put(item, modelLoader.createItemRenderer(res, renderEngine.blocksAndItemsMap));
                Log.message(res.getFullPath() + " loaded.");
            }
            else
            {
                Log.message(res.getFullPath() + " doesn't exist.");
                renderers.put(item, new FallbackItemRenderer());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return renderers.get(item);
    }

    /**
     * Renders given item
     */
    public void renderItem(RenderEngine engine, Stack item, World w, float x, float y, float z)
    {
        buffer.setOffset(0);
        buffer.clear();
        engine.bindLocation(engine.blocksAndItemsMapLocation);
        ItemRenderer renderer = getRenderer(item.getItem());
        renderer.renderItem(engine, buffer, item.getItem(), item, x, y, z);
        buffer.upload();
        engine.renderBuffer(buffer);
    }
}
