package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

public abstract class AbstractCommand
{

    public abstract boolean invoke(ICommandSender source, String argument, List<String> parents) throws CommandException;

    public List<String> getSuggestions(ICommandSender source, String arguments)
    {
        return Lists.newArrayList();
    }

    public boolean testPermission(ICommandSender source)
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

    public String getUsage()
    {
        return getName();
    }

    public abstract String getName();

    public List<String> getPermissions()
    {
        return Lists.newArrayList();
    }

}
