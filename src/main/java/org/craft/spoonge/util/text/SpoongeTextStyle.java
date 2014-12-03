package org.craft.spoonge.util.text;

import java.util.*;

import org.spongepowered.api.text.format.*;

public class SpoongeTextStyle implements TextStyle
{

    private ArrayList<TextStyle> components;
    private boolean              composite;

    public SpoongeTextStyle()
    {
        components = new ArrayList<TextStyle>();
    }

    public SpoongeTextStyle(TextStyle... styles)
    {
        this();
        for(TextStyle style : styles)
        {
            components.add(style);
        }
        composite = true;
    }

    @Override
    public boolean isComposite()
    {
        return composite;
    }

    @Override
    public boolean is(TextStyle style)
    {
        return components.contains(style);
    }

    @Override
    public TextStyle negate()
    {
        TextStyle[] negatedArray = new TextStyle[components.size()];
        for(int i = 0; i < negatedArray.length; i++ )
            negatedArray[i] = components.get(i).negate();
        SpoongeTextStyle style = new SpoongeTextStyle(negatedArray);
        return style;
    }

    @Override
    public TextStyle and(TextStyle... styles)
    {
        TextStyle[] negatedArray = new TextStyle[components.size() + styles.length];
        for(int i = 0; i < negatedArray.length; i++ )
            negatedArray[i] = components.get(i);
        for(int i = 0; i < styles.length; i++ )
            negatedArray[components.size() + i] = styles[i];
        SpoongeTextStyle style = new SpoongeTextStyle(negatedArray);
        return style;
    }

    @Override
    public TextStyle andNot(TextStyle... styles)
    {
        TextStyle[] negatedStyles = new TextStyle[styles.length];
        for(int i = 0; i < styles.length; i++ )
            negatedStyles[i] = styles[i].negate();
        return and(negatedStyles);
    }

}
