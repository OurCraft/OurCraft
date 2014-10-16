package org.craft.client.models;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.blocks.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockModelRenderer extends AbstractBlockRenderer
{

    private BlockModel                   blockModel;
    private HashMap<String, TextureIcon> icons;
    private static Quaternion            rotationQuaternion;

    public BlockModelRenderer(BlockModel blockModel)
    {
        this.blockModel = blockModel;
        icons = Maps.newHashMap();
        if(rotationQuaternion == null)
            rotationQuaternion = new Quaternion();
    }

    @Override
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z)
    {
        if(!b.shouldRender())
            return;
        Chunk chunk = w.getChunk(x, y, z);
        if(chunk == null)
            return;
        float lightValue = chunk.getLightValue(w, x, y, z);
        for(int i = 0; i < blockModel.getElementsCount(); i++ )
        {
            BlockElement element = blockModel.getElement(i);
            if(element.hasRotation())
            {
                Vector3 axis = Vector3.xAxis;
                if(element.getRotationAxis() == null)
                    ;
                else if(element.getRotationAxis().equalsIgnoreCase("y"))
                    axis = Vector3.yAxis;
                else if(element.getRotationAxis().equalsIgnoreCase("z"))
                    axis = Vector3.zAxis;
                rotationQuaternion.init(axis, (float) Math.toRadians(element.getRotationAngle()));
            }
            Set<Entry<String, BlockFace>> entries = element.getFaces().entrySet();
            Vector3 startPos = element.getFrom();
            Vector3 size = element.getTo().sub(startPos);
            for(Entry<String, BlockFace> entry : entries)
            {
                Vector3 faceStart = Vector3.NULL;
                Vector3 faceSize = Vector3.NULL;
                TextureIcon icon = getTexture(blockModel, element, entry.getValue().getTexture());
                boolean flip = false;
                EnumSide cullface = EnumSide.fromString(entry.getValue().getCullface());
                if(cullface != EnumSide.UNDEFINED)
                {
                    Block next = w.getBlockNextTo(x, y, z, cullface);
                    if(next.isSideOpaque(w, x, y, z, cullface.opposite()))
                    {
                        continue;
                    }
                }
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
                renderFace(lightValue, buffer, w, b, x, y, z, icon, faceStart, faceSize, flip, entry.getValue().getMinUV(), entry.getValue().getMaxUV(), element.getRotationOrigin(), rotationQuaternion);
                faceSize.dispose();
                faceStart.dispose();
            }
            size.dispose();
        }
    }

    private TextureIcon getTexture(BlockModel blockModel, BlockElement element, String texture)
    {
        if(texture == null)
            return null;
        if(!icons.containsKey(texture))
        {
            if(texture.startsWith("#"))
            {
                TextureIcon icon = getTexture(blockModel, element, blockModel.getTexturePath(texture.substring(1)));
                icons.put(texture, icon);
            }
            else
            {
                TextureMap blockMap = (TextureMap) OurCraft.getOurCraft().getRenderEngine().getByLocation(RenderBlocks.blockMapLoc);
                icons.put(texture, blockMap.get(texture + ".png"));
            }
        }
        return icons.get(texture);
    }
}
