package org.craft.spoonge.modifiers;

import org.craft.client.render.fonts.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.format.TextStyle.Base;

@BytecodeModifier(value = "org.spongepowered.api.text.format.TextStyles", replaceStaticBlock = true)
public class SpoongeTextStyles
{

    @Shadow
    public SpoongeTextStyles()
    {
        ;
    }

    @Shadow
    public static final TextStyle.Base OBFUSCATED    = new Base("obfuscated", TextFormatting.OBFUSCATED.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base BOLD          = new Base("bold", TextFormatting.BOLD.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base STRIKETHROUGH = new Base("strikethrough", 's');                                        // TODO: Add corresponding TextFormatting

    @Shadow
    public static final TextStyle.Base UNDERLINE     = new Base("underline", TextFormatting.UNDERLINED.toString().charAt(1));

    @Shadow
    public static final TextStyle.Base ITALIC        = new Base("italic", TextFormatting.ITALIC.toString().charAt(1));

    /**
     * Resets all currently applied text styles to their default values.
     */
    @Shadow
    public static final TextStyle.Base RESET         = new Base("reset", TextFormatting.RESET.toString().charAt(1));
}
