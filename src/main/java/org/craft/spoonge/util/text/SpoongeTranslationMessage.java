package org.craft.spoonge.util.text;

import org.spongepowered.api.text.translation.*;

public class SpoongeTranslationMessage extends SpoongeMessage
{

    private Object[] args;

    public SpoongeTranslationMessage(Translation trans, Object... args)
    {
        super();
        setContent(trans);
        this.args = args;
    }

    public Object[] getArgs()
    {
        return args;
    }
}
