package org.craft.client.models;

import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;

import org.craft.client.*;
import org.craft.client.render.blocks.*;
import org.craft.maths.*;
import org.craft.resources.*;

public class ModelLoader
{

    private Gson                                  gson;
    private HashMap<ResourceLocation, BlockModel> models;

    public ModelLoader()
    {
        gson = new Gson();
        models = new HashMap<ResourceLocation, BlockModel>();
    }

    public AbstractBlockRenderer createRenderer(ResourceLocation modelFile) throws Exception
    {
        return createRenderer(OurCraft.getOurCraft().getAssetsLoader().getResource(modelFile));
    }

    public AbstractBlockRenderer createRenderer(AbstractResource modelFile) throws Exception
    {
        return new BlockModelRenderer(loadModel(modelFile));
    }

    public BlockModel loadModel(AbstractResource abstractResource) throws Exception
    {
        if(!models.containsKey(abstractResource.getResourceLocation()))
        {
            BlockModel loadedModel = new BlockModel();
            String rawJsonData = new String(abstractResource.getData(), "UTF-8");
            JsonObject model = gson.fromJson(rawJsonData, JsonObject.class);
            if(model.has("parent"))
            {
                loadedModel.copyFrom(loadModel(abstractResource.getLoader().getResource(new ResourceLocation("ourcraft", "models/" + model.get("parent").getAsString() + ".json"))));
            }

            if(model.has("textures"))
            {
                JsonObject textures = model.get("textures").getAsJsonObject();
                Iterator<Entry<String, JsonElement>> textureEntries = textures.entrySet().iterator();
                while(textureEntries.hasNext())
                {
                    Entry<String, JsonElement> textureData = textureEntries.next();
                    loadedModel.setTexturePath(textureData.getKey(), textureData.getValue().getAsString());
                }
            }

            if(model.has("elements"))
            {
                JsonArray elements = model.get("elements").getAsJsonArray();
                for(int i = 0; i < elements.size(); i++ )
                {
                    JsonObject element = elements.get(i).getAsJsonObject();
                    BlockElement loadedElement = new BlockElement();
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
                            BlockFace face = new BlockFace();
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
                    }
                    loadedModel.addElement(loadedElement);
                }
            }

            models.put(abstractResource.getResourceLocation(), loadedModel);
        }
        return models.get(abstractResource.getResourceLocation());
    }
}
