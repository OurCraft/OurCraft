package org.craft;

import org.craft.modding.*;

public final class CommonHandler
{

    private static OurCraftInstance  instance;
    private static AddonContainer<?> currentContainer;

    private CommonHandler()
    {
        throw new IllegalAccessError("Why are you trying to access an instance of static-only class?? Plz tel me");
    }

    public static OurCraftInstance getCurrentInstance()
    {
        return instance;
    }

    public static void setInstance(OurCraftInstance instance)
    {
        CommonHandler.instance = instance;
    }

    public static void setCurrentContainer(AddonContainer<?> container)
    {
        currentContainer = container;
    }

    public static AddonContainer<?> getCurrentContainer()
    {
        return currentContainer;
    }
}
