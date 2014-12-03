package org.craft.spoonge.text;

import org.craft.client.*;
import org.spongepowered.api.text.translation.*;

public class SpoongeTranslation implements Translation
{

    private String id;

    public SpoongeTranslation(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String get()
    {
        return get(new Object[0]);
    }

    @Override
    public String get(Object... args)
    {
        return I18n.format(id, args);
    }

}
