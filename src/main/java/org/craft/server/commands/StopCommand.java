package org.craft.server.commands;

import java.util.*;

import org.craft.commands.*;
import org.craft.server.*;
import org.spongepowered.api.util.command.*;

public class StopCommand extends AbstractCommand
{

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException
    {
        OurCraftServer.getServer().shutdown();
        return true;
    }

    @Override
    public String getName()
    {
        return "stop";
    }

}
