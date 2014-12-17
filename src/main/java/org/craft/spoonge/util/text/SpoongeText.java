package org.craft.spoonge.util.text;

import org.spongepowered.api.text.message.*;

/**
 * Text Message implementation. Actually Empty, but that will probably change.
 */
public class SpoongeText extends SpoongeMessage implements Message.Text
{

    public String getContent()
    {
        return (String) super.getContent(); // Because I'm too lazy to code anything else
    }

    @Override
    public MessageBuilder.Text builder()
    {
        SpoongeTextMessageBuilder builder = new SpoongeTextMessageBuilder();
        builder.append(this);
        return builder;
    }
}
