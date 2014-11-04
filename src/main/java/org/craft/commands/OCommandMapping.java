package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

public class OCommandMapping
{

    private AbstractCommand command;
    private Set<String>     aliases;
    private String          primary;

    public OCommandMapping(AbstractCommand command, String[] aliases)
    {
        this(command, aliases[0], Sets.newHashSet(aliases));
    }

    public OCommandMapping(AbstractCommand command, String primary, Set<String> aliases)
    {
        this.command = command;
        this.primary = primary;
        this.aliases = aliases;
    }

    public String getPrimaryAlias()
    {
        return primary;
    }

    public Set<String> getAllAliases()
    {
        return aliases;
    }

    public AbstractCommand getCommand()
    {
        return command;
    }

}
