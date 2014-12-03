package org.craft.spongeimpl.util.text;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.text.action.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;

public class SpoongeMessage<T> implements Message<T>
{

    private ShiftClickAction<?>   onShiftClickAction;
    private ClickAction<?>        onClickAction;
    private HoverAction<?>        onHoverAction;
    private ArrayList<Message<?>> hierarchy;
    private T                     content;
    private TextStyle             style;
    private TextColor             color;

    public SpoongeMessage()
    {
        hierarchy = Lists.newArrayList();
    }

    @Override
    public Iterator<Message<T>> iterator()
    {
        //return hierarchy.iterator();
        return null;
    }

    @Override
    public T getContent()
    {
        return content;
    }

    @Override
    public TextColor getColor()
    {
        return color;
    }

    @Override
    public TextStyle getStyle()
    {
        return style;
    }

    @Override
    public List<Message<?>> getChildren()
    {
        return hierarchy;
    }

    @Override
    public Optional<ClickAction<?>> getClickAction()
    {
        //return Optional.of(onClickAction);
        return Optional.absent();
    }

    @Override
    public Optional<HoverAction<?>> getHoverAction()
    {
        //   return Optional.of(onHoverAction);
        return Optional.absent();
    }

    @Override
    public Optional<ShiftClickAction<?>> getShiftClickAction()
    {
        //return Optional.of(onShiftClickAction);
        return Optional.absent();
    }

    @Override
    public MessageBuilder<T> builder()
    {
        SpoongeMessageBuilder<T> builder = new SpoongeMessageBuilder<T>();
        builder.append(hierarchy);
        builder.color(color);
        builder.style(style);
        builder.content(content);
        builder.onClick(onClickAction);
        builder.onShiftClick(onShiftClickAction);
        builder.onHover(onHoverAction);
        return builder;
    }

    public void setOnShiftClickAction(ShiftClickAction<?> onShiftClickAction)
    {
        this.onShiftClickAction = onShiftClickAction;
    }

    public void setOnClickAction(ClickAction<?> onClickAction)
    {
        this.onClickAction = onClickAction;
    }

    public void setOnHoverAction(HoverAction<?> onHoverAction)
    {
        this.onHoverAction = onHoverAction;
    }

    public void append(Message<?>... children)
    {
        for(Message<?> child : children)
            hierarchy.add(child);
    }

    public void append(Iterable<Message<?>> children)
    {
        for(Message<?> child : children)
            hierarchy.add(child);
    }

    public void setContent(T content)
    {
        this.content = content;
    }

    public void setStyle(TextStyle style)
    {
        this.style = style;
    }

    public void setColor(TextColor color)
    {
        this.color = color;
    }
}
