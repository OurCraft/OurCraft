package org.craft.commands;

import java.util.*;

import org.spongepowered.api.util.command.*;

public class HelpCommand extends AbstractCommand
{

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException
    {
        for(CommandMapping mapping : Commands.getDispatcher().getCommands())
        {
            String helpText = "";
            Iterator<String> it = mapping.getAllAliases().iterator();
            while(it.hasNext())
            {
                String alias = it.next();
                if(helpText.length() != 0 && it.hasNext())
                    helpText += ", ";
                else if(!it.hasNext())
                    helpText += " and ";
                helpText += "'" + alias + "'";
            }
            helpText += ": ";
            if(mapping.getDescription() != null)
            {
                String desc = "";
                if(mapping.getDescription().getShortDescription() != null)
                    desc += " - " + mapping.getDescription().getShortDescription();
                helpText += mapping.getDescription().getUsage() + desc;
            }
            else
            {
                helpText += "No description available";
            }
            source.sendMessage(helpText);
        }
        return true;
    }

    public String getShortCommandDescription()
    {
        return "Displays an usage tip about every command in the game";
    }

    @Override
    public String getName()
    {
        return "help";
    }

}
