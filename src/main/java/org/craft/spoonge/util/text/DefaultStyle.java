package org.craft.spoonge.util.text;

import com.google.common.base.*;

import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.format.TextStyle.Base;

public class DefaultStyle implements Base
{

    private String name;
    private char   code;

    public DefaultStyle(String name, char code)
    {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Optional<Character> getCode()
    {
        return Optional.of(code);
    }

    @Override
    public boolean isComposite()
    {
        return false;
    }

    @Override
    public boolean is(TextStyle style)
    {
        return style == this;
    }

    @Override
    public TextStyle negate()
    {
        return this;
    }

    @Override
    public TextStyle and(TextStyle... styles)
    {
        return new SpoongeTextStyle(this).and(styles);
    }

    @Override
    public TextStyle andNot(TextStyle... styles)
    {
        return new SpoongeTextStyle(this).negate().and(styles);
    }

}
