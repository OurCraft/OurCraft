import java.io.*;
import java.util.*;

import org.craft.*;
import org.craft.modding.modifiers.*;
import org.craft.utils.*;
import org.craft.utils.crash.*;
import org.reflections.*;

public class ServerDevStart
{

    public static void main(String[] args) throws IOException, SecurityException, ReflectiveOperationException
    {
        if(!(ClassLoader.getSystemClassLoader() instanceof OurClassLoader))
        {
            new CrashReport("Wrong classloader at launch. Please add -Djava.system.class.loader=org.craft.OurClassLoader in VM arguments").printStack();
            System.exit(-2);
        }
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
        ModifierClassTransformer modTrans = new ModifierClassTransformer();
        OurClassLoader.instance.addTransformer(modTrans);
        Reflections reflections = new Reflections(OurClassLoader.instance);
        for(Class<?> c : reflections.getSubTypesOf(ASMTransformerPlugin.class))
        {
            try
            {
                ASMTransformerPlugin transformers = (ASMTransformerPlugin) c.newInstance();
                transformers.registerModifiers(modTrans);
            }
            catch(InstantiationException e)
            {
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        final File gameFolder = new File(properties.get("gamefolder"));
        SystemUtils.setGameFolder(gameFolder);
        LWJGLSetup.load(new File(gameFolder, "natives"));
        Class<?> clazz = Class.forName("org.craft.server.OurCraftServerStartup");
        clazz.getMethod("main", String[].class).invoke(null, new Object[]
        {
                args
        });
    }

}
