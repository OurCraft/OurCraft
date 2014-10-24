import java.io.*;
import java.util.*;

import org.craft.client.start.*;
import org.craft.utils.*;

public class ClientDevStart
{

    public static void main(String[] args) throws Exception
    {
        HashMap<String, String> properties = new HashMap<String, String>();
        String current = null;
        properties.put("username", "Player_" + (int) (Math.random() * 100000L));
        properties.put("lang", "en_US");
        properties.put("gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        for(int i = 0; i < args.length; i++ )
        {
            String arg = args[i];
            if(arg.startsWith("--"))
            {
                current = arg.substring(2);
            }
            else
            {
                properties.put(current, arg);
            }
        }
        final File gameFolder = new File(properties.get("gamefolder"));
        SystemUtils.setGameFolder(gameFolder);
        LWJGLSetup.load(new File(gameFolder, "natives"));
        OurCraftStartup.start(properties);
    }

}
