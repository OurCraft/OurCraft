package org.craft.commands;

import java.util.*;

import com.google.common.collect.*;

public class OCommandMapping
{

    private AbstractCommand command;
    private List<String>    aliases;

    public OCommandMapping(AbstractCommand command, String[] aliases)
    {
        this(command, Lists.newArrayList(aliases));
    }

    public OCommandMapping(AbstractCommand command, List<String> aliases)
    {
        this.command = command;
        this.aliases = aliases;
    }

    public String getPrimaryAlias()
    {
        return aliases.get(0);
    }

    public List<String> getAllAliases()
    {
        return aliases;
    }

    public AbstractCommand getCommand()
    {
        return command;
    }

}
