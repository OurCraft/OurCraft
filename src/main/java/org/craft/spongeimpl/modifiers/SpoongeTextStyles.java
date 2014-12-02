package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spongeimpl.util.text.*;
import org.spongepowered.api.text.format.*;

@BytecodeModifier(value = "org.spongepowered.api.text.format.TextStyles", replaceStaticBlock = true)
public class SpoongeTextStyles
{

    @Shadow
    public SpoongeTextStyles()
    {
        ;
    }

    @Shadow
    public static final TextStyle.Base OBFUSCATED    = new DefaultStyle("obfuscated", 'o');
    @Shadow
    public static final TextStyle.Base BOLD          = null;
    @Shadow
    public static final TextStyle.Base STRIKETHROUGH = null;
    @Shadow
    public static final TextStyle.Base UNDERLINE     = null;
    @Shadow
    public static final TextStyle.Base ITALIC        = null;

    /**
     * Resets all currently applied text styles to their default values.
     */
    @Shadow
    public static final TextStyle.Base RESET         = null;
}
