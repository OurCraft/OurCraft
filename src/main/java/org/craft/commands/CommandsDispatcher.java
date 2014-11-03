package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

public class CommandsDispatcher
{

    private ArrayList<OCommandMapping> commandMappings = new ArrayList<OCommandMapping>();

    public void registerCommand(AbstractCommand command, String... alias)
    {
        commandMappings.add(new OCommandMapping(command, alias));
    }

    public List<OCommandMapping> getCommands()
    {
        return commandMappings;
    }

    public List<String> getPrimaryAliases()
    {
        ArrayList<String> list = Lists.newArrayList();
        for(OCommandMapping mapping : commandMappings)
        {
            list.add(mapping.getPrimaryAlias());
        }
        return list;
    }

    public List<String> getAliases()
    {
        ArrayList<String> list = Lists.newArrayList();
        for(OCommandMapping mapping : commandMappings)
        {
            list.addAll(mapping.getAllAliases());
        }
        return list;
    }

    public OCommandMapping get(String alias)
    {
        for(OCommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return mapping;
        }
        return null;
    }

    public boolean contains(String alias)
    {
        for(OCommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return true;
        }
        return false;
    }

    public boolean call(AbstractCommand command, String arguments, List<String> parents) throws CommandException
    {
        return command.invoke(null, arguments, parents);
    }

}
