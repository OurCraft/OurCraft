package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.world.*;

public abstract class AbstractBlockRenderer extends Renderer
{

    /**
     * Renders given block at given coords
     */
    public abstract void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z);

    /**
     * Returns true if block should be rendered in given pass
     */
    public abstract boolean shouldRenderInPass(EnumRenderPass pass, World w, Block b, int x, int y, int z);
}
