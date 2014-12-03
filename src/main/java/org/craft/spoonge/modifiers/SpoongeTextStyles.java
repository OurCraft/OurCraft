package org.craft.spoonge.modifiers;

import org.craft.client.render.fonts.*;
import org.craft.modding.modifiers.*;
import org.craft.spoonge.util.text.*;
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
    public static final TextStyle.Base OBFUSCATED    = new DefaultStyle("obfuscated", TextFormatting.OBFUSCATED.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base BOLD          = new DefaultStyle("bold", TextFormatting.BOLD.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base STRIKETHROUGH = new DefaultStyle("strikethrough", 's');                                        // TODO: Add corresponding TextFormatting

    @Shadow
    public static final TextStyle.Base UNDERLINE     = new DefaultStyle("underline", TextFormatting.UNDERLINED.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base ITALIC        = new DefaultStyle("italic", TextFormatting.ITALIC.toString().charAt(1));

    /**
     * Resets all currently applied text styles to their default values.
     */
    @Shadow
    public static final TextStyle.Base RESET         = new DefaultStyle("reset", TextFormatting.RESET.toString().charAt(1));
}
