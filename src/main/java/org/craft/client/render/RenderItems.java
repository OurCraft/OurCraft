package org.craft.client.render;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.client.models.*;
import org.craft.client.render.blocks.*;
import org.craft.client.render.items.*;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;

public class RenderItems
{

    public static ResourceLocation            itemMapLoc;
    private RenderEngine                      renderEngine;
    private HashMap<IStackable, ItemRenderer> renderers;
    private OffsettedOpenGLBuffer             buffer;
    private TextureMap                        itemMap;
    private ModelLoader                       modelLoader;

    public RenderItems(RenderEngine engine, ModelLoader modelLoader)
    {
        this.modelLoader = modelLoader;
        renderers = Maps.newHashMap();
        this.renderEngine = engine;
        buffer = new OffsettedOpenGLBuffer();
        createItemMap(renderEngine);
    }

    public void createItemMap(RenderEngine engine)
    {
        itemMap = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft/textures", "items"), true);
        renderers.clear();
        for(Item b : Items.ITEM_REGISTRY.values())
        {
            getRenderer(b);
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
    public ItemRenderer getRenderer(Item item)
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
                renderers.put(item, modelLoader.createItemRenderer(res, itemMap));
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
        if(item.getItem() instanceof Block)
        {
            engine.bindLocation(RenderBlocks.blockMapLoc);
            AbstractBlockRenderer renderer = OurCraft.getOurCraft().getRenderBlocks().getRenderer((Block) item.getItem());
            renderer.render(engine, buffer, w, (Block) item.getItem(), (int) x, (int) y, (int) z);
        }
        else
        {
            ItemRenderer renderer = getRenderer((Item) item.getItem());
            engine.bindLocation(itemMapLoc);
            renderer.renderItem(engine, buffer, item.getItem(), item, x, y, z);
        }
        buffer.upload();
        engine.renderBuffer(buffer);
    }
}
