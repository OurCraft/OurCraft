package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.world.*;

public interface IBlockRenderer
{

    /**
     * Renders given block at given coords
     */
    public abstract void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z);
}
