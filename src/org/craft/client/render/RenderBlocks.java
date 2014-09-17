package org.craft.client.render;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.resources.*;
import org.craft.util.*;
import org.craft.world.*;
import org.lwjgl.util.vector.*;

public class RenderBlocks
{

    private OpenGLBuffer     buffer;
    private RenderEngine     renderEngine;
    private int              index;
    public static TextureMap blockMap;

    public RenderBlocks(RenderEngine engine)
    {
        this.renderEngine = engine;
        buffer = new OpenGLBuffer();
        if(blockMap == null)
        {
            blockMap = new TextureMap(OurCraft.getOurCraft().getBaseLoader(), new ResourceLocation("assets/textures", "blocks"), true);
            for(Block b : Block.registry.values())
            {
                b.registerIcons(blockMap);
            }
            try
            {
                blockMap.compile();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void startRendering()
    {
        buffer.clear();
    }

    public void drawAllFaces(Block block, World world, int x, int y, int z)
    {
        drawNorthFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.NORTH), x, y, z);
        drawSouthFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.SOUTH), x, y, z);
        drawWestFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.WEST), x, y, z);
        drawEastFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.EAST), x, y, z);
        drawTopFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.TOP), x, y, z);
        drawBottomFace(world, block, block.getBlockIcon(world, x, y, z, EnumSide.BOTTOM), x, y, z);
    }

    public void drawNorthFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 6
        endDrawFace();
    }

    public void drawSouthFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 6
        endDrawFace();
    }

    public void drawWestFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace();
    }

    public void drawEastFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace();
    }

    public void drawTopFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 1 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 1 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace();
    }

    public void drawBottomFace(World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(new Vector3f(0 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 1 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(new Vector3f(1 + x, 0 + y, 0 + z), new Vector2f((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace();
    }

    private void endDrawFace()
    {
        buffer.addIndex(0 + index);
        buffer.addIndex(1 + index);
        buffer.addIndex(2 + index);
        buffer.addIndex(1 + index);
        buffer.addIndex(2 + index);
        buffer.addIndex(3 + index);

        index += 4;
    }

    public void flush()
    {
        buffer.upload();
    }

    public void render(Texture test)
    {
        renderEngine.renderBuffer(buffer, blockMap);
    }
}
