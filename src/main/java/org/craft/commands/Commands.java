package org.craft.commands;

public final class Commands
{

    private static CommandsDispatcher commandDispatcher;

    public static void init()
    {
        commandDispatcher = new CommandsDispatcher();
        register(new HelpCommand(), "help", "h", "halp", "?");
    }

    public static void register(AbstractCommand command, String... aliases)
    {
        commandDispatcher.registerCommand(command, aliases);
    }

    public static CommandsDispatcher getDispatcher()
    {
        return commandDispatcher;
    }
}
