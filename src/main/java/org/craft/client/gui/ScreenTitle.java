package org.craft.client.gui;

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

    public String getMainTitle()
    {
        return mainTitle;
    }

    public String getSubTitle()
    {
        return subtitle;
    }

    public void setSubTitle(String title)
    {
        this.subtitle = title;
    }

    public void setMainTitle(String title)
    {
        this.mainTitle = title;
    }

    public long getDisplayTime()
    {
        return displayTime;
    }

    public long getFadeIn()
    {
        return fadeInDuration;
    }

    public long getFadeOut()
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
        started = System.currentTimeMillis();
    }

    public long timeSinceStart()
    {
        return System.currentTimeMillis() - started;
    }
}
