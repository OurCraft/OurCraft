package org.craft.utils;

import java.io.*;
import java.text.*;
import java.util.logging.*;

public class LogFormater extends Formatter
{
    private SimpleDateFormat dataformat;

    protected LogFormater()
    {
        this.dataformat = new SimpleDateFormat("[HH:mm:ss]");
    }

    public String format(LogRecord par1LogRecord)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(this.dataformat.format(Long.valueOf(par1LogRecord
                .getMillis())));
        stringbuilder.append(" [" + Thread.currentThread().getName() +  "/").append(par1LogRecord.getLevel().getName()).append("] ");

        stringbuilder.append(this.formatMessage(par1LogRecord));
        stringbuilder.append('\n');
        Throwable throwable = par1LogRecord.getThrown();

        if(throwable != null)
        {
            StringWriter stringwriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringwriter));
            stringbuilder.append(stringwriter.toString());
        }

        return stringbuilder.toString();
    }
}
