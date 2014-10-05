package org.craft.client.render.blocks;

import org.craft.blocks.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockFlowerRenderer implements IBlockRenderer
{

    @Override
    public void render(RenderEngine engine, OffsettedOpenGLBuffer buffer, World w, Block b, int x, int y, int z)
    {
        if(w.getChunk(x, y, z) == null)
            return;
        TextureIcon icon = b.getBlockIcon(w, x, y, z, EnumSide.UNDEFINED);
        float lightValue = w.getChunk(x, y, z).getLightValue(w, x, y, z);
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 0 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);
        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
        buffer.setOffsetToEnd();

        buffer.addVertex(new Vertex(Vector3.get(0 + x, 0 + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 0
        buffer.addVertex(new Vertex(Vector3.get(0 + x, 1 + y, 1 + z), Vector2.get((float) icon.getMinU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 2
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 1 + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMinV()), Vector3.get(lightValue, lightValue, lightValue))); // 4
        buffer.addVertex(new Vertex(Vector3.get(1 + x, 0 + y, 0 + z), Vector2.get((float) icon.getMaxU(), (float) icon.getMaxV()), Vector3.get(lightValue, lightValue, lightValue))); // 6
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);
        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
        buffer.setOffsetToEnd();
    }
}
