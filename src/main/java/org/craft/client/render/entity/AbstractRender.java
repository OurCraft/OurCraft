package org.craft.client.render.entity;

import org.craft.client.render.*;
import org.craft.entity.*;

public abstract class AbstractRender<T extends Entity>
{

    /**
     * Renders given entity at given coords (usually entity's position)
     */
    public abstract void render(RenderEngine engine, T e, float entX, float entY, float entZ);
}
