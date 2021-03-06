package org.craft.client.models;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.craft.blocks.Block;
import org.craft.blocks.states.BlockState;
import org.craft.blocks.states.BlockStates;
import org.craft.blocks.states.IBlockStateValue;
import org.craft.client.OurCraft;
import org.craft.client.render.EnumRenderPass;
import org.craft.client.render.IconGenerator;
import org.craft.client.render.blocks.AbstractBlockRenderer;
import org.craft.client.render.blocks.BlockModelRenderer;
import org.craft.client.render.items.ItemModelRenderer;
import org.craft.client.render.items.ItemRenderer;
import org.craft.items.IStackable;
import org.craft.items.Item;
import org.craft.maths.Vector2;
import org.craft.maths.Vector3;
import org.craft.resources.AbstractResource;
import org.craft.resources.ResourceLocation;
import org.craft.utils.Dev;
import org.craft.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ModelLoader
{

    private Gson                             gson;
    private HashMap<ResourceLocation, Model> models;

    public ModelLoader()
    {
        gson = new Gson();
        models = new HashMap<ResourceLocation, Model>();
    }

    /**
     * Generates a new block renderer from given ResourceLocation. Actual resource will be get from the assets loader
     */
    public AbstractBlockRenderer createBlockRenderer(ResourceLocation modelFile, IconGenerator blockMap) throws Exception
    {
        return createBlockRenderer(OurCraft.getOurCraft().getAssetsLoader().getResource(modelFile), blockMap);
    }

    /**
     * Generates a new block renderer from given Resource
     */
    public AbstractBlockRenderer createBlockRenderer(AbstractResource modelFile, IconGenerator blockMap) throws Exception
    {
        return new BlockModelRenderer(loadBlockVariants(modelFile, blockMap));
    }

    public AbstractBlockRenderer createDefaultBlockRenderer(BlockVariant blockVariant, IconGenerator blockMap)
    {
        List<BlockVariant> variants = new ArrayList<BlockVariant>();
        variants.add(blockVariant);
        return new BlockModelRenderer(variants);
    }

    /**
     * Load a list of block variant from given resource
     */
    public List<BlockVariant> loadBlockVariants(AbstractResource variantFile, IconGenerator blockMap) throws Exception
    {
        List<BlockVariant> variants = Lists.newArrayList();
        String rawJsonData = new String(variantFile.getData(), "UTF-8");
        JsonObject data = gson.fromJson(rawJsonData, JsonObject.class);
        JsonObject variantsObject = data.get("variants").getAsJsonObject();
        for(Entry<String, JsonElement> entry : variantsObject.entrySet())
        {
            BlockVariant variant = new BlockVariant();
            String[] conditions = null;
            if(entry.getKey().contains("&"))
            {
                conditions = entry.getKey().split("&");
            }
            else
                conditions = new String[]
                {
                        entry.getKey()
                };
            List<BlockState> states = Lists.newArrayList();
            List<IBlockStateValue> values = Lists.newArrayList();
            for(String condition : conditions)
            {
                String[] split = condition.split("=");
                if(!condition.equals("normal"))
                {
                    BlockState state = BlockStates.getState(split[0]);

                    IBlockStateValue value = BlockStates.getValue(state, split[1]);
                    states.add(state);
                    values.add(value);

                    if(Dev.debug())
                        Log.message("Found variant: " + state + "=" + value);
                }
            }
            variant.setBlockStateKeys(states);
            variant.setBlockStateValues(values);
            JsonArray array = entry.getValue().getAsJsonArray();
            for(int i = 0; i < array.size(); i++ )
            {
                JsonObject model = array.get(0).getAsJsonObject();
                variant.addBlockModel(loadModel(variantFile.getLoader().getResource(new ResourceLocation("ourcraft", "models/block/" + model.get("model").getAsString() + ".json")), blockMap, Block.class));

                if(model.has("renderPass"))
                    variant.setPass(EnumRenderPass.valueOf(model.get("renderPass").getAsString().toUpperCase()));
                // TODO: rotations
            }

            variants.add(variant);
        }
        return variants;
    }

    /**
     * Loads model from given resource.
     */
    public Model loadModel(AbstractResource modelFile, IconGenerator iconGenerator, Class<? extends IStackable> type) throws Exception
    {
        if(Dev.debug())
            Log.message("Loading model " + modelFile.getResourceLocation().getFullPath());
        Model loadedModel = new Model(modelFile.getResourceLocation().getName());
        String rawJsonData = new String(modelFile.getData(), "UTF-8");
        JsonObject model = gson.fromJson(rawJsonData, JsonObject.class);
        if(model.has("parent"))
        {
            String parentPath = model.get("parent").getAsString();
            if(parentPath.startsWith("buildin/"))
            {
                String[] split = parentPath.split("/");
                String name = split[split.length - 1];
                if(name.equals("generated"))
                {
                    loadedModel = new GeneratedModel(modelFile.getResourceLocation().getName());
                    GeneratedModel generated = (GeneratedModel) loadedModel; // Convenience variable to avoid multiple casts
                    if(!model.has("textures"))
                    {
                        throw new IllegalArgumentException("Cannot generate model if no textures are specified!");
                    }
                    else
                    {
                        JsonObject textures = model.get("textures").getAsJsonObject();
                        Iterator<Entry<String, JsonElement>> textureEntries = textures.entrySet().iterator();
                        while(textureEntries.hasNext())
                        {
                            Entry<String, JsonElement> textureData = textureEntries.next();
                            String path = textureData.getValue().getAsString();
                            if(!path.startsWith("#"))
                            {
                                iconGenerator.generateIcon(path);
                                if(textureData.getKey().startsWith("layer"))
                                {
                                    int layerId = Integer.parseInt(textureData.getKey().replaceFirst("layer", ""));
                                    generated.addLayer(path, layerId);
                                }
                            }
                        }
                    }
                    generated.generateElements();
                    return generated;
                }
            }
            else
                loadedModel.copyFrom(loadModel(modelFile.getLoader().getResource(new ResourceLocation("ourcraft", "models/" + parentPath + ".json")), iconGenerator, type));
        }

        if(type == Item.class)
        {
            if(model.has("display"))
            {
                // TODO: Get infos from json file
            }
        }

        if(model.has("textures"))
        {
            JsonObject textures = model.get("textures").getAsJsonObject();
            Iterator<Entry<String, JsonElement>> textureEntries = textures.entrySet().iterator();
            while(textureEntries.hasNext())
            {
                Entry<String, JsonElement> textureData = textureEntries.next();
                String path = textureData.getValue().getAsString();
                if(!path.startsWith("#"))
                {
                    iconGenerator.generateIcon(path);
                }
                loadedModel.setTexturePath(textureData.getKey(), path);
            }
        }

        if(model.has("elements"))
        {
            JsonArray elements = model.get("elements").getAsJsonArray();
            for(int i = 0; i < elements.size(); i++ )
            {
                JsonObject element = elements.get(i).getAsJsonObject();
                ModelElement loadedElement = new ModelElement();
                if(element.has("from"))
                {
                    JsonArray fromData = element.get("from").getAsJsonArray();
                    loadedElement.setFrom(Vector3.get(fromData.get(0).getAsFloat() / 16f, fromData.get(1).getAsFloat() / 16f, fromData.get(2).getAsFloat() / 16f));
                }
                if(element.has("to"))
                {
                    JsonArray toData = element.get("to").getAsJsonArray();
                    loadedElement.setTo(Vector3.get(toData.get(0).getAsFloat() / 16f, toData.get(1).getAsFloat() / 16f, toData.get(2).getAsFloat() / 16f));
                }
                if(element.has("faces"))
                {
                    JsonObject facesObject = element.get("faces").getAsJsonObject();
                    Iterator<Entry<String, JsonElement>> it = facesObject.entrySet().iterator();
                    while(it.hasNext())
                    {
                        Entry<String, JsonElement> faceEntry = it.next();
                        JsonObject faceData = faceEntry.getValue().getAsJsonObject();
                        ModelFace face = new ModelFace();
                        if(faceData.has("texture"))
                            face.setTexture(faceData.get("texture").getAsString());
                        if(faceData.has("cullface"))
                        {
                            face.setCullface(faceData.get("cullface").getAsString());
                        }
                        if(faceData.has("uv"))
                        {
                            JsonArray uvArray = faceData.get("uv").getAsJsonArray();
                            face.setMinUV(Vector2.get(uvArray.get(0).getAsFloat() / 16f, uvArray.get(1).getAsFloat() / 16f));
                            face.setMaxUV(Vector2.get(uvArray.get(2).getAsFloat() / 16f, uvArray.get(3).getAsFloat() / 16f));
                        }
                        if(faceData.has("hideIfSameAdjacent"))
                        {
                            face.hideIfSameAdjacent(faceData.get("hideIfSameAdjacent").getAsBoolean());
                        }
                        loadedElement.setFace(faceEntry.getKey(), face);
                    }
                }
                if(element.has("rotation"))
                {
                    loadedElement.setHasRotation(true);
                    JsonObject rotationData = element.get("rotation").getAsJsonObject();
                    JsonArray originArray = rotationData.get("origin").getAsJsonArray();
                    loadedElement.setRotationOrigin(Vector3.get(originArray.get(0).getAsFloat() / 16f, originArray.get(1).getAsFloat() / 16f, originArray.get(2).getAsFloat() / 16f));
                    loadedElement.setRotationAngle(rotationData.get("angle").getAsFloat());
                    loadedElement.setRotationAxis(rotationData.get("axis").getAsString());
                    if(rotationData.has("rescale"))
                        loadedElement.shouldRescale(rotationData.get("rescale").getAsBoolean());
                }
                loadedModel.addElement(loadedElement);
            }
        }
        return loadedModel;
    }

    /**
     * Generates a new item renderer from given ResourceLocation
     */
    public ItemRenderer createItemRenderer(ResourceLocation modelFile, IconGenerator blockMap) throws Exception
    {
        return createItemRenderer(OurCraft.getOurCraft().getAssetsLoader().getResource(modelFile), blockMap);
    }

    /**
     * Generates a new item renderer from given Resource
     */
    public ItemRenderer createItemRenderer(AbstractResource modelFile, IconGenerator blockMap) throws Exception
    {
        return new ItemModelRenderer(loadModel(modelFile, blockMap, Item.class));
    }

    public void clearModels()
    {
        models.clear();
    }
}
