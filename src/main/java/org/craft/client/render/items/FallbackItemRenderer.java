package org.craft.client.render.items;

import org.craft.client.render.*;
import org.craft.client.render.texture.TextureIcon;
import org.craft.inventory.*;
import org.craft.items.*;
import org.craft.maths.*;

public class FallbackItemRenderer extends ItemRenderer
{

    @Override
    public void renderItem(RenderEngine engine, OffsettedOpenGLBuffer buffer, IStackable item, Stack stack, float x, float y, float z)
    {
        TextureIcon icon = engine.blocksAndItemsMap.get("test.png");
        Vector3 a = Vector3.get(0, 0, 0);
        Vector3 b = Vector3.get(1, 0, 0);
        Vector3 c = Vector3.get(1, 1, 0);
        Vector3 d = Vector3.get(0, 1, 0);
        Vector3 color = Vector3.get(1, 1, 1);
        buffer.addVertex(Vertex.get(a.add(x, y, z), Vector2.get(icon.getMaxU(), icon.getMaxV()), color));
        buffer.addVertex(Vertex.get(b.add(x, y, z), Vector2.get(icon.getMinU(), icon.getMaxV()), color));
        buffer.addVertex(Vertex.get(c.add(x, y, z), Vector2.get(icon.getMinU(), icon.getMinV()), color));
        buffer.addVertex(Vertex.get(d.add(x, y, z), Vector2.get(icon.getMaxU(), icon.getMinV()), color));

        a.dispose();
        b.dispose();
        c.dispose();
        d.dispose();
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(0);
        buffer.addIndex(3);
        buffer.addIndex(2);
        buffer.setOffsetToEnd();
    }
}
