package org.craft.client.gui.widgets;

import org.craft.client.render.fonts.*;

public class GuiSimpleTextSlot extends GuiDualTextSlot
{

    public GuiSimpleTextSlot(String text, FontRenderer font)
    {
        super(text, "", font);
    }

    public GuiSimpleTextSlot(GuiLabel label)
    {
        super(label, new GuiLabel(-1, 0, 0, "", label.getFontRenderer()));
    }

}
