package org.craft.spongeimpl.util.scheduler;

import java.util.concurrent.*;

import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.scheduler.*;

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

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        if(done)
            return false;
        cancelled = true;
        return true;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public boolean isDone()
    {
        return done;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Runnable getRunnable()
    {
        return task;
    }

}
