package org.craft.spongeimpl.service.command;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.commands.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.service.command.*;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.CommandException;

@BytecodeModifier("org.craft.commands.CommandsDispatcher")
public class SpoongeCommandsDispatcher implements CommandDispatcher
{

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public void registerCommand(AbstractCommand command, String... alias)
    {
        ;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================
    @Override
    public void registerCommand(CommandCallable callable, String... alias)
    {
        registerCommand(new SpoongeCommand(callable, alias[0]), alias);
    }

    @Override
    public Set<CommandMapping> getCommands()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getPrimaryAliases()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getAliases()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CommandMapping> get(String alias)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(String alias)
    {
        // TODO Auto-generated method stub
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
        return false;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException
    {
        return Lists.newArrayList();
    }

}
