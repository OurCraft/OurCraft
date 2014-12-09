package org.craft.client;

import java.util.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.world.*;

public class ParticleRenderer implements IParticleHandler
{

    private Particle[]                   particles;
    private int                          max;
    private int                          index;
    private OpenGLBuffer[]               buffers;
    private TextureMap                   map;
    private HashMap<String, TextureIcon> particleIcons;

    public ParticleRenderer()
    {
        this(2000);
    }

    public ParticleRenderer(int max)
    {
        this.max = max;
        particleIcons = Maps.newHashMap();
        particles = new Particle[max];
        buffers = new OpenGLBuffer[max];
        map = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft", "textures/particles"));
        for(String particle : ParticleRegistry.getTypes())
        {
            particleIcons.put(particle, map.generateIcon(particle));
        }
        try
        {
            map.compile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void spawnParticle(Particle particle)
    {
        int index = getFreeIndex();
        particles[index] = particle;
        this.index = index;
    }

    private int getFreeIndex()
    {
        if(particles[index] != null)
        {
            for(int i = 0; i < max; i++ )
            {
                if(particles[i] == null)
                    return i;
            }
            return 0;
        }
        return index;
    }

    public void spawnParticle(String particleID, float x, float y, float z)
    {
        spawnParticle(particleID, x, y, z, 120);
    }

    public void spawnParticle(String particleID, float x, float y, float z, long life)
    {
        spawnParticle(new Particle(particleID, x, y, z, life));
    }

    public void renderAll(RenderEngine engine)
    {
        engine.bindTexture(map);
        for(int i = 0; i < max; i++ )
        {
            if(particles[i] != null)
            {
                if(buffers[i] == null)
                {
                    OpenGLBuffer buffer = new OpenGLBuffer();
                    TextureIcon icon = particleIcons.get(particles[i].getName());
                    float minU = icon.getMinU();
                    float maxU = icon.getMaxU();
                    float minV = icon.getMaxV();
                    float maxV = icon.getMinV();
                    Quaternion color = Quaternion.get(1, 1, 1, 1);
                    buffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(minU, minV), color));
                    buffer.addVertex(Vertex.get(Vector3.get(1, 0, 0), Vector2.get(maxU, minV), color));
                    buffer.addVertex(Vertex.get(Vector3.get(1, 1, 0), Vector2.get(maxU, maxV), color));
                    buffer.addVertex(Vertex.get(Vector3.get(0, 1, 0), Vector2.get(minU, maxV), color));
                    buffer.addIndex(1);
                    buffer.addIndex(2);
                    buffer.addIndex(0);

                    buffer.addIndex(2);
                    buffer.addIndex(0);
                    buffer.addIndex(3);
                    buffer.upload();
                    buffers[i] = buffer;
                }
                Matrix4 translation = Matrix4.get().initTranslation(particles[i].getX(), particles[i].getY(), particles[i].getZ());
                Matrix4 camRot = engine.getRenderViewEntity().getQuaternionRotation().toRotationMatrix();
                Matrix4 modelview = translation.mul(camRot);
                camRot.dispose();
                translation.dispose();
                engine.setModelviewMatrix(modelview);
                engine.renderBuffer(buffers[i]);
                //                modelview.dispose();
            }
        }
        engine.setModelviewMatrix(Matrix4.get().initIdentity());
    }

    public void updateAll()
    {
        for(int i = 0; i < max; i++ )
        {
            if(particles[i] != null)
            {
                particles[i].update();
                if(particles[i].shouldBeKilled())
                {
                    buffers[i].dispose();
                    buffers[i] = null;
                    particles[i] = null;
                    index = i;
                }
            }
            else
                index = i;
        }
    }
}
