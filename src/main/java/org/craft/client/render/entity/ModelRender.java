package org.craft.client.render.entity;

import java.nio.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.client.render.texture.Texture;
import org.craft.entity.*;
import org.craft.maths.*;
import org.lwjgl.*;

public class ModelRender extends AbstractRender
{

    protected        ModelBase                       model;
    private          HashMap<ModelBox, OpenGLBuffer> buffers;
    protected static Texture                         defaultTexture;

    public ModelRender(ModelBase model)
    {
        if(defaultTexture == null)
        {
            defaultTexture = new Texture(1, 1, (ByteBuffer) BufferUtils.createByteBuffer(4).put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255).flip());
        }
        this.model = model;
        buffers = Maps.newHashMap();
    }

    public Texture getTexture(Entity e)
    {
        return defaultTexture;
    }

    @Override
    public void render(RenderEngine engine, Entity e, float entX, float entY, float entZ)
    {
        Matrix4 tmpMatrix = Matrix4.get();
        for(ModelBox box : model.getChildren())
        {
            if(box == null)
            {
                continue;
            }
            if(!buffers.containsKey(box))
            {
                OpenGLBuffer buffer = new OpenGLBuffer();
                box.prepareBuffer(getTexture(e), buffer, getColor(e));
                buffers.put(box, buffer);
            }
            Matrix4 rot = box.getRotation().toRotationMatrix();
            Matrix4 scale = tmpMatrix.initScale(1, 1, 1);//box.getWidth(), box.getHeight(), box.getDepth());
            Matrix4 translation = Matrix4.get().initTranslation(-box.getX(), -box.getY(), -box.getZ()).mul(Matrix4.get().initTranslation(entX, entY, entZ));
            Quaternion erot = new Quaternion(Vector3.yAxis, e.getYaw());
            Matrix4 rot1 = erot.toRotationMatrix();

            Matrix4 finalMatrix = scale.mul(translation.mul(rot.mul(rot1)));
            engine.setModelviewMatrix(finalMatrix);
            engine.renderBuffer(buffers.get(box), getTexture(e));
        }
        engine.setModelviewMatrix(Matrix4.get().initIdentity());
    }

    public Quaternion getColor(Entity e)
    {
        return Quaternion.get(1, 1, 1, 1);
    }
}
