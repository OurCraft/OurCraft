package org.craft.client.models;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class GeneratedModel extends Model
{

    private HashMap<Integer, String> layers;

    public GeneratedModel(String name)
    {
        super(name);
        layers = Maps.newHashMap();
    }

    public void addLayer(String layerIcon, int layerId)
    {
        layers.put(layerId, layerIcon);
    }

    public void generateElements()
    {
        for(Entry<Integer, String> entry : layers.entrySet())
        {
            try
            {
                BufferedImage img = ImageUtils.loadImage(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/items/" + entry.getValue() + ".png")));
                int pixels[] = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
                for(int x = 0; x < img.getWidth(); x++ )
                {
                    for(int y = 0; y < img.getHeight(); y++ )
                    {
                        int color = pixels[x + y * img.getWidth()];
                        int alpha = color >> 24 & 0xFF;
                        int red = color >> 16 & 0xFF;
                        int green = color >> 8 & 0xFF;
                        int blue = color >> 0 & 0xFF;
                        if(alpha < 0xFF)
                            continue;
                        int elemY = img.getHeight() - y - 1;
                        int elemX = img.getWidth() - x - 1;
                        ModelElement element = new ModelElement();
                        ModelFace northFace = new ModelFace();
                        northFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        northFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFrom(Vector3.get((float) elemX / (float) img.getWidth(), (float) elemY / (float) img.getHeight(), 0));
                        element.setTo(Vector3.get((float) (elemX + 1) / (float) img.getWidth(), (float) (elemY + 1) / (float) img.getHeight(), 1f / 16f));
                        element.setFace("north", northFace);

                        ModelFace southFace = new ModelFace();
                        southFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        southFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFace("south", southFace);

                        ModelFace westFace = new ModelFace();
                        westFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        westFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFace("west", westFace);

                        ModelFace eastFace = new ModelFace();
                        eastFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        eastFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFace("east", eastFace);

                        ModelFace topFace = new ModelFace();
                        topFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        topFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFace("up", topFace);

                        ModelFace downFace = new ModelFace();
                        downFace.setMinUV(Vector2.get((float) x / (float) img.getWidth(), (float) y / (float) img.getHeight()));
                        downFace.setMaxUV(Vector2.get((float) (x + 1) / (float) img.getWidth(), (float) (y + 1) / (float) img.getHeight()));
                        element.setFace("down", downFace);
                        addElement(element);
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
