package org.craft.spoonge.util.title;

import org.craft.client.gui.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.title.*;

public class SpoongeTitleBuilder implements TitleBuilder
{

    private long    displayDuration;
    private long    fadeInDuration;
    private long    fadeOutDuration;
    private boolean visible;
    private Message title;
    private Message subtitle;

    public SpoongeTitleBuilder()
    {
        visible = true;
    }

    @Override
    public TitleBuilder title(Message message)
    {
        this.title = message;
        return this;
    }

    @Override
    public TitleBuilder subtitle(Message message)
    {
        this.subtitle = message;
        return this;
    }

    @Override
    public TitleBuilder fadeIn(int ticks)
    {
        fadeInDuration = ticks;
        return this;
    }

    @Override
    public TitleBuilder stay(int ticks)
    {
        displayDuration = ticks;
        return this;
    }

    @Override
    public TitleBuilder fadeOut(int ticks)
    {
        fadeOutDuration = ticks;
        return this;
    }

    @Override
    public TitleBuilder clear()
    {
        visible = false;
        return this;
    }

    @Override
    public TitleBuilder reset()
    {
        visible = false;
        displayDuration = 0;
        fadeInDuration = 0;
        fadeOutDuration = 0;
        return this;
    }

    @Override
    public Title build()
    {
        ScreenTitle title = new ScreenTitle();
        title.setDisplayDuration(displayDuration);
        title.setFadeInDuration(fadeInDuration);
        title.setFadeOutDuration(fadeOutDuration);
        title.setVisible(visible);
        title.setRawMainTitle(this.title.getContent().toString());
        title.setRawSubTitle(this.subtitle.getContent().toString());
        title.show();
        return (Title) title;
    }

}
