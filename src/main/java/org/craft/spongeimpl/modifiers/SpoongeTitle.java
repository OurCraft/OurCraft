package org.craft.spongeimpl.modifiers;

import com.google.common.base.*;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.title.*;

@BytecodeModifier("org.craft.client.gui.ScreenTitle")
public class SpoongeTitle implements Title
{
    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public void show()
    {
        ;
    }

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
    public long getFadeInDuration()
    {
        return 0;
    }

    @Shadow
    public long getFadeOutDuration()
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
    /*   @Override
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
           show();
           return this;
       }*/

    @Override
    public Optional<Message<?>> getTitle()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Message<?>> getSubtitle()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Integer> getFadeIn()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Integer> getStay()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Integer> getFadeOut()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isClear()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReset()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TitleBuilder builder()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
