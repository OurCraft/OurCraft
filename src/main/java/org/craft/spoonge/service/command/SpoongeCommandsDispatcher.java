package org.craft.spoonge.service.command;

import java.util.*;

import com.google.common.base.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.commands.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.command.*;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.CommandException;

@BytecodeModifier("org.craft.commands.CommandsDispatcher")
public class SpoongeCommandsDispatcher implements CommandService
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
    public Set<OCommandMapping> getCommandSet()
    {
        return null;
    }

    @Shadow
    public OCommandMapping getFromAlias(String alias)
    {
        return null;
    }

    @Override
    @Shadow
    public boolean containsAlias(String alias)
    {
        return false;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================
    //@Override
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

    // @Override
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
    public Set<String> getPrimaryAliases()
    {
        // TODO Implement
        return null;
    }

    @Override
    public Set<String> getAliases()
    {
        // TODO Implement
        return null;
    }

    @Override
    public boolean containsMapping(CommandMapping mapping)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<String> getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<String> getHelp()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsage()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CommandMapping> register(Object owner, CommandCallable callable, String... alias)
    {
        SpoongeCommand command = new SpoongeCommand(callable, alias[0]);
        registerCommand(command, alias);
        //return Optional.of(command)
        return Optional.absent();
    }

    @Override
    public Optional<CommandMapping> register(Object owner, CommandCallable callable, List<String> aliases)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CommandMapping> register(Object owner, CommandCallable callable, List<String> aliases, Function<List<String>, List<String>> callback)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CommandMapping> remove(String alias)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CommandMapping> removeMapping(CommandMapping mapping)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Set<PluginContainer> getPluginContainers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<CommandMapping> getOwnedBy(PluginContainer container)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
