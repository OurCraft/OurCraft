package org.craft.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

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
        stringbuilder.append(" [Client]");
        
        stringbuilder.append(" [").append(par1LogRecord.getLevel().getName())
                .append("] ");
        stringbuilder.append(this.formatMessage(par1LogRecord));
        stringbuilder.append('\n');
        Throwable throwable = par1LogRecord.getThrown();
        
        if (throwable != null)
        {
            StringWriter stringwriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringwriter));
            stringbuilder.append(stringwriter.toString());
        }
        
        return stringbuilder.toString();
    }
    
}
