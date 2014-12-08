package org.craft.spoonge.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spoonge.util.title.*;
import org.spongepowered.api.text.title.*;

@BytecodeModifier("org.spongepowered.api.text.title.Titles")
public class SpoongeTitles
{

    @Shadow
    public SpoongeTitles()
    {
        ;
    }

    @Overwrite
    public static TitleBuilder builder()
    {
        new SpoongeTitle().show();
        return new SpoongeTitleBuilder();
    }

    @Overwrite
    public static TitleBuilder update()
    {
        return new SpoongeTitleBuilder();
    }
}
