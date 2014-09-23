package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiIngame extends Gui
{

    public GuiIngame(FontRenderer fontRenderer)
    {
        super(fontRenderer);
    }

    @Override
    public void init()
    {

    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        getFontRenderer().drawString("Playing as \"" + OurCraft.getOurCraft().getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFF, 2, 0, renderEngine);
        getFontRenderer().drawString("Free memory: " + (OurCraft.getOurCraft().getFreeMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getFreeMemory() / 1000000L) + "Mb", 0x00FF00, 2, 15, renderEngine);
        getFontRenderer().drawString("Used memory: " + (OurCraft.getOurCraft().getUsedMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getUsedMemory() / 1000000L) + "Mb", 0x00FF00, 2, 30, renderEngine);
        getFontRenderer().drawString("Total memory: " + (OurCraft.getOurCraft().getTotalMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getTotalMemory() / 1000000L) + "Mb", 0x00FF00, 2, 45, renderEngine);
        getFontRenderer().drawString("Max available memory: " + (OurCraft.getOurCraft().getMaxMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getMaxMemory() / 1000000L) + "Mb", 0x00FF00, 2, 60, renderEngine);
    }

    @Override
    public void update()
    {
    }

}
