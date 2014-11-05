package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.title.*;

@BytecodeModifier("org.craft.client.gui.ScreenTitle")
public class SpoongeTitle implements Title
{
    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public String getMainTitle()
    {
        return null;
    }

    @Shadow
    public String getSubTitle()
    {
        return null;
    }

    @Shadow
    public void setSubTitle(String title)
    {
    }

    @Shadow
    public void setMainTitle(String title)
    {
    }

    @Shadow
    public long getFadeIn()
    {
        return 0;
    }

    @Shadow
    public long getFadeOut()
    {
        return 0;
    }

    @Shadow
    public void setDisplayDuration(long time)
    {
    }

    @Shadow
    public void setFadeInDuration(long time)
    {
    }

    @Shadow
    public void setFadeOutDuration(long time)
    {
    }

    @Shadow
    public void setVisible(boolean visible)
    {
    }

    @Shadow
    public boolean getVisible()
    {
        return false;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================
    @Override
    public Title title(String text)
    {
        setMainTitle(text);
        return this;
    }

    @Override
    public Title subTitle(String text)
    {
        setSubTitle(text);
        return this;
    }

    @Override
    public Title fadeIn(int ticks)
    {
        setFadeInDuration((long) (((float) ticks / 20f) * 1000L));
        return this;
    }

    @Override
    public Title stay(int ticks)
    {
        setDisplayDuration((long) (((float) ticks / 20f) * 1000L));
        return this;
    }

    @Override
    public Title fadeOut(int ticks)
    {
        setFadeOutDuration((long) (((float) ticks / 20f) * 1000L));
        return this;
    }

    @Override
    public Title clear()
    {
        setVisible(false);
        return this;
    }

    @Override
    public Title reset()
    {
        setVisible(true);
        return this;
    }

    @Override
    public Title send(Player player)
    {
        setVisible(true);
        return this;
    }

}
