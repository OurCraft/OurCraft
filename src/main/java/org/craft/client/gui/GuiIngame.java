package org.craft.client.gui;

import java.util.*;
import java.util.Map.Entry;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

public class GuiIngame extends Gui
{

    private float   scale;
    private Matrix4 scaleMatrix;

    public GuiIngame(FontRenderer fontRenderer)
    {
        super(fontRenderer);
    }

    @Override
    public void init()
    {
        scale = 1.85f;
        scaleMatrix = Matrix4.get().initScale(scale, scale, 1);
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        super.draw(mx, my, renderEngine);
        getFontRenderer().drawString("Playing as \"" + OurCraft.getOurCraft().getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFF, 2, 0, renderEngine);

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
                    if(entry.getKey() == null || entry.getValue() == null)
                        continue;
                    String s = entry.getKey().toString() + ":" + TextFormatting.generateFromColor(0, 255, 0) + entry.getValue().toString();
                    getFontRenderer().drawString(s, 0xFFFFFFFF, OurCraft.getOurCraft().getDisplayWidth() - (int) getFontRenderer().getTextLength(s) - 2, (i++ ) * 15, renderEngine);
                }
            }
        }

        EntityPlayer player = (EntityPlayer) OurCraft.getOurCraft().getPlayer(null);
        org.craft.inventory.Stack stack = player.getHeldItem();
        if(stack != null)
        {
            String s = I18n.format(stack.getItem().getUnlocalizedID());
            Matrix4 m = renderEngine.getModelviewMatrix().copy();
            renderEngine.setModelviewMatrix(m.mul(scaleMatrix));
            getFontRenderer().drawShadowedString(s, 0xFFFFFF, (int) (OurCraft.getOurCraft().getDisplayWidth() / (2 * scale) - (int) getFontRenderer().getTextLength(s) / (2 * scale)), (int) (OurCraft.getOurCraft().getDisplayHeight() / scale) - 40, renderEngine);
            renderEngine.setModelviewMatrix(m);
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
