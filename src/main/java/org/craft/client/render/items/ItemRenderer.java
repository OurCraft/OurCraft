package org.craft.client.render.items;

import org.craft.client.render.*;
import org.craft.inventory.*;
import org.craft.items.*;

public abstract class ItemRenderer
{

    /**
     * Renders given stackable at given coords
     */
    public abstract void renderItem(RenderEngine engine, OffsettedOpenGLBuffer buffer, IStackable item, Stack stack, float x, float y, float z);
}
