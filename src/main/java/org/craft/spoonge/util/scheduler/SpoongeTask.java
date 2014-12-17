package org.craft.spoonge.util.scheduler;

import java.util.*;

import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.scheduler.*;

public class SpoongeTask implements Task
{

    private Object    plugin;
    private Runnable  task;
    protected boolean cancelled;
    private boolean   done;
    private long      delay;
    private UUID      uuid;

    public SpoongeTask(Object plugin, Runnable task)
    {
        this(plugin, task, 0L);
    }

    public SpoongeTask(Object plugin, Runnable task, long delay)
    {
        this.plugin = plugin;
        this.task = task;
        this.delay = delay;

        uuid = UUID.randomUUID();
    }

    // TODO: @Override
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
        return null;
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

    @Override
    public Runnable getRunnable()
    {
        return task;
    }

    @Override
    public UUID getUniqueId()
    {
        return uuid;
    }

}
