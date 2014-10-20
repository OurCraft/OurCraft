package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class FullCubeBlockRenderer extends AbstractBlockRenderer
{

    @Override
    /**
     * Draws a full cube from the given block
     */
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World world, Block block, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        EnumSide side = EnumSide.NORTH;
        Chunk chunk = world.getChunk(x, y, z);
        if(chunk == null)
            return;
        float lightValue = chunk.getLightValue(world, x, y, z);
        TextureIcon icon = ((TextureMap) engine.getByLocation(RenderBlocks.blockMapLoc)).get("bedrock.png");
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawNorthFace(buffer, world, block, lightValue, icon, x, y, z);
        }

        side = EnumSide.SOUTH;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawSouthFace(buffer, world, block, lightValue, icon, x, y, z);
        }

        side = EnumSide.WEST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawWestFace(buffer, world, block, lightValue, icon, x, y, z);
        }

        side = EnumSide.EAST;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawEastFace(buffer, world, block, lightValue, icon, x, y, z);
        }

        side = EnumSide.TOP;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawTopFace(buffer, world, block, lightValue, icon, x, y, z);
        }

        side = EnumSide.BOTTOM;
        if(!world.getBlockNextTo(x, y, z, side).isSideOpaque(world, x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), side) && block.shouldSideBeRendered(world, x, y, z, side))
        {
            drawBottomFace(buffer, world, block, lightValue, icon, x, y, z);
        }

    }

    public void drawNorthFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 sizeVec = Vector3.get(1, 1, 0);
        renderFace(buffer, world, block, x, y, z, icon, Vector3.NULL, sizeVec);
        sizeVec.dispose();
    }

    public void drawSouthFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 startPos = Vector3.get(0, 0, 1);
        Vector3 sizeVec = Vector3.get(1, 1, 0);
        renderFace(buffer, world, block, x, y, z, icon, startPos, sizeVec);
        sizeVec.dispose();
        startPos.dispose();
    }

    public void drawWestFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 startPos = Vector3.NULL;
        Vector3 sizeVec = Vector3.get(0, 1, 1);
        renderFace(buffer, world, block, x, y, z, icon, startPos, sizeVec);
        sizeVec.dispose();
    }

    public void drawEastFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 startPos = Vector3.get(1, 0, 0);
        Vector3 sizeVec = Vector3.get(0, 1, 1);
        renderFace(buffer, world, block, x, y, z, icon, startPos, sizeVec);
        sizeVec.dispose();
        startPos.dispose();
    }

    public void drawTopFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 startPos = Vector3.get(0, 1f, 0);
        Vector3 sizeVec = Vector3.get(1, 0, 1);
        renderFace(buffer, world, block, x, y, z, icon, startPos, sizeVec, true);
        sizeVec.dispose();
        startPos.dispose();
    }

    public void drawBottomFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        Vector3 startPos = Vector3.get(0, 0, 0);
        Vector3 sizeVec = Vector3.get(1, 0, 1);
        renderFace(buffer, world, block, x, y, z, icon, startPos, sizeVec, true);
        sizeVec.dispose();
        startPos.dispose();
    }

    @Override
    public boolean shouldRenderInPass(EnumRenderPass currentPass, World w, Block b, int x, int y, int z)
    {
        return b.shouldRenderInPass(currentPass);
    }

}
