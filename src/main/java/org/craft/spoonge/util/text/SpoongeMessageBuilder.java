package org.craft.spoonge.util.text;

import org.spongepowered.api.text.action.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;

public class SpoongeMessageBuilder implements MessageBuilder
{

    private SpoongeMessage message;

    public SpoongeMessageBuilder()
    {
        message = new SpoongeMessage();
    }

    @Override
    public MessageBuilder append(Message... children)
    {
        message.append(children);
        return this;
    }

    @Override
    public MessageBuilder append(Iterable<Message> children)
    {
        message.append(children);
        return this;
    }

    @Override
    public MessageBuilder color(TextColor color)
    {
        message.setColor(color);
        return this;
    }

    @Override
    public MessageBuilder style(TextStyle... styles)
    {
        message.setStyle(new TextStyle().and(styles));
        return this;
    }

    @Override
    public MessageBuilder onClick(ClickAction<?> action)
    {
        message.setOnClickAction(action);
        return this;
    }

    @Override
    public MessageBuilder onHover(HoverAction<?> action)
    {
        message.setOnHoverAction(action);
        return this;
    }

    @Override
    public MessageBuilder onShiftClick(ShiftClickAction<?> action)
    {
        message.setOnShiftClickAction(action);
        return this;
    }

    @Override
    public Message build()
    {
        Message built = message;
        message = new SpoongeMessage();
        return built;
    }

}
