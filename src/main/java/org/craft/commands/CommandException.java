package org.craft.commands;


public class CommandException extends Exception
{

    private static final long serialVersionUID = 935887472234193760L;

    public CommandException()
    {
        this("");
    }

    public CommandException(String message)
    {
        super(message);
    }

    public CommandException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CommandException(Throwable cause)
    {
        super(cause);
    }
}
