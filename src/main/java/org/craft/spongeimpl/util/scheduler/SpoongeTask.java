package org.craft.spongeimpl.util.scheduler;

import org.spongepowered.api.plugin.*;
import org.spongepowered.api.util.scheduler.*;

public class SpoongeTask implements Task
{

    private PluginContainer plugin;
    private Runnable        task;
    protected boolean       cancelled;
    private boolean         done;
    private long            delay;

    public SpoongeTask(PluginContainer plugin, Runnable task)
    {
        this(plugin, task, 0L);
    }

    public SpoongeTask(PluginContainer plugin, Runnable task, long delay)
    {
        this.plugin = plugin;
        this.task = task;
        this.delay = delay;
    }

    @Override
    public void run()
    {
        task.run();
        done = true;
    }

    @Override
    public String getName()
    {
        return "Task " + task.hashCode();
    }

    @Override
    public PluginContainer getOwner()
    {
        return plugin;
    }

    @Override
    public long getDelay()
    {
        return delay;
    }

    @Override
    public boolean cancel()
    {
        cancelled = true;
        return cancelled && !done;
    }

}
