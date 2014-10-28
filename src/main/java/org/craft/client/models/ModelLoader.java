package org.craft.client.models;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;
import com.google.gson.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.blocks.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class ModelLoader
{

    private Gson                                  gson;
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

    /**
     * Load a list of block variant from given resource
     */
    public List<BlockVariant> loadBlockVariants(AbstractResource variantFile, IconGenerator blockMap) throws Exception
    {
        ArrayList<BlockVariant> variants = Lists.newArrayList();
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
            ArrayList<BlockState> states = new ArrayList<BlockState>();
            ArrayList<IBlockStateValue> values = new ArrayList<IBlockStateValue>();
            for(String condition : conditions)
            {
                String[] split = condition.split("=");
                if(!condition.equals("normal"))
                {
                    BlockState state = BlockStates.getState(split[0]);

                    IBlockStateValue value = BlockStates.getValue(state, split[1]);
                    states.add(state);
                    values.add(value);

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
     * Loads model from given resource.<br/>Loaded models are then cached
     */
    public Model loadModel(AbstractResource modelFile, IconGenerator iconGenerator, Class<? extends IStackable> type) throws Exception
    {
        if(!models.containsKey(modelFile.getResourceLocation()))
        {
            Log.message("Loading model " + modelFile.getResourceLocation().getFullPath());
            Model loadedModel = new Model(modelFile.getResourceLocation().getName());
            String rawJsonData = new String(modelFile.getData(), "UTF-8");
            JsonObject model = gson.fromJson(rawJsonData, JsonObject.class);
            if(model.has("parent"))
            {
                loadedModel.copyFrom(loadModel(modelFile.getLoader().getResource(new ResourceLocation("ourcraft", "models/" + model.get("parent").getAsString() + ".json")), iconGenerator, type));
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
                                face.setCullface(faceData.get("cullface").getAsString());
                            if(faceData.has("uv"))
                            {
                                JsonArray uvArray = faceData.get("uv").getAsJsonArray();
                                face.setMinUV(Vector2.get(uvArray.get(0).getAsFloat() / 16f, uvArray.get(1).getAsFloat() / 16f));
                                face.setMaxUV(Vector2.get(uvArray.get(2).getAsFloat() / 16f, uvArray.get(3).getAsFloat() / 16f));
                            }
                            if(faceData.has("hideIfSameAdjacent"))
                                face.hideIfSameAdjacent(faceData.get("hideIfSameAdjacent").getAsBoolean());
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
            models.put(modelFile.getResourceLocation(), loadedModel);
        }
        return models.get(modelFile.getResourceLocation());
    }
}
