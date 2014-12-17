package org.craft.spoonge.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spoonge.util.text.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.translation.*;

@BytecodeModifier("org.spongepowered.api.text.message.Messages")
public class SpoongeMessages
{

    @Shadow
    private SpoongeMessages()
    {
        ;
    }

    @Overwrite
    public static MessageBuilder.Text builder(String content)
    {
        SpoongeMessageBuilder builder = new SpoongeMessageBuilder();
        throw new UnsupportedOperationException();
    }

    @Overwrite
    public static MessageBuilder builder(Translation translation, Object... args)
    {
        SpoongeMessageBuilder builder = new SpoongeMessageBuilder();
        SpoongeMessage message = new SpoongeTranslationMessage(translation, args);
        builder.append(message);
        return builder;
    }

    @Overwrite
    public static MessageBuilder builder(Object score, String override)
    {
        return builder(override);
    }

}
