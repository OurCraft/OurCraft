package org.craft.commands;

import java.util.*;

public class HelpCommand extends AbstractCommand
{

    @Override
    public boolean invoke(ICommandSender source, String arguments, List<String> parents) throws CommandException
    {
        for(OCommandMapping mapping : Commands.getDispatcher().getCommands())
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
            String desc = "";
            desc = "No description available";
            if(mapping.getCommand().getShortCommandDescription() != null)
                desc = " - " + mapping.getCommand().getShortCommandDescription();
            helpText += mapping.getCommand().getUsage() + desc;
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
