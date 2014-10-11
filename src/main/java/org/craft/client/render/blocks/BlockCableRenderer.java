package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockCableRenderer extends AbstractBlockRenderer
{

    @Override
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z)
    {
        Vector3 startPos = Vector3.get(0, 2f / 16f, 0);
        Vector3 size = Vector3.get(1, 0, 1);
        renderFace(buffer, w, b, x, y, z, b.getBlockIcon(w, x, y, z, EnumSide.TOP), startPos, size, true);
        size.dispose();
        startPos.dispose();

    }

}
