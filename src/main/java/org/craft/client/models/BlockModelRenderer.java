package org.craft.client.models;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.blocks.*;
import org.craft.maths.*;
import org.craft.world.*;

public class BlockModelRenderer extends AbstractBlockRenderer
{

    private BlockModel                   blockModel;
    private HashMap<String, TextureIcon> icons;

    public BlockModelRenderer(BlockModel blockModel)
    {
        this.blockModel = blockModel;
        icons = Maps.newHashMap();
    }

    @Override
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z)
    {
        if(!b.shouldRender())
            return;
        Chunk chunk = w.getChunk(x, y, z);
        if(chunk == null)
            return;
        for(int i = 0; i < blockModel.getElementsCount(); i++ )
        {
            BlockElement element = blockModel.getElement(i);
            Iterator<Entry<String, BlockFace>> it = element.getFaces().entrySet().iterator();
            Vector3 startPos = element.getFrom();
            Vector3 size = element.getTo().sub(startPos);
            while(it.hasNext())
            {
                Entry<String, BlockFace> entry = it.next();
                Vector3 faceStart = Vector3.NULL;
                Vector3 faceSize = Vector3.NULL;
                TextureIcon icon = getTexture(blockModel, element, entry.getValue().getTexture());
                boolean flip = false;
                if(entry.getKey().equals("up"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY() + size.getY(), startPos.getZ());
                    faceSize = Vector3.get(size.getX(), 0, size.getZ());
                    flip = true;
                }
                else if(entry.getKey().equals("down"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY(), startPos.getZ());
                    faceSize = Vector3.get(size.getX(), 0, size.getZ());
                    flip = true;
                }

                else if(entry.getKey().equals("west"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY(), startPos.getZ());
                    faceSize = Vector3.get(0, size.getY(), size.getZ());
                }
                else if(entry.getKey().equals("east"))
                {
                    faceStart = Vector3.get(startPos.getX() + size.getX(), startPos.getY(), startPos.getZ());
                    faceSize = Vector3.get(0, size.getY(), size.getZ());
                }

                else if(entry.getKey().equals("north"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY(), startPos.getZ());
                    faceSize = Vector3.get(size.getX(), size.getY(), 0);
                }
                else if(entry.getKey().equals("south"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY(), startPos.getZ() + size.getZ());
                    faceSize = Vector3.get(size.getX(), size.getY(), 0);
                }

                else
                {
                    continue;
                }
                faceSize.dispose();
                faceStart.dispose();
                if(icon != null)
                    renderFace(buffer, w, b, x, y, z, icon, faceStart, faceSize, flip, entry.getValue().getMinUV(), entry.getValue().getMaxUV());
            }
        }
    }

    private TextureIcon getTexture(BlockModel blockModel, BlockElement element, String texture)
    {
        if(texture.startsWith("#"))
            return getTexture(blockModel, element, blockModel.getTexturePath(texture.substring(1)));
        if(!icons.containsKey(texture))
        {
            TextureMap blockMap = (TextureMap) OurCraft.getOurCraft().getRenderEngine().getByLocation(RenderBlocks.blockMapLoc);
            icons.put(texture, blockMap.get(texture + ".png"));
        }
        return icons.get(texture);
    }
}
