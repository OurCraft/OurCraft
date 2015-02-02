package org.craft.client.render.texture;

import org.craft.client.render.NullTextureIcon;

public interface TextureIcon
{
    public static final TextureIcon NULL_ICON = new NullTextureIcon();

    /**
     * Returns the width of this icon
     */
    public float getWidth();

    /**
     * Returns the height of this icon
     */
    public float getHeight();

    /**
     * Returns the min U coordinate of this icon
     */
    public float getMinU();

    /**
     * Returns the max U coordinate of this icon
     */
    public float getMaxU();

    /**
     * Returns the min V coordinate of this icon
     */
    public float getMinV();

    /**
     * Returns the max V coordinate of this icon
     */
    public float getMaxV();
}
