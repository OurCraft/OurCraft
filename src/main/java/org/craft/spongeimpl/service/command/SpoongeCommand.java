package org.craft.spongeimpl.service.command;

import java.util.*;

import org.craft.commands.*;
import org.craft.commands.CommandException;
import org.spongepowered.api.util.command.*;

public class SpoongeCommand extends AbstractCommand
{

    private CommandCallable callable;
    private String          name;

    public SpoongeCommand(CommandCallable callable, String name)
    {
        this.name = name;
        this.callable = callable;
    }

    @Override
    public boolean invoke(ICommandSender source, String argument, List<String> parents) throws CommandException
    {
        try
        {
            return callable.call((CommandSource) source, argument, parents);
        }
        catch(org.spongepowered.api.util.command.CommandException e)
        {
            throw new CommandException(e);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    public boolean testPermission(ICommandSender source)
    {
        return callable.testPermission((CommandSource) source);
    }

    public String getShortCommandDescription()
    {
        return callable.getDescription().getShortDescription().get();
    }

    public String getCommandHelp()
    {
        return callable.getDescription().getHelp().get();
    }

    public String getUsage()
    {
        return callable.getDescription().getUsage();
    }

    public List<String> getPermissions()
    {
        return callable.getDescription().getPermissions();
    }

}
