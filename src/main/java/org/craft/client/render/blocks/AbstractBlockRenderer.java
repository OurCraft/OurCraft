package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.world.*;

public abstract class AbstractBlockRenderer
{

    private static final TextureIcon nullIcon = new NullTextureIcon();

    /**
     * Renders given block at given coords
     */
    public abstract void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z);

    public void renderFace(OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z, TextureIcon icon, Vector3 startPos, Vector3 size)
    {
        renderFace(buffer, w, b, x, y, z, icon, startPos, size, false);
    }

    public void renderFace(OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z, TextureIcon icon, Vector3 startPos, Vector3 size, boolean flipZ)
    {
        Vector2 maxUV = Vector2.get(1, 1);
        renderFace(buffer, w, b, x, y, z, icon, startPos, size, flipZ, Vector2.NULL, maxUV);
        maxUV.dispose();
    }

    public void renderFace(OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z, TextureIcon icon, Vector3 startPos, Vector3 size, boolean flipZ, Vector2 minUV, Vector2 maxUV)
    {
        renderFace(buffer, w, b, x, y, z, icon, startPos, size, flipZ, minUV, maxUV, Vector3.NULL, Quaternion.NULL);
    }

    public void renderFace(OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z, TextureIcon icon, Vector3 startPos, Vector3 size, boolean flipV, Vector2 minUV, Vector2 maxUV, Vector3 rotationOrigin, Quaternion rotation)
    {
        Chunk chunk = w.getChunk(x, y, z);
        if(chunk == null)
            return;
        float lightValue = chunk.getLightValue(w, x, y, z);
        renderFace(lightValue, buffer, w, b, x, y, z, icon, startPos, size, flipV, minUV, maxUV, rotationOrigin, rotation);
    }

    public void renderFace(float lightValue, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z, TextureIcon icon, Vector3 startPos, Vector3 size, boolean flipV, Vector2 minUV, Vector2 maxUV, Vector3 rotationOrigin, Quaternion rotation)
    {
        float startX = startPos.getX();
        float startY = startPos.getY();
        float startZ = startPos.getZ();

        float width = size.getX();
        float height = size.getY();
        float depth = size.getZ();
        if(icon == null)
            icon = nullIcon;
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
            buffer.addVertex(Vertex.get(p0.add(x, y, z), Vector2.get(minU, minV), Vector3.get(lightValue, lightValue, lightValue))); // 2
            buffer.addVertex(Vertex.get(p1.add(x, y, z), Vector2.get(minU, maxV), Vector3.get(lightValue, lightValue, lightValue))); // 0
            buffer.addVertex(Vertex.get(p2.add(x, y, z), Vector2.get(maxU, maxV), Vector3.get(lightValue, lightValue, lightValue))); // 4
            buffer.addVertex(Vertex.get(p3.add(x, y, z), Vector2.get(maxU, minV), Vector3.get(lightValue, lightValue, lightValue))); // 6
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
                quaternionAsMatrix.dispose();
                initialTranslation.dispose();
                initialTranslation2.dispose();
                rotationMatrix.dispose();
                finalTransformation.dispose();
            }
            buffer.addVertex(Vertex.get(p0.add(x, y, z), Vector2.get(minU, maxV), Vector3.get(lightValue, lightValue, lightValue))); // 0
            buffer.addVertex(Vertex.get(p1.add(x, y, z), Vector2.get(minU, minV), Vector3.get(lightValue, lightValue, lightValue))); // 2
            buffer.addVertex(Vertex.get(p2.add(x, y, z), Vector2.get(maxU, minV), Vector3.get(lightValue, lightValue, lightValue))); // 4
            buffer.addVertex(Vertex.get(p3.add(x, y, z), Vector2.get(maxU, maxV), Vector3.get(lightValue, lightValue, lightValue))); // 6
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
