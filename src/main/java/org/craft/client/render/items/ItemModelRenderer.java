package org.craft.client.render.items;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.inventory.Stack;
import org.craft.items.*;
import org.craft.maths.*;

public class ItemModelRenderer extends ItemRenderer
{

    private HashMap<String, TextureIcon> icons;
    private Model                        itemModel;
    private static Quaternion            rotationQuaternion;

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

    private static final TextureIcon NULLICON = new NullTextureIcon();

    public void renderFace(float shininess, OffsettedOpenGLBuffer buffer, float x, float y, float z, TextureIcon icon, Vector3 startPos, Vector3 size, boolean flipV, Vector2 minUV, Vector2 maxUV, Vector3 rotationOrigin, Quaternion rotation, boolean rescale)
    {
        float startX = startPos.getX();
        float startY = startPos.getY();
        float startZ = startPos.getZ();

        float width = size.getX();
        float height = size.getY();
        float depth = size.getZ();
        if(icon == null)
            icon = NULLICON;
        float deltaX = icon.getMaxU() - icon.getMinU();
        float deltaY = icon.getMaxV() - icon.getMinV();
        float minU = icon.getMinU() + minUV.getX() * deltaX;
        float minV = icon.getMinV() + minUV.getY() * deltaY;
        float maxU = icon.getMinU() + maxUV.getX() * deltaX;
        float maxV = icon.getMinV() + maxUV.getY() * deltaY;
        if(flipV)
        {
            Vector3 p0 = Vector3.get(startX, height + startY, startZ);
            Vector3 p1 = Vector3.get(startX, startY, depth + startZ);
            Vector3 p2 = Vector3.get(width + startX, height + startY, depth + startZ);
            Vector3 p3 = Vector3.get(width + startX, startY, startZ);

            if(rotationOrigin != null)
            {
                Matrix4 initialTranslation = Matrix4.get().initTranslation(rotationOrigin.getX(), rotationOrigin.getY(), rotationOrigin.getZ());
                Matrix4 initialTranslation2 = Matrix4.get().initTranslation(-rotationOrigin.getX(), -rotationOrigin.getY(), -rotationOrigin.getZ());
                Matrix4 quaternionAsMatrix = rotation.toRotationMatrix();
                Matrix4 rotationMatrix = initialTranslation.mul(quaternionAsMatrix);
                Matrix4 finalTransformation = rotationMatrix.mul(initialTranslation2);

                Vector3 rotatedPos0 = finalTransformation.transform(p0);
                Vector3 rotatedPos1 = finalTransformation.transform(p1);
                Vector3 rotatedPos2 = finalTransformation.transform(p2);
                Vector3 rotatedPos3 = finalTransformation.transform(p3);
                p0.dispose();
                p1.dispose();
                p2.dispose();
                p3.dispose();
                p0 = rotatedPos0;
                p1 = rotatedPos1;
                p2 = rotatedPos2;
                p3 = rotatedPos3;
                quaternionAsMatrix.dispose();
                initialTranslation.dispose();
                initialTranslation2.dispose();
                rotationMatrix.dispose();
                finalTransformation.dispose();
            }
            buffer.addVertex(Vertex.get(p0.add(x, y, z), Vector2.get(minU, minV), Vector3.get(shininess, shininess, shininess))); // 2
            buffer.addVertex(Vertex.get(p1.add(x, y, z), Vector2.get(minU, maxV), Vector3.get(shininess, shininess, shininess))); // 0
            buffer.addVertex(Vertex.get(p2.add(x, y, z), Vector2.get(maxU, maxV), Vector3.get(shininess, shininess, shininess))); // 4
            buffer.addVertex(Vertex.get(p3.add(x, y, z), Vector2.get(maxU, minV), Vector3.get(shininess, shininess, shininess))); // 6
            p0.dispose();
            p1.dispose();
            p2.dispose();
            p3.dispose();
        }
        else
        {
            Vector3 p0 = Vector3.get(startX, startY, startZ);
            Vector3 p1 = Vector3.get(startX, height + startY, startZ);
            Vector3 p2 = Vector3.get(width + startX, height + startY, depth + startZ);
            Vector3 p3 = Vector3.get(width + startX, startY, depth + startZ);
            if(rotationOrigin != null)
            {
                Matrix4 initialTranslation = Matrix4.get().initTranslation(rotationOrigin.getX(), rotationOrigin.getY(), rotationOrigin.getZ());
                Matrix4 initialTranslation2 = Matrix4.get().initTranslation(-rotationOrigin.getX(), -rotationOrigin.getY(), -rotationOrigin.getZ());
                Matrix4 quaternionAsMatrix = rotation.toRotationMatrix();
                Matrix4 rotationMatrix = initialTranslation.mul(quaternionAsMatrix);
                Matrix4 finalTransformation = rotationMatrix.mul(initialTranslation2);

                Vector3 rotatedPos0 = finalTransformation.transform(p0);
                Vector3 rotatedPos1 = finalTransformation.transform(p1);
                Vector3 rotatedPos2 = finalTransformation.transform(p2);
                Vector3 rotatedPos3 = finalTransformation.transform(p3);
                p0.dispose();
                p1.dispose();
                p2.dispose();
                p3.dispose();
                p0 = rotatedPos0;
                p1 = rotatedPos1;
                p2 = rotatedPos2;
                p3 = rotatedPos3;
                if(rescale)
                {
                    // TODO: rescaling
                }
                quaternionAsMatrix.dispose();
                initialTranslation.dispose();
                initialTranslation2.dispose();
                rotationMatrix.dispose();
                finalTransformation.dispose();
            }
            buffer.addVertex(Vertex.get(p0.add(x, y, z), Vector2.get(minU, maxV), Vector3.get(shininess, shininess, shininess))); // 0
            buffer.addVertex(Vertex.get(p1.add(x, y, z), Vector2.get(minU, minV), Vector3.get(shininess, shininess, shininess))); // 2
            buffer.addVertex(Vertex.get(p2.add(x, y, z), Vector2.get(maxU, minV), Vector3.get(shininess, shininess, shininess))); // 4
            buffer.addVertex(Vertex.get(p3.add(x, y, z), Vector2.get(maxU, maxV), Vector3.get(shininess, shininess, shininess))); // 6
            p0.dispose();
            p1.dispose();
            p2.dispose();
            p3.dispose();
        }
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);
        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
        buffer.setOffsetToEnd();
    }

}
