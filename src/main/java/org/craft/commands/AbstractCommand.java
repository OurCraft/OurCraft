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

    @Deprecated
    public final Optional<String> getShortDescription()
    {
        return Optional.of(getShortCommandDescription());
    }

    @Deprecated
    public final Optional<String> getHelp()
    {
        return Optional.of(getCommandHelp());
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

    public String getShortCommandDescription()
    {
        return null;
    }

    public String getCommandHelp()
    {
        return null;
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
