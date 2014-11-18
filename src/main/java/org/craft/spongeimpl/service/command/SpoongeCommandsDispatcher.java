package org.craft.spongeimpl.service.command;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.commands.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.service.command.*;
import org.spongepowered.api.util.*;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.CommandException;

@BytecodeModifier("org.craft.commands.CommandsDispatcher")
public class SpoongeCommandsDispatcher implements CommandDispatcher
{

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public SpoongeCommandsDispatcher()
    {
        ;
    }

    @Shadow
    public void registerCommand(AbstractCommand command, String... alias)
    {
        ;
    }

    @Shadow
    @Override
    public Collection<String> getPrimaryAliases()
    {
        return null;
    }

    @Shadow
    @Override
    public Collection<String> getAliases()
    {
        return null;
    }

    @Shadow
    public Set<OCommandMapping> getCommandSet()
    {
        return null;
    }

    @Shadow
    public OCommandMapping getFromAlias(String alias)
    {
        return null;
    }

    @Shadow
    public boolean containsAlias(String alias)
    {
        return false;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================
    @Override
    public Set<CommandMapping> getCommands()
    {
        List<CommandMapping> mappings = Lists.newArrayList();
        for(OCommandMapping command : getCommandSet())
        {
            mappings.add((CommandMapping) command);
        }
        return Sets.newHashSet(mappings);
    }

    @Override
    public Optional<CommandMapping> get(String alias)
    {
        return Optional.of((CommandMapping) getFromAlias(alias));
    }

    @Override
    public boolean contains(String alias)
    {
        return containsAlias(alias);
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
        return false;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException
    {
        return Lists.newArrayList();
    }

    @Override
    public void registerCommand(CommandCallable callable, Owner owner, String... alias)
    {
        registerCommand(new SpoongeCommand(callable, alias[0]), alias);
    }

}
