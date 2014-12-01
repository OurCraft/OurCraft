package org.craft.client.gui;

import org.craft.client.*;

public class ScreenTitle
{

    private String  mainTitle;
    private String  subtitle;
    private long    fadeOutDuration;
    private long    fadeInDuration;
    private long    displayTime;
    private boolean visible;
    private long    started;

    public ScreenTitle()
    {
        mainTitle = "";
        subtitle = "";
    }

    public String getRawMainTitle()
    {
        return mainTitle;
    }

    public String getRawSubTitle()
    {
        return subtitle;
    }

    public void setRawSubTitle(String title)
    {
        this.subtitle = title;
    }

    public void setRawMainTitle(String title)
    {
        this.mainTitle = title;
    }

    public long getDisplayTime()
    {
        return displayTime;
    }

    public long getFadeInDuration()
    {
        return fadeInDuration;
    }

    public long getFadeOutDuration()
    {
        return fadeOutDuration;
    }

    public void setFadeInDuration(long time)
    {
        fadeInDuration = time;
    }

    public void setFadeOutDuration(long time)
    {
        fadeOutDuration = time;
    }

    public void setDisplayDuration(long time)
    {
        displayTime = time;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
        if(visible)
            started = System.currentTimeMillis();
    }

    public boolean getVisible()
    {
        return visible;
    }

    public void show()
    {
        OurCraft.getOurCraft().setScreenTitle(this);
        started = System.currentTimeMillis();
    }

    public long timeSinceStart()
    {
        return System.currentTimeMillis() - started;
    }
}
