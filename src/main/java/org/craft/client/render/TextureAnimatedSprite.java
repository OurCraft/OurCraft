package org.craft.client.render;

public class TextureAnimatedSprite extends TextureMapSprite
{

    private long    rate;
    private boolean activated;
    private long    counter;

    public TextureAnimatedSprite(long rate)
    {
        this.rate = rate;
    }

    public void tick()
    {
        if(activated)
        {
            counter++ ;
            if(counter >= rate)
            {
                ((AnimatedIcon) icon).incrementIndex();
                counter = 0;
            }
        }
        else
            counter = 0;
    }

    public void activate()
    {
        activated = true;
    }

    public void disactivate()
    {
        activated = false;
    }

}
