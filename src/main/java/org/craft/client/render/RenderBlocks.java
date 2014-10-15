package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.client.render.blocks.*;
import org.craft.resources.*;
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

    private HashMap<ChunkCoord, OffsettedOpenGLBuffer>             chunkBuffersPass0;
    private HashMap<ChunkCoord, OffsettedOpenGLBuffer>             chunkBuffersPass1;
    private RenderEngine                                           renderEngine;
    private HashMap<Class<? extends Block>, AbstractBlockRenderer> renderers;
    private AbstractBlockRenderer                                  fallbackRenderer;
    private Comparator<Chunk>                                      chunkComparator;
    private Comparator<BlockRenderInfos>                           blockComparator;
    public static ResourceLocation                                 blockMapLoc;

    public static void createBlockMap(RenderEngine engine)
    {
        TextureMap blockMap = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft/textures", "blocks"), true);
        for(Block b : Blocks.BLOCK_REGISTRY.values())
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
        blockMapLoc = new ResourceLocation("ourcraft", "textures/atlases/blocks.png");
        engine.registerLocation(blockMapLoc, blockMap);
    }

    public RenderBlocks(RenderEngine engine)
    {
        this.renderEngine = engine;
        chunkBuffersPass0 = new HashMap<ChunkCoord, OffsettedOpenGLBuffer>();
        chunkBuffersPass1 = new HashMap<ChunkCoord, OffsettedOpenGLBuffer>();
        if(blockMapLoc == null)
        {
            createBlockMap(engine);
        }
        fallbackRenderer = new FullCubeBlockRenderer();
        renderers = new HashMap<Class<? extends Block>, AbstractBlockRenderer>();
    }

    public void registerBlockRenderer(Class<? extends Block> blockClass, AbstractBlockRenderer renderer)
    {
        renderers.put(blockClass, renderer);
    }

    public AbstractBlockRenderer getRenderer(Block block)
    {
        Class<? extends Block> blockClass = block.getClass();
        if(renderers.containsKey(blockClass))
        {
            AbstractBlockRenderer renderer = renderers.get(blockClass);
            return renderer;
        }
        return fallbackRenderer;
    }

    /**
     * Clears the buffer and setup required informations in order to start rendering
     */
    public void startRendering(OffsettedOpenGLBuffer buffer)
    {
        buffer.setOffset(0);
        buffer.clear();
    }

    /**
     * Uploads buffer data to OpenGL
     */
    public void flush(OpenGLBuffer buffer)
    {
        buffer.upload();
        buffer.clearAndDisposeVertices();
    }

    /**
     * Renders visible chunks from World instance 'w'
     */
    public void render(World w, List<Chunk> visiblesChunks)
    {
        if(visiblesChunks.size() != 0)
        {
            if(chunkComparator == null)
                chunkComparator = new Comparator<Chunk>()
                {

                    @Override
                    public int compare(Chunk a, Chunk b)
                    {
                        float adx = a.getCoords().x * 16 - renderEngine.getRenderViewEntity().posX;
                        float ady = a.getCoords().y * 16 - renderEngine.getRenderViewEntity().posY;
                        float adz = a.getCoords().z * 16 - renderEngine.getRenderViewEntity().posZ;
                        float adist = (float) Math.sqrt(adx * adx + ady * ady + adz * adz);

                        float bdx = b.getCoords().x * 16 - renderEngine.getRenderViewEntity().posX;
                        float bdy = b.getCoords().y * 16 - renderEngine.getRenderViewEntity().posY;
                        float bdz = b.getCoords().z * 16 - renderEngine.getRenderViewEntity().posZ;
                        float bdist = (float) Math.sqrt(bdx * bdx + bdy * bdy + bdz * bdz);
                        return Float.compare(bdist, adist);
                    }
                };
            Collections.sort(visiblesChunks, chunkComparator);
            for(int passId = 0; passId < 2; passId++ )
            {
                EnumRenderPass currentPass = EnumRenderPass.getFromId(passId);
                if(currentPass == EnumRenderPass.ALPHA)
                {
                    glDepthMask(false);
                    glDepthFunc(GL_LESS);
                }
                for(Chunk c : visiblesChunks)
                {
                    OffsettedOpenGLBuffer buffer = null;
                    HashMap<ChunkCoord, OffsettedOpenGLBuffer> map = null;
                    if(currentPass == EnumRenderPass.NORMAL)
                    {
                        map = chunkBuffersPass0;
                    }
                    else if(currentPass == EnumRenderPass.ALPHA)
                    {
                        map = chunkBuffersPass1;
                    }
                    buffer = map.get(c.getCoords());
                    if(buffer == null)
                    {
                        map.put(c.getCoords(), new OffsettedOpenGLBuffer());
                        buffer = map.get(c.getCoords());
                    }
                    if(c.isDirty() || buffer == null)
                    {
                        startRendering(buffer);
                        if(currentPass == EnumRenderPass.NORMAL)
                        {
                            for(int x = 0; x < 16; x++ )
                            {
                                for(int y = 0; y < 16; y++ )
                                {
                                    for(int z = 0; z < 16; z++ )
                                    {
                                        Block b = c.getBlock(w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        if(b != null && b.shouldRender() && b.shouldRenderInPass(currentPass))
                                        {
                                            getRenderer(b).render(renderEngine, buffer, w, b, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            ArrayList<BlockRenderInfos> infosList = new ArrayList<RenderBlocks.BlockRenderInfos>();
                            for(int x = 0; x < 16; x++ )
                            {
                                for(int y = 0; y < 16; y++ )
                                {
                                    for(int z = 0; z < 16; z++ )
                                    {
                                        Block b = c.getBlock(w, x + c.getCoords().x * 16, y + c.getCoords().y * 16, z + c.getCoords().z * 16);
                                        if(b != null && b.shouldRender() && b.shouldRenderInPass(currentPass))
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
                            if(blockComparator == null)
                                blockComparator = new Comparator<BlockRenderInfos>()
                                {

                                    @Override
                                    public int compare(BlockRenderInfos a, BlockRenderInfos b)
                                    {
                                        float adx = a.x - renderEngine.getRenderViewEntity().posX;
                                        float ady = a.y - renderEngine.getRenderViewEntity().posY;
                                        float adz = a.z - renderEngine.getRenderViewEntity().posZ;
                                        float adist = (float) Math.sqrt(adx * adx + ady * ady + adz * adz);

                                        float bdx = b.x - renderEngine.getRenderViewEntity().posX;
                                        float bdy = b.y - renderEngine.getRenderViewEntity().posY;
                                        float bdz = b.z - renderEngine.getRenderViewEntity().posZ;
                                        float bdist = (float) Math.sqrt(bdx * bdx + bdy * bdy + bdz * bdz);
                                        return Float.compare(bdist, adist);
                                    }
                                };
                            Collections.sort(infosList, blockComparator);
                            for(BlockRenderInfos infos : infosList)
                            {
                                getRenderer(infos.block).render(renderEngine, buffer, w, infos.block, infos.x, infos.y, infos.z);
                            }
                        }
                        flush(buffer);

                        if(currentPass == EnumRenderPass.ALPHA)
                            c.cleanUpDirtiness();
                    }
                    renderEngine.bindLocation(blockMapLoc);
                    renderEngine.renderBuffer(buffer);
                }
                if(currentPass == EnumRenderPass.ALPHA)
                {
                    glDepthFunc(GL_LESS);
                    glDepthMask(true);
                }
            }
        }
    }
}
