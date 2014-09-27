package org.craft.client.gui;

import java.util.*;
import java.util.Map.Entry;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

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
        super.draw(mx, my, renderEngine);
        getFontRenderer().drawString("Playing as \"" + OurCraft.getOurCraft().getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFF, 2, 0, renderEngine);
        getFontRenderer().drawString("Free memory: " + (OurCraft.getOurCraft().getFreeMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getFreeMemory() / 1000000L) + "Mb", 0x00FF00, 2, 15, renderEngine);
        getFontRenderer().drawString("Used memory: " + (OurCraft.getOurCraft().getUsedMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getUsedMemory() / 1000000L) + "Mb", 0x00FF00, 2, 30, renderEngine);
        getFontRenderer().drawString("Total memory: " + (OurCraft.getOurCraft().getTotalMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getTotalMemory() / 1000000L) + "Mb", 0x00FF00, 2, 45, renderEngine);
        getFontRenderer().drawString("Max available memory: " + (OurCraft.getOurCraft().getMaxMemory() / 1000L) + "kb:" + (OurCraft.getOurCraft().getMaxMemory() / 1000000L) + "Mb", 0x00FF00, 2, 60, renderEngine);

        CollisionInfos infos = OurCraft.getOurCraft().getObjectInFront();
        if(infos != null && infos.type == CollisionType.BLOCK)
        {
            getFontRenderer().drawString("Block at " + infos.x + ", " + infos.y + ", " + infos.z + " is " + TextFormatting.generateFromColor(208, 208, 208) + ((Block) infos.value).getID(), 0xFFFFFFFF, 2, 75, renderEngine);

            BlockStatesObject states = OurCraft.getOurCraft().getClientWorld().getBlockStates((int) infos.x, (int) infos.y, (int) infos.z);
            if(states != null)
            {
                Iterator<Entry<BlockState, IBlockStateValue>> it = states.getMap().entrySet().iterator();
                int i = 0;
                while(it.hasNext())
                {
                    Entry<BlockState, IBlockStateValue> entry = it.next();
                    String s = entry.getKey().toString() + ":" + TextFormatting.generateFromColor(0, 255, 0) + entry.getValue().toString();
                    getFontRenderer().drawString(s, 0xFFFFFFFF, OurCraft.getOurCraft().getDisplayWidth() - (int) getFontRenderer().getTextLength(s) - 2, (i++ ) * 15, renderEngine);
                }
            }
        }
    }

    @Override
    public void update()
    {
    }

    @Override
    public boolean requiresMouse()
    {
        return false;
    }

}
