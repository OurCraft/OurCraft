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
    public static <T> MessageBuilder<T> builder(T content)
    {
        return new SpoongeMessageBuilder<T>().content(content);
    }

    @Overwrite
    public static MessageBuilder<Translation> builder(Translation translation, Object... args)
    {
        SpoongeMessageBuilder<Translation> builder = new SpoongeMessageBuilder<Translation>();
        SpoongeMessage<Translation> message = new SpoongeTranslationMessage(translation, args);
        builder.append(message);
        return builder;
    }

    @Overwrite
    public static MessageBuilder<Object> builder(Object score, String override)
    {
        return builder(score);
    }

}
