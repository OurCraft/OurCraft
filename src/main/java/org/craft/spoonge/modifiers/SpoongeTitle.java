package org.craft.spoonge.modifiers;

import com.google.common.base.*;

import org.craft.modding.modifiers.*;
import org.craft.spoonge.util.title.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.title.*;

@BytecodeModifier("org.craft.client.gui.ScreenTitle")
public class SpoongeTitle implements Title
{
    @Shadow
    public SpoongeTitle()
    {
        ;
    }

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public void show()
    {
        ;
    }

    @Shadow
    public String getRawMainTitle()
    {
        return null;
    }

    @Shadow
    public String getRawSubTitle()
    {
        return null;
    }

    @Shadow
    public void setRawSubTitle(String title)
    {
    }

    @Shadow
    public void setRawMainTitle(String title)
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
    public long getDisplayTime()
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
        //return Optional.of(title);
        return Optional.absent();
    }

    @Override
    public Optional<Message<?>> getSubtitle()
    {
        // return Optional.of(subtitle);
        return Optional.absent();
    }

    @Override
    public Optional<Integer> getFadeIn()
    {
        return Optional.of((int) getFadeInDuration());
    }

    @Override
    public Optional<Integer> getStay()
    {
        return Optional.of((int) getDisplayTime());
    }

    @Override
    public Optional<Integer> getFadeOut()
    {
        return Optional.of((int) getFadeOutDuration());
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
        return new SpoongeTitleBuilder();
    }

}
