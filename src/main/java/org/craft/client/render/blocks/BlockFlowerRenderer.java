package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockFlowerRenderer extends AbstractBlockRenderer
{

    @Override
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z)
    {
        if(!b.shouldRender())
            return;
        Chunk chunk = w.getChunk(x, y, z);
        if(chunk == null)
            return;
        TextureIcon icon = b.getBlockIcon(w, x, y, z, EnumSide.UNDEFINED);
        Vector3 sizeVec = Vector3.get(1, 1, 1);
        renderFace(buffer, w, b, x, y, z, icon, Vector3.NULL, sizeVec);
        sizeVec.dispose();

        Vector3 posVec = Vector3.get(0, 0, 1);
        sizeVec = Vector3.get(1, 1, -1);
        renderFace(buffer, w, b, x, y, z, icon, posVec, sizeVec);
        sizeVec.dispose();
        posVec.dispose();
    }
}
