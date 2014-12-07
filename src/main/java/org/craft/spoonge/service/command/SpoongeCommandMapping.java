package org.craft.spoonge.service.command;

import java.util.*;

import org.craft.commands.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.util.command.*;

@BytecodeModifier("org.craft.commands.OCommandMapping")
public class SpoongeCommandMapping implements CommandMapping
{

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Override
    @Shadow
    public String getPrimaryAlias()
    {
        return null;
    }

    @Override
    @Shadow
    public Set<String> getAllAliases()
    {
        return null;
    }

    @Shadow
    public AbstractCommand getCommand()
    {
        return null;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================

    @Override
    public CommandCallable getCallable()
    {
        return (CommandCallable) getCommand();
    }

}
