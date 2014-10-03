package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockHalfSlabRenderer implements IBlockRenderer
{

    @Override
    /**
     * Draws a full cube from the given block
     */
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World world, Block block, int x, int y, int z)
    {
        EnumSide side = EnumSide.NORTH;
        Chunk chunk = world.getChunk(x, y, z);
        if(chunk == null)
            return;
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

    public void drawNorthFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawSouthFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawWestFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawEastFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) (icon.getMaxV() + icon.getMinV()) / 2), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawTopFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0.5f + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    public void drawBottomFace(OffsettedOpenGLBuffer buffer, World world, Block block, float lightValue, TextureIcon icon, int x, int y, int z)
    {
        if(!block.shouldRender())
            return;
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        endDrawFace(buffer);
    }

    private void endDrawFace(OffsettedOpenGLBuffer buffer)
    {
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);
        buffer.addIndex(1);
        buffer.addIndex(2);
        buffer.addIndex(3);

        buffer.setOffsetToEnd();
    }
}
