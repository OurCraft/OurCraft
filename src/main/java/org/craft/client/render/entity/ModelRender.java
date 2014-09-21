package org.craft.client.render.entity;

import java.nio.*;

import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.lwjgl.*;

public class ModelRender<T extends Entity> extends AbstractRender<T>
{

    private ModelBase      model;
    private OpenGLBuffer   buffer;
    private static Texture defaultTexture;

    public ModelRender(ModelBase model)
    {
        if(defaultTexture == null)
        {
            defaultTexture = new Texture(1, 1, (ByteBuffer) BufferUtils.createByteBuffer(4).put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255).flip());
        }
        this.model = model;
        buffer = new OpenGLBuffer();
        buffer.setToCube();
    }

    public Texture getTexture(Entity e)
    {
        return defaultTexture;
    }

    @Override
    public void render(RenderEngine engine, T e, float entX, float entY, float entZ)
    {
        Matrix4 tmpMatrix = Matrix4.TMP;
        for(ModelBox box : model.getChildren())
        {
            if(box == null)
                continue;
            Matrix4 rot = (tmpMatrix.initTranslation(box.getRotationPointX(), box.getRotationPointY(), box.getRotationPointZ()).mul(box.getRotation().toRotationMatrix()));
            Matrix4 scale = tmpMatrix.initScale(box.getWidth(), box.getHeight(), box.getDepth());
            Matrix4 translation = new Matrix4().initTranslation(box.getX(), box.getY(), box.getZ()).mul(new Matrix4().initTranslation(entX, entY, entZ));
            Quaternion erot = e.getRotation().copy();
            erot = new Quaternion(Vector3.yAxis, (float) -Math.acos(erot.getForward().dot(Vector3.xAxis))).mul(erot).normalize();
            Matrix4 rot1 = erot.toRotationMatrix();

            Matrix4 finalMatrix = scale.mul(rot.mul(translation.mul(rot1)));
            Shader.getCurrentlyBound().setUniform("modelview", finalMatrix);
            engine.renderBuffer(buffer, getTexture(e));
        }
        Shader.getCurrentlyBound().setUniform("modelview", new Matrix4().initIdentity());
    }
}
