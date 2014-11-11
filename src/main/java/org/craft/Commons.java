package org.craft;

import java.util.*;

import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.modding.modifiers.*;
import org.craft.utils.*;

public final class Commons
{

    public static void applyArguments(HashMap<String, String> properties)
    {
        boolean debug = properties.get("debug") != null && !properties.get("debug").equalsIgnoreCase("false");
        Log.useFullClassNames = debug;
        Log.showCaller = debug;
        ModifierClassTransformer.debug = debug;
        RenderEngine.debug = debug;
        RenderItems.debug = debug;
        ModelLoader.debug = debug;
    }

}
