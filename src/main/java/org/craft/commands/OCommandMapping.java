package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

import org.spongepowered.api.util.command.*;

public class OCommandMapping implements CommandMapping
{

    private CommandCallable command;
    private String[]        aliases;

    public OCommandMapping(CommandCallable callable, String[] alias)
    {
        this.command = callable;
        this.aliases = alias;
    }

    @Override
    public String getPrimaryAlias()
    {
        return aliases[0];
    }

    @Override
    public Set<String> getAllAliases()
    {
        return Sets.newHashSet(aliases);
    }

    @Override
    public CommandCallable getCallable()
    {
        return command;
    }

    @Override
    public Description getDescription()
    {
        return command.getDescription();
    }

}
