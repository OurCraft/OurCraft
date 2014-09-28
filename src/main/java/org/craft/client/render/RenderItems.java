package org.craft.client.render;

import org.craft.client.*;
import org.craft.items.*;
import org.craft.resources.*;

public class RenderItems
{

    public static ResourceLocation itemMapLoc;
    private RenderEngine           renderEngine;

    public RenderItems(RenderEngine engine)
    {
        this.renderEngine = engine;
        if(itemMapLoc == null)
            createItemMap(renderEngine);
    }

    public static void createItemMap(RenderEngine engine)
    {
        TextureMap blockMap = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft/textures", "items"), true);
        for(Item b : Items.ITEM_REGISTRY.values())
        {
            b.registerIcons(blockMap);
        }
        try
        {
            blockMap.compile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        itemMapLoc = new ResourceLocation("ourcraft", "textures/atlases/items.png");
        engine.registerLocation(itemMapLoc, blockMap);
    }
}
