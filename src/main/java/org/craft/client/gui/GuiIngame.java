package org.craft.client.gui;

import java.util.*;
import java.util.Map.Entry;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

public class GuiIngame extends Gui
{

    private float       scale;
    private ScreenTitle title;

    public GuiIngame(OurCraft game)
    {
        super(game);
        this.title = new ScreenTitle();
        title.setMainTitle("<Insert main title here>");
        title.setSubTitle("<Insert sub title here>");
    }

    @Override
    public void init()
    {
        scale = 1.85f;
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        super.draw(mx, my, renderEngine);
        getFontRenderer().drawString("Playing as \"" + oc.getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFFFF, 2, 0, renderEngine);

        CollisionInfos infos = oc.getObjectInFront();
        if(infos != null && infos.type == CollisionType.BLOCK)
        {
            getFontRenderer().drawString("Block at " + infos.x + ", " + infos.y + ", " + infos.z + " is " + TextFormatting.generateFromColor(208, 208, 208) + ((Block) infos.value).getID(), 0xFFFFFFFF, 2, 75, renderEngine);

            BlockStatesObject states = oc.getClientWorld().getBlockStates((int) infos.x, (int) infos.y, (int) infos.z);
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
                    getFontRenderer().drawString(s, 0xFFFFFFFF, oc.getDisplayWidth() - (int) getFontRenderer().getTextWidth(s) - 2, (i++ ) * 15, renderEngine);
                }
            }
        }

        EntityPlayer player = oc.getClientPlayer();
        org.craft.inventory.Stack stack = player.getHeldItem();
        if(stack != null)
        {
            String s = I18n.format(stack.getStackable().getUnlocalizedID());
            getFontRenderer().setScale(scale);
            getFontRenderer().drawShadowedString(s, 0xFFFFFFFF, (int) (oc.getDisplayWidth() / 2 - (int) getFontRenderer().getTextWidth(s) / 2), (int) oc.getDisplayHeight() - 40, renderEngine);
            getFontRenderer().setScale(1);
        }

        if(title != null)
        {
            String mainTitle = title.getMainTitle();
            String subTitle = title.getSubTitle();
            getFontRenderer().setScale(1.15f);
            float titleX = oc.getDisplayWidth() / 2;
            float titleY = oc.getDisplayHeight() / 2;
            float f = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
            getFontRenderer().setScale(f);

            getFontRenderer().drawShadowedString(mainTitle, 0xFFFFFFFF, (int) (titleX - (int) getFontRenderer().getTextWidth(mainTitle) / 2), (int) (titleY - getFontRenderer().getCharHeight('A') * getFontRenderer().getScale()), renderEngine);
            getFontRenderer().setScale(1.85f);
            float f2 = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
            getFontRenderer().setScale(f2);
            getFontRenderer().drawShadowedString(subTitle, 0xFFFFFFFF, (int) (titleX - (int) getFontRenderer().getTextWidth(subTitle) / 2), (int) (titleY), renderEngine);
            getFontRenderer().setScale(1);
        }
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean requiresMouse()
    {
        return false;
    }

}
