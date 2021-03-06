package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

public class CommandsDispatcher
{

    private List<OCommandMapping> commandMappings = Lists.newArrayList();

    public void registerCommand(AbstractCommand command, String... alias)
    {
        commandMappings.add(new OCommandMapping(command, alias));
    }

    public Set<OCommandMapping> getCommandSet()
    {
        return Sets.newHashSet(commandMappings);
    }

    public Collection<String> getPrimaryAliases()
    {
        List<String> list = Lists.newArrayList();
        for(OCommandMapping mapping : commandMappings)
        {
            list.add(mapping.getPrimaryAlias());
        }
        return list;
    }

    public Collection<String> getAliases()
    {
        List<String> list = Lists.newArrayList();
        for(OCommandMapping mapping : commandMappings)
        {
            list.addAll(mapping.getAllAliases());
        }
        return list;
    }

    public OCommandMapping getFromAlias(String alias)
    {
        for(OCommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return mapping;
        }
        return null;
    }

    public boolean containsAlias(String alias)
    {
        for(OCommandMapping mapping : commandMappings)
        {
            if(mapping.getAllAliases().contains(alias))
                return true;
        }
        return false;
    }

}
