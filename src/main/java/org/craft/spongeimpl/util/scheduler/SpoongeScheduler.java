package org.craft.spongeimpl.util.scheduler;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.plugin.*;
import org.spongepowered.api.util.scheduler.*;

public class SpoongeScheduler implements Scheduler
{

    private HashMap<PluginContainer, ArrayList<Task>> scheduled;

    public SpoongeScheduler()
    {
        this.scheduled = Maps.newHashMap();
    }

    @Override
    public Optional<Task> runTask(PluginContainer plugin, Runnable task)
    {
        Task spoongeTask = new SpoongeTask(plugin, task);
        spoongeTask.run();
        return Optional.of(spoongeTask);
    }

    @Override
    public Optional<Task> runTaskAfter(PluginContainer plugin, Runnable task, long delay)
    {
        Task spoongeTask = new SpoongeTask(plugin, task, delay);
        addScheduled(plugin, spoongeTask);
        return Optional.of(spoongeTask);
    }

    @Override
    public Optional<RepeatingTask> runRepeatingTask(PluginContainer plugin, Runnable task, long interval)
    {
        RepeatingTask spoongeTask = new SpoongeRepeatingTask(plugin, task, 0L, interval);
        spoongeTask.run();
        addScheduled(plugin, spoongeTask);
        return Optional.of(spoongeTask);
    }

    @Override
    public Optional<RepeatingTask> runRepeatingTaskAfter(PluginContainer plugin, Runnable task, long interval, long delay)
    {
        RepeatingTask spoongeTask = new SpoongeRepeatingTask(plugin, task, delay, interval);
        addScheduled(plugin, spoongeTask);
        return Optional.of(spoongeTask);
    }

    @Override
    public Collection<Task> getScheduledTasks()
    {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for(ArrayList<Task> tasksList : scheduled.values())
        {
            tasks.addAll(tasksList);
        }
        return tasks;
    }

    @Override
    public Collection<Task> getScheduledTasks(PluginContainer plugin)
    {
        return scheduled.get(plugin);
    }

    public void removeScheduled(PluginContainer plugin, Task task)
    {
        scheduled.get(plugin).remove(task);
    }

    public void addScheduled(PluginContainer plugin, Task task)
    {
        if(!scheduled.containsKey(plugin))
        {
            scheduled.put(plugin, new ArrayList<Task>());
        }
        scheduled.get(plugin).add(task);
    }

    public void tick()
    {
        ;
    }
}
