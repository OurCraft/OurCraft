package org.craft.client.gui;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
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
import org.craft.world.*;
import org.craft.world.biomes.*;

public class GuiIngame extends Gui
{

    private float       scale;
    private ScreenTitle title;

    public GuiIngame(OurCraft game)
    {
        super(game);
        this.title = oc.getTitle();
        if(title == null)
        {
            this.title = new ScreenTitle();
            title.setRawMainTitle("Dawn of a new day");
            title.setRawSubTitle("78 hours to go.");
        }
    }

    @Override
    public void init()
    {
        scale = 1.85f;
    }

    @Override
    public void render(int mx, int my, RenderEngine renderEngine)
    {
        super.render(mx, my, renderEngine);
        getFontRenderer().drawString("Playing as \"" + oc.getClientUsername() + "\" and password is " + TextFormatting.OBFUSCATED + "LOL_THERE'S_NO_PASSWORD_HERE", 0xFFFFFFFF, 2, 0, renderEngine);
        EntityPlayer player = oc.getClientPlayer();
        Biome biome = null;

        if(player.worldObj.doesChunkExists((int) player.posX / 16, (int) player.posY / 16, (int) player.posZ / 16))
        {
            Chunk chunk = player.worldObj.getChunk((int) player.posX, (int) player.posY, (int) player.posZ);
            biome = chunk.getBiome();
            if(biome != null)
                getFontRenderer().drawString("Biome: " + biome.getID(), 0xFFFFFFFF, 2, 20, renderEngine);
        }

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
            String mainTitle = title.getRawMainTitle();
            String subTitle = title.getRawSubTitle();

            int color = 0;
            int shadowColor = 0;
            long t = title.timeSinceStart();
            if(t <= title.getFadeInDuration() && title.getFadeInDuration() != 0L)
            {
                float i = (float) t / (float) title.getFadeInDuration();
                int alpha = (int) (i * 255f);
                int red = (int) (i * 255f);
                int green = (int) (i * 255f);
                int blue = (int) (i * 255f);
                shadowColor = alpha << 24;
                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
            else if(t <= title.getFadeInDuration() + title.getDisplayTime() && t > title.getFadeInDuration())
            {
                color = 0xFFFFFFFF;
                shadowColor = 0xFF000000;
            }
            else if(t - title.getFadeInDuration() - title.getDisplayTime() < title.getFadeOutDuration())
            {
                float i = 1f - (float) (t - title.getFadeInDuration() - title.getDisplayTime()) / (float) (title.getFadeOutDuration());
                int alpha = (int) (i * 255);
                int red = (int) (i * 255f);
                int green = (int) (i * 255f);
                int blue = (int) (i * 255f);
                shadowColor = alpha << 24;
                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }

            if(t < title.getFadeInDuration() + title.getDisplayTime() + title.getFadeOutDuration())
            {
                renderEngine.enableGLCap(GL_ALPHA_TEST);
                getFontRenderer().setScale(1.15f);
                float titleX = oc.getDisplayWidth() / 2;
                float titleY = oc.getDisplayHeight() / 2;
                float f = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
                getFontRenderer().setScale(f);
                getFontRenderer().drawShadowedString(mainTitle, color, shadowColor, (int) (titleX - (int) getFontRenderer().getTextWidth(mainTitle) / 2), (int) (titleY - getFontRenderer().getCharHeight('A') * getFontRenderer().getScale()), renderEngine);

                getFontRenderer().setScale(1.85f);

                float f2 = (float) oc.getDisplayWidth() / getFontRenderer().getTextWidth(mainTitle);
                getFontRenderer().setScale(f2);
                getFontRenderer().drawShadowedString(subTitle, color, shadowColor, (int) (titleX - (int) getFontRenderer().getTextWidth(subTitle) / 2), (int) (titleY), renderEngine);
                getFontRenderer().setScale(1);

                renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
            if(glfwGetKey(OurCraft.getOurCraft().windowPointer, GLFW_KEY_T) == GLFW_PRESS)
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

    public void setTitle(ScreenTitle screenTitle)
    {
        this.title = screenTitle;
    }

}
