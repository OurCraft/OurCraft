package org.craft.client.gui;

import static org.lwjgl.opengl.GL11.*;

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
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

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
            getFontRenderer().drawString("Block at " + infos.x + ", " + infos.y + ", " + infos.z + " is " + TextFormatting.generateFromColor(208, 208, 208) + ((Block) infos.value).getRawID(), 0xFFFFFFFF, 2, 75, renderEngine);

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

            int color = 0;
            long t = title.timeSinceStart();
            if(t <= title.getFadeIn() && title.getFadeIn() != 0L)
            {
                float i = (float) t / (float) title.getFadeIn();
                int alpha = (int) (i * 0f);
                int red = (int) (i * 255f);
                int green = (int) (i * 255f);
                int blue = (int) (i * 255f);
                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
            else if(t <= title.getFadeIn() + title.getDisplayTime() && t > title.getFadeIn())
            {
                color = 0xFFFFFFFF;
            }
            else if(t - title.getFadeIn() - title.getDisplayTime() < title.getFadeOut())
            {
                float i = 1f - (float) (t - title.getFadeIn() - title.getDisplayTime()) / (float) (title.getFadeOut());
                int alpha = (int) (i * 255);
                int red = (int) (i * 255f);
                int green = (int) (i * 255f);
                int blue = (int) (i * 255f);
                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }

            if(t < title.getFadeIn() + title.getDisplayTime() + title.getFadeOut())
            {
                //            renderEngine.enableGLCap(GL_BLEND);
                GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
                renderEngine.enableGLCap(GL_ALPHA_TEST);
                getFontRenderer().setScale(1.15f);
                float titleX = oc.getDisplayWidth() / 2;
                float titleY = oc.getDisplayHeight() / 2;
                float f = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
                getFontRenderer().setScale(f);
                getFontRenderer().drawShadowedString(mainTitle, color, (int) (titleX - (int) getFontRenderer().getTextWidth(mainTitle) / 2), (int) (titleY - getFontRenderer().getCharHeight('A') * getFontRenderer().getScale()), renderEngine);
                getFontRenderer().setScale(1.85f);

                float f2 = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
                getFontRenderer().setScale(f2);
                getFontRenderer().drawShadowedString(subTitle, color, (int) (titleX - (int) getFontRenderer().getTextWidth(subTitle) / 2), (int) (titleY), renderEngine);
                getFontRenderer().setScale(1);

                renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_T))
            {
                title.setFadeInDuration(2000);
                title.setFadeOutDuration(2000);
                title.setDisplayDuration(2000);
                title.show();
            }
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
