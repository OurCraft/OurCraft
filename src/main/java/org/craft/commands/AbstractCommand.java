package org.craft.commands;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.util.command.*;

public abstract class AbstractCommand implements CommandCallable, Description
{

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException
    {
        return Lists.newArrayList();
    }

    @Override
    public Description getDescription()
    {
        return this;
    }

    @Override
    public boolean testPermission(CommandSource source)
    {
        return true;
    }

    @Override
    public Optional<String> getShortDescription()
    {
        return Optional.absent();
    }

    @Override
    public Optional<String> getHelp()
    {
        return getShortDescription();
    }

    @Override
    public String getUsage()
    {
        return getName();
    }

    public abstract String getName();

    @Override
    public List<String> getPermissions()
    {
        return Lists.newArrayList();
    }

}
