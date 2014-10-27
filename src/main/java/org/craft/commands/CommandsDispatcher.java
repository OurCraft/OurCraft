package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

import org.spongepowered.api.service.command.*;
import org.spongepowered.api.util.command.*;

public class CommandsDispatcher implements CommandDispatcher
{

    private ArrayList<CommandMapping> commandMappings = new ArrayList<CommandMapping>();

    @Override
    public void registerCommand(CommandCallable callable, String... alias)
    {
        commandMappings.add(new OCommandMapping(callable, alias));
    }

    @Override
    public Set<CommandMapping> getCommands()
    {
        return Sets.newHashSet(commandMappings);
    }

    @Override
    public Collection<String> getPrimaryAliases()
    {
        ArrayList<String> list = Lists.newArrayList();
        for(CommandMapping mapping : commandMappings)
        {
            list.add(mapping.getPrimaryAlias());
        }
        return list;
    }

    @Override
    public Collection<String> getAliases()
    {
        ArrayList<String> list = Lists.newArrayList();
        for(CommandMapping mapping : commandMappings)
        {
            list.addAll(mapping.getAllAliases());
        }
        return list;
    }

    @Override
    public com.google.common.base.Optional<CommandMapping> get(String alias)
    {
        for(CommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return com.google.common.base.Optional.of(mapping);
        }
        return com.google.common.base.Optional.absent();
    }

    @Override
    public boolean contains(String alias)
    {
        for(CommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return true;
        }
        return false;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException
    {
        return false;
    }

    @Override
    public Description getDescription()
    {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source)
    {
        return true;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException
    {
        return null;
    }

}
