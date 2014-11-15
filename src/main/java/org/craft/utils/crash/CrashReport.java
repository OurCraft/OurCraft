package org.craft.utils.crash;

import java.nio.charset.*;

import org.craft.client.*;
import org.craft.resources.*;

public class CrashReport
{

    private static String[] comments = new String[]
                                     {
            null, null, "Well, this was a disappointment.", "I'm sorry Dave. I think I can't let you do that", "Here, have a gift http://xkcd.com/953/ ", "This computer is on fiiiiiiiiiiiiiiiiiiiire!"
                                     };

    public static class UndefinedException extends Exception
    {

        public UndefinedException(String message)
        {
            super(message);
        }

        private static final long serialVersionUID = 3352250643266742630L;
    }

    private Throwable exception;

    public CrashReport(String message)
    {
        this(new UndefinedException(message).fillInStackTrace());
    }

    public CrashReport(Throwable throwable)
    {
        this.exception = throwable;
        try
        {
            if(OurCraft.getOurCraft() != null && comments[0] == null)
            {
                comments[0] = new String(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "text/crackedFloppy.ascii")).getData(), Charset.forName("utf-8")).replace("\n       -jglrxavpok", "");
                comments[1] = new String(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "text/deadFace.ascii")).getData(), Charset.forName("utf-8")).replace("\n       -jglrxavpok", "");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void printStack()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(CrashInfos.SECTION_START + " Crash " + CrashInfos.SECTION_END + "\n");
        String comment = generateRandomComment();
        buffer.append(comment + "\n");
        buffer.append("\n" + exception.getClass().getCanonicalName());
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if(exception.getLocalizedMessage() != null)
        {
            buffer.append(": " + exception.getLocalizedMessage());
        }
        else if(exception.getMessage() != null)
            buffer.append(": " + exception.getMessage());
        buffer.append("\n");
        if(stackTrace != null && stackTrace.length > 0)
        {
            for(StackTraceElement elem : stackTrace)
            {
                buffer.append("\tat " + elem.toString() + "\n");
            }
        }
        else
        {
            buffer.append("\t**** Stack Trace is empty ****");
        }
        buffer.append(CrashInfos.SECTION_START + " Game " + CrashInfos.SECTION_END + "\n\tName: OurCraft\n");
        add(buffer, new DateInfos());
        add(buffer, new SystemInfos());
        if(OurCraft.getOurCraft() != null)
        {
            add(buffer, new OpenALInfos());
            add(buffer, new OpenGLInfos());
            add(buffer, new RenderStateInfos(OurCraft.getOurCraft().getRenderEngine()));
        }
        System.out.println(buffer.toString());
    }

    private void add(StringBuffer buffer, CrashInfos infos)
    {
        try
        {
            buffer.append(infos.getInfos() + "\n");
        }
        catch(Exception e)
        {
            ;
        }
    }

    private String generateRandomComment()
    {
        int index = (int) Math.floor(Math.random() * comments.length);
        while(comments[index] == null)
            index = (int) Math.floor(Math.random() * comments.length);
        return comments[index];
    }

}
