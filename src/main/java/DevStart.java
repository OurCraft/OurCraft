import java.io.*;
import java.util.*;

import org.craft.*;
import org.craft.utils.crash.*;

public abstract class DevStart
{

    public void start(String[] args) throws ReflectiveOperationException, IOException
    {
        if(!(ClassLoader.getSystemClassLoader() instanceof OurClassLoader))
        {
            new CrashReport("Wrong classloader at launch. Please add -Djava.system.class.loader=org.craft.OurClassLoader in VM arguments").printStack();
            System.exit(-2);
        }
        Map<String, String> properties = Startup.argsToMap(args);

        applyDefaults(properties);
        Startup.applyArguments(properties);
        Startup.loadTransformers();

        preInit(properties);
        Class<?> clazz = Class.forName(getStartupClassName());
        clazz.getMethod("main", String[].class).invoke(null, new Object[]
        {
                args
        });
    }

    public abstract String getStartupClassName();

    public void preInit(Map<String, String> properties)
    {
        ;
    }

    public void applyDefaults(Map<String, String> properties)
    {
        ;
    }

    public static void applyDefault(Map<String, String> properties, String key, String value)
    {
        if(!properties.containsKey(key))
            properties.put(key, value);
    }
}
