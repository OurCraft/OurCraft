package org.craft.spoonge.util.scheduler;

import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.scheduler.*;

public class SpoongeRepeatingTask extends SpoongeTask implements RepeatingTask
{

    private long interval;

    public SpoongeRepeatingTask(PluginContainer plugin, Runnable task, long delay, long interval)
    {
        super(plugin, task, delay);
        this.interval = interval;
    }

    @Override
    public long getInterval()
    {
        return interval;
    }

}
