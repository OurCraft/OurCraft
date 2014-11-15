import java.io.*;
import java.util.*;

import org.craft.utils.*;

public class ClientDevStart extends DevStart
{

    public static void main(String[] args) throws IOException, ReflectiveOperationException
    {
        new ClientDevStart().start(args);
    }

    @Override
    public void applyDefaults(Map<String, String> properties)
    {
        applyDefault(properties, "username", "Player_" + (int) (Math.random() * 100000L));
        applyDefault(properties, "lang", "en_US");
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
    }

    @Override
    public void preInit(Map<String, String> properties)
    {
        final File gameFolder = new File(properties.get("gamefolder"));
        try
        {
            LWJGLSetup.load(new File(gameFolder, "natives"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getStartupClassName()
    {
        return "org.craft.client.OurCraftStartup";
    }

}
