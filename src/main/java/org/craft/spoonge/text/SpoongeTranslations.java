package org.craft.spoonge.text;

import java.util.*;
import org.spongepowered.api.text.translation.*;

public final class SpoongeTranslations
{

    private static HashMap<String, Translation> map = new HashMap<String, Translation>();

    public static Translation get(String id)
    {
        if(!map.containsKey(id))
            map.put(id, new SpoongeTranslation(id));
        return map.get(id);
    }

}
