package org.craft.client.render.entity;

import java.util.*;

import com.google.common.collect.*;

import org.craft.client.render.*;
import org.craft.entity.*;

public abstract class AbstractRender
{

    /**
     * Renders given entity at given coords (usually entity's position)
     */
    public abstract void render(RenderEngine engine, Entity e, float entX, float entY, float entZ);

    private final static HashMap<Class<? extends Entity>, AbstractRender> map = Maps.newHashMap();

    public static void registerVanillaRenderers()
    {
        registerRenderer(EntityPrimedTNT.class, new RenderPrimedTNT());
    }

    public static void registerRenderer(Class<? extends Entity> clazz, AbstractRender render)
    {
        map.put(clazz, render);
    }

    public static AbstractRender getRenderer(Entity e)
    {
        return getRenderer(e.getClass());
    }

    public static AbstractRender getRenderer(Class<? extends Entity> clazz)
    {
        return (AbstractRender) map.get(clazz);
    }
}
