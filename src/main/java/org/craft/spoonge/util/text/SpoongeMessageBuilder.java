package org.craft.spoonge.util.text;

import org.spongepowered.api.text.action.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;

public class SpoongeMessageBuilder<T> implements MessageBuilder<T>
{

    private SpoongeMessage<T> message;

    public SpoongeMessageBuilder()
    {
        message = new SpoongeMessage<T>();
    }

    @Override
    public MessageBuilder<T> append(Message<?>... children)
    {
        message.append(children);
        return this;
    }

    @Override
    public MessageBuilder<T> append(Iterable<Message<?>> children)
    {
        message.append(children);
        return this;
    }

    @Override
    public MessageBuilder<T> content(T content)
    {
        message.setContent(content);
        return this;
    }

    @Override
    public MessageBuilder<T> color(TextColor color)
    {
        message.setColor(color);
        return this;
    }

    @Override
    public MessageBuilder<T> style(TextStyle... styles)
    {
        message.setStyle(new SpoongeTextStyle(styles));
        return this;
    }

    @Override
    public MessageBuilder<T> onClick(ClickAction<?> action)
    {
        message.setOnClickAction(action);
        return this;
    }

    @Override
    public MessageBuilder<T> onHover(HoverAction<?> action)
    {
        message.setOnHoverAction(action);
        return this;
    }

    @Override
    public MessageBuilder<T> onShiftClick(ShiftClickAction<?> action)
    {
        message.setOnShiftClickAction(action);
        return this;
    }

    @Override
    public Message<T> build()
    {
        Message<T> built = message;
        message = new SpoongeMessage<T>();
        return built;
    }

}
