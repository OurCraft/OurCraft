package org.craft.client.render.items;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.client.render.texture.TextureIcon;
import org.craft.client.render.texture.TextureMap;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.maths.*;

public class ItemModelRenderer extends ItemRenderer
{

    private        HashMap<String, TextureIcon> icons;
    private        Model                        itemModel;
    private static Quaternion                   rotationQuaternion;

    /**
     * Creates a new renderer for given item model
     */
    public ItemModelRenderer(Model itemModel)
    {
        icons = Maps.newHashMap();
        this.itemModel = itemModel;
        if(rotationQuaternion == null)
            rotationQuaternion = new Quaternion();
    }

    @Override
    public void renderItem(RenderEngine engine, OffsettedOpenGLBuffer buffer, IStackable item, Stack stack, float x, float y, float z)
    {
        Model blockModel = itemModel;
        for(int i = 0; i < blockModel.getElementsCount(); i++ )
        {
            ModelElement element = blockModel.getElement(i);
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
            Set<Entry<String, ModelFace>> entries = element.getFaces().entrySet();
            Vector3 startPos = element.getFrom();
            Vector3 size = element.getTo().sub(startPos);
            for(Entry<String, ModelFace> entry : entries)
            {
                //float shininess = 0.90f;
                float shininess = 1.00f;
                Vector3 faceStart = Vector3.NULL;
                Vector3 faceSize = Vector3.NULL;
                TextureIcon icon = getTexture(blockModel, entry.getValue().getTexture());
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
                    shininess = 0.75f;
                }
                else if(entry.getKey().equals("south"))
                {
                    faceStart = Vector3.get(startPos.getX(), startPos.getY(), startPos.getZ() + size.getZ());
                    faceSize = Vector3.get(size.getX(), size.getY(), 0);
                    shininess = 0.75f;
                }

                else
                {
                    continue;
                }
                renderFace(shininess, buffer, x, y, z, icon, faceStart, faceSize, flip, entry.getValue().getMinUV(), entry.getValue().getMaxUV(), element.getRotationOrigin(), rotationQuaternion, element.shouldRescale());
                faceSize.dispose();
                faceStart.dispose();
            }
            size.dispose();
        }
    }

    /**
     * Gets TextureIcon from texture variable found in json model file
     */
    private TextureIcon getTexture(Model itemModel, String texture)
    {
        if(texture == null)
        {
            return null;
        }
        if(!icons.containsKey(texture))
        {
            if(texture.startsWith("#"))
            {
                TextureIcon icon = getTexture(itemModel, itemModel.getTexturePath(texture.substring(1)));
                icons.put(texture, icon);
            }
            else
            {
                TextureMap itemMap = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap;
                icons.put(texture, itemMap.get(texture + ".png"));
            }
        }
        return icons.get(texture);
    }

}
