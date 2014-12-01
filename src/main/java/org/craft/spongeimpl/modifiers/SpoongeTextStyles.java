package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.text.format.*;

@BytecodeModifier("org.spongepowered.api.text.format.TextStyles")
public class SpoongeTextStyles
{

    @Overwrite
    public static final TextStyle.Base OBFUSCATED    = null;
    @Overwrite
    public static final TextStyle.Base BOLD          = null;
    @Overwrite
    public static final TextStyle.Base STRIKETHROUGH = null;
    @Overwrite
    public static final TextStyle.Base UNDERLINE     = null;
    @Overwrite
    public static final TextStyle.Base ITALIC        = null;

    /**
     * Resets all currently applied text styles to their default values.
     */
    @Overwrite
    public static final TextStyle.Base RESET         = null;
}
