package org.craft.client.render;

import java.util.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.util.*;
import org.craft.world.*;

public class RenderBlocks
{

    private HashMap<ChunkCoord, OpenGLBuffer> chunkBuffers;
    private RenderEngine                      renderEngine;
    private int                               index;
    public static TextureMap                  blockMap;

    public RenderBlocks(RenderEngine engine)
    {
        this.renderEngine = engine;
        chunkBuffers = new HashMap<>();
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

    public void startRendering(OpenGLBuffer buffer)
    {
        index = 0;
        buffer.clear();
    }

    public void drawAllFaces(OpenGLBuffer buffer, Block block, World world, int x, int y, int z)
    {
        EnumSide side = EnumSide.NORTH;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawNorthFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.NORTH), x, y, z);
        }

        side = EnumSide.SOUTH;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawSouthFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.SOUTH), x, y, z);
        }

        side = EnumSide.WEST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawWestFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.WEST), x, y, z);
        }

        side = EnumSide.EAST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawEastFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.EAST), x, y, z);
        }

        side = EnumSide.TOP;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawTopFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.TOP), x, y, z);
        }

        side = EnumSide.BOTTOM;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side))
        {
            drawBottomFace(buffer, world, block, block.getBlockIcon(world, x, y, z, EnumSide.BOTTOM), x, y, z);
        }
    }

    public void drawNorthFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 6
        endDrawFace(buffer);
    }

    public void drawSouthFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 6
        endDrawFace(buffer);
    }

    public void drawWestFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace(buffer);
    }

    public void drawEastFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace(buffer);
    }

    public void drawTopFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace(buffer);
    }

    public void drawBottomFace(OpenGLBuffer buffer, World world, Block block, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()))); // 6
        endDrawFace(buffer);
    }

    private void endDrawFace(OpenGLBuffer buffer)
    {
        buffer.addIndex(0 + index);
        buffer.addIndex(1 + index);
        buffer.addIndex(2 + index);
        buffer.addIndex(1 + index);
        buffer.addIndex(2 + index);
        buffer.addIndex(3 + index);

        index += 4;
    }

    public void flush(OpenGLBuffer buffer)
    {
        buffer.upload();
    }

    public void render(World w, List<Chunk> visiblesChunks)
    {
        if(visiblesChunks.size() != 0)
        {
            for(Chunk c : visiblesChunks)
            {
                OpenGLBuffer buffer = chunkBuffers.get(c.getCoords());
                if(c.isDirty() || buffer == null)
                {
                    c.cleanUpDirtiness();
                    if(buffer == null)
                    {
                        chunkBuffers.put(c.getCoords(), new OpenGLBuffer());
                        buffer = chunkBuffers.get(c.getCoords());
                    }
                    startRendering(buffer);
                    for(int x = 0; x < 16; x++ )
                    {
                        for(int y = 0; y < 16; y++ )
                        {
                            for(int z = 0; z < 16; z++ )
                            {
                                Block b = c.getBlock(w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                drawAllFaces(buffer, b, w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                            }
                        }
                    }
                    flush(buffer);
                }
                renderEngine.renderBuffer(buffer, blockMap);
            }
        }
    }
}
