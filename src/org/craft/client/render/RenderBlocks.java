package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;

public class RenderBlocks
{

    public static class BlockRenderInfos
    {

        public Block block;
        public int   x;
        public int   y;
        public int   z;
    }

    private HashMap<ChunkCoord, OpenGLBuffer> chunkBuffersPass0;
    private HashMap<ChunkCoord, OpenGLBuffer> chunkBuffersPass1;
    private RenderEngine                      renderEngine;
    private int                               index;
    public static TextureMap                  blockMap;

    public RenderBlocks(RenderEngine engine)
    {
        this.renderEngine = engine;
        chunkBuffersPass0 = new HashMap<>();
        chunkBuffersPass1 = new HashMap<>();
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
        Chunk chunk = world.getChunk(x, y, z);
        if(chunk == null) return;
        float lightValue = chunk.getLightValue(world, x, y, z);
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawNorthFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.NORTH), x, y, z);
        }

        side = EnumSide.SOUTH;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawSouthFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.SOUTH), x, y, z);
        }

        side = EnumSide.WEST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawWestFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.WEST), x, y, z);
        }

        side = EnumSide.EAST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawEastFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.EAST), x, y, z);
        }

        side = EnumSide.TOP;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawTopFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.TOP), x, y, z);
        }

        side = EnumSide.BOTTOM;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawBottomFace(buffer, world, block, lightValue, block.getBlockIcon(world, x, y, z, EnumSide.BOTTOM), x, y, z);
        }
    }

    public void drawNorthFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawSouthFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawWestFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawEastFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawTopFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawBottomFace(OpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender()) return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), new Vector2((float)icon.getMinU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), new Vector2((float)icon.getMinU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), new Vector2((float)icon.getMaxU(), (float)icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
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
            Collections.sort(visiblesChunks, new Comparator<Chunk>()
            {

                @Override
                public int compare(Chunk a, Chunk b)
                {
                    float adx = a.getCoords().x * 16 - renderEngine.getRenderViewEntity().getPos().x;
                    float ady = a.getCoords().y * 16 - renderEngine.getRenderViewEntity().getPos().x;
                    float adz = a.getCoords().z * 16 - renderEngine.getRenderViewEntity().getPos().z;
                    float adist = (float)Math.sqrt(adx * adx + ady * ady + adz * adz);

                    float bdx = b.getCoords().x * 16 - renderEngine.getRenderViewEntity().getPos().x;
                    float bdy = b.getCoords().y * 16 - renderEngine.getRenderViewEntity().getPos().x;
                    float bdz = b.getCoords().z * 16 - renderEngine.getRenderViewEntity().getPos().z;
                    float bdist = (float)Math.sqrt(bdx * bdx + bdy * bdy + bdz * bdz);
                    return Float.compare(bdist, adist);
                }
            });
            for(int pass = 0; pass < 2; pass++ )
            {
                if(pass == 1)
                {
                    glDepthMask(false);
                }
                for(Chunk c : visiblesChunks)
                {
                    OpenGLBuffer buffer = null;
                    if(pass == 0)
                    {
                        buffer = chunkBuffersPass0.get(c.getCoords());
                        if(buffer == null)
                        {
                            chunkBuffersPass0.put(c.getCoords(), new OpenGLBuffer());
                            buffer = chunkBuffersPass0.get(c.getCoords());
                        }
                    }
                    if(pass == 1)
                    {
                        buffer = chunkBuffersPass1.get(c.getCoords());
                        if(buffer == null)
                        {
                            chunkBuffersPass1.put(c.getCoords(), new OpenGLBuffer());
                            buffer = chunkBuffersPass1.get(c.getCoords());
                        }
                    }
                    if(c.isDirty() || buffer == null)
                    {

                        startRendering(buffer);
                        if(pass == 0)
                        {
                            for(int x = 0; x < 16; x++ )
                            {
                                for(int y = 0; y < 16; y++ )
                                {
                                    for(int z = 0; z < 16; z++ )
                                    {
                                        Block b = c.getBlock(w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        if(b != null && b.shouldRenderInPass(pass))
                                        {
                                            drawAllFaces(buffer, b, w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            buffer = chunkBuffersPass1.get(c.getCoords());
                            if(buffer == null)
                            {
                                chunkBuffersPass1.put(c.getCoords(), new OpenGLBuffer());
                                buffer = chunkBuffersPass1.get(c.getCoords());
                            }
                            startRendering(buffer);

                            ArrayList<BlockRenderInfos> infosList = new ArrayList<>();
                            for(int x = 0; x < 16; x++ )
                            {
                                for(int y = 0; y < 16; y++ )
                                {
                                    for(int z = 0; z < 16; z++ )
                                    {
                                        Block b = c.getBlock(w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        if(b != null && b.shouldRenderInPass(pass))
                                        {
                                            BlockRenderInfos infos = new BlockRenderInfos();
                                            infos.block = b;
                                            infos.x = x + c.getCoords().x * 16;
                                            infos.y = y + c.getCoords().y * 16;
                                            infos.z = z + c.getCoords().z * 16;
                                            infosList.add(infos);
                                        }
                                    }
                                }
                            }
                            Collections.sort(infosList, new Comparator<BlockRenderInfos>()
                            {

                                @Override
                                public int compare(BlockRenderInfos a, BlockRenderInfos b)
                                {
                                    float adx = a.x - renderEngine.getRenderViewEntity().getPos().x;
                                    float ady = a.y - renderEngine.getRenderViewEntity().getPos().x;
                                    float adz = a.z - renderEngine.getRenderViewEntity().getPos().z;
                                    float adist = (float)Math.sqrt(adx * adx + ady * ady + adz * adz);

                                    float bdx = b.x - renderEngine.getRenderViewEntity().getPos().x;
                                    float bdy = b.y - renderEngine.getRenderViewEntity().getPos().x;
                                    float bdz = b.z - renderEngine.getRenderViewEntity().getPos().z;
                                    float bdist = (float)Math.sqrt(bdx * bdx + bdy * bdy + bdz * bdz);
                                    return Float.compare(bdist, adist);
                                }
                            });
                            for(BlockRenderInfos infos : infosList)
                            {
                                drawAllFaces(buffer, infos.block, w, infos.x, infos.y, infos.z);
                            }
                        }
                        flush(buffer);

                        if(pass == 1) c.cleanUpDirtiness();
                    }
                    renderEngine.renderBuffer(buffer, blockMap);
                }
                if(pass == 1)
                {
                    glDepthMask(true);
                }
            }
        }
    }
}
