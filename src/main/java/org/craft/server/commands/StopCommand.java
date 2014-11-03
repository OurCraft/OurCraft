package org.craft.server.commands;

import java.util.*;

import org.craft.commands.*;
import org.craft.server.*;

public class StopCommand extends AbstractCommand
{

    @Override
    public boolean invoke(ICommandSender source, String arguments, List<String> parents) throws CommandException
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
