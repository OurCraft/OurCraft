package org.craft.client.models;

import org.craft.client.render.*;
import org.craft.maths.*;

public class ModelBox
{

    private float      x;
    private float      y;
    private float      z;
    private float      width;
    private float      height;
    private float      depth;
    private float      rotX;
    private float      rotY;
    private float      rotZ;
    private Quaternion rotation;
    private float      ratio;
    private float      uCoord;
    private float      vCoord;
    private String     name;
    private float      textW;
    private float      textH;

    public ModelBox(float x, float y, float z, float width, float height, float depth)
    {
        textW = -1f;
        textH = -1f;
        this.rotation = new Quaternion();
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setUVCoords(float u, float v)
    {
        this.uCoord = u;
        this.vCoord = v;
    }

    public void setRotation(Quaternion rot)
    {
        this.rotation = rot;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    /**
     * Sets a rotation point relative to the coordinates
     */
    public void setRotationPoint(float rx, float ry, float rz)
    {
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getRotationPointX()
    {
        return rotX;
    }

    public float getRotationPointY()
    {
        return rotY;
    }

    public float getRotationPointZ()
    {
        return rotZ;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getDepth()
    {
        return depth;
    }

    public void setPixelRatio(float f)
    {
        this.ratio = f;
    }

    public float getPixelRatio()
    {
        return ratio;
    }

    public float getU()
    {
        return uCoord;
    }

    public float getV()
    {
        return vCoord;
    }

    public void prepareBuffer(Texture texture, OpenGLBuffer buffer, float alpha)
    {
        prepareBuffer(texture, buffer, Quaternion.get(1, 1, 1, alpha));
    }

    public void prepareBuffer(Texture texture, OpenGLBuffer buffer, Quaternion color)
    {
        int index = 0;
        if(textW == -1f)
        {
            textW = texture.getWidth();
        }
        if(textH == -1f)
        {
            textH = texture.getHeight();
        }
        float ratioX = ratio * ((float) texture.getWidth() / textW) / (float) texture.getWidth() * getWidth();
        float ratioY = ratio * ((float) texture.getHeight() / textH) / (float) texture.getHeight() * getHeight();
        float frontMinU = ratioX + uCoord;
        float frontMinV = ratioY + vCoord;
        float frontMaxU = ratioX * 2 + uCoord;
        float frontMaxV = ratioY * 2 + vCoord;

        float backMinU = ratioX * 2 + uCoord;
        float backMinV = ratioY + vCoord;
        float backMaxU = ratioX * 3 + uCoord;
        float backMaxV = ratioY * 2 + vCoord;

        float leftMinU = uCoord;
        float leftMinV = ratioY + vCoord;
        float leftMaxU = ratioX + uCoord;
        float leftMaxV = ratioY * 2 + vCoord;

        float rightMinU = ratioX * 3 + uCoord;
        float rightMinV = ratioY + vCoord;
        float rightMaxU = ratioX * 4 + uCoord;
        float rightMaxV = ratioY * 2 + vCoord;

        float topMinU = ratioX * 2 + uCoord;
        float topMinV = vCoord;
        float topMaxU = ratioX * 3 + uCoord;
        float topMaxV = ratioY + vCoord;

        float bottomMinU = ratioX + uCoord;
        float bottomMinV = vCoord;
        float bottomMaxU = ratioX * 2 + uCoord;
        float bottomMaxV = ratioY + vCoord;
        buffer.addVertex(Vertex.get(Vector3.get(x, y, z), Vector2.get(frontMinU, frontMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z), Vector2.get(frontMaxU, frontMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z), Vector2.get(frontMaxU, frontMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z), Vector2.get(frontMinU, frontMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth()), Vector2.get(backMaxU, backMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth()), Vector2.get(backMinU, backMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth()), Vector2.get(backMinU, frontMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth()), Vector2.get(backMaxU, backMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z), Vector2.get(leftMaxU, leftMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth()), Vector2.get(leftMinU, leftMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth()), Vector2.get(leftMinU, leftMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z), Vector2.get(leftMaxU, leftMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;

        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z), Vector2.get(rightMinU, leftMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth()), Vector2.get(rightMaxU, rightMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth()), Vector2.get(rightMaxU, rightMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z), Vector2.get(rightMinU, rightMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;

        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z), Vector2.get(topMinU, topMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z), Vector2.get(topMaxU, topMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth()), Vector2.get(topMaxU, topMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth()), Vector2.get(topMinU, topMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z), Vector2.get(bottomMinU, bottomMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z), Vector2.get(bottomMaxU, bottomMaxV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth()), Vector2.get(bottomMaxU, bottomMinV), color));
        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth()), Vector2.get(bottomMinU, bottomMinV), color));

        buffer.addIndex(index + 0);
        buffer.addIndex(index + 1);
        buffer.addIndex(index + 2);

        buffer.addIndex(index + 2);
        buffer.addIndex(index + 0);
        buffer.addIndex(index + 3);

        index += 4;
        buffer.upload();
        buffer.clearAndDisposeVertices();
    }

    public void prepareWireframeBuffer(OpenGLBuffer buffer)
    {
        buffer.addVertex(Vertex.get(Vector3.get(x, y, z)));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z)));

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth())));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth())));

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z)));
        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth())));

        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z)));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth())));

        buffer.addIndex(0);
        buffer.addIndex(1);

        buffer.addIndex(2);
        buffer.addIndex(3);

        buffer.addIndex(4);
        buffer.addIndex(5);

        buffer.addIndex(6);
        buffer.addIndex(7);

        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z)));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z)));

        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth())));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth())));

        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z)));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth())));

        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z)));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth())));

        buffer.addIndex(8);
        buffer.addIndex(9);

        buffer.addIndex(10);
        buffer.addIndex(11);

        buffer.addIndex(12);
        buffer.addIndex(13);

        buffer.addIndex(14);
        buffer.addIndex(15);

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z)));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z)));

        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z)));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z)));

        buffer.addVertex(Vertex.get(Vector3.get(x, y, z + getDepth())));
        buffer.addVertex(Vertex.get(Vector3.get(x, y + getHeight(), z + getDepth())));

        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y, z + getDepth())));
        buffer.addVertex(Vertex.get(Vector3.get(x + getWidth(), y + getHeight(), z + getDepth())));

        buffer.addIndex(16);
        buffer.addIndex(17);

        buffer.addIndex(18);
        buffer.addIndex(19);

        buffer.addIndex(20);
        buffer.addIndex(21);

        buffer.addIndex(22);
        buffer.addIndex(23);

        buffer.upload();
        buffer.clearAndDisposeVertices();
    }

    public void setWidth(float value)
    {
        this.width = value;
    }

    public void setHeight(float value)
    {
        this.height = value;
    }

    public void setDepth(float value)
    {
        this.depth = value;
    }

    public void setX(float value)
    {
        this.x = value;
    }

    public void setY(float value)
    {
        this.y = value;
    }

    public void setZ(float value)
    {
        this.z = value;
    }

    public void setTextureSize(float w, float h)
    {
        this.textW = w;
        this.textH = h;
    }

}
