package org.craft.spoonge.util.text;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.text.action.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;

public class SpoongeMessage implements Message
{

    private ShiftClickAction<?> onShiftClickAction;
    private ClickAction<?>      onClickAction;
    private HoverAction<?>      onHoverAction;
    private ArrayList<Message>  hierarchy;
    private Object              content;
    private TextStyle           style;
    private TextColor           color;

    public SpoongeMessage()
    {
        hierarchy = Lists.newArrayList();
    }

    @Override
    public Object getContent()
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
    public List<Message> getChildren()
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
    public MessageBuilder builder()
    {
        SpoongeMessageBuilder builder = new SpoongeMessageBuilder();
        builder.append(hierarchy);
        builder.color(color);
        builder.style(style);
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

    public void append(Message... children)
    {
        for(Message child : children)
            hierarchy.add(child);
    }

    public void append(Iterable<Message> children)
    {
        for(Message child : children)
            hierarchy.add(child);
    }

    public void setContent(Object content)
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

    @Override
    public Iterable<Message> withChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Deprecated
    public String toLegacy()
    {
        return null;
    }

    @Override
    @Deprecated
    public String toLegacy(char code)
    {
        return null;
    }
}
