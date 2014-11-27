package org.craft.client.gui;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.states.*;
import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.world.*;
import org.lwjgl.input.*;

public class GuiPowerSourceModifier extends Gui
{

    private int           coordX;
    private int           coordY;
    private int           coordZ;
    private World         world;
    private float         x;
    private float         y;
    private List<Vector2> toPlot;

    public GuiPowerSourceModifier(OurCraft game, World w, int x, int y, int z)
    {
        super(game);
        toPlot = Lists.newArrayList();
        this.world = w;
        this.coordX = x;
        this.coordY = y;
        this.coordZ = z;
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, 0, 0, 200, 40, "Constant", getFontRenderer()));
        addWidget(new GuiButton(1, 0, 60, 200, 40, "Sin", getFontRenderer()));
        addWidget(new GuiButton(2, 0, 120, 200, 40, "Cos", getFontRenderer()));
        addWidget(new GuiButton(3, 0, 180, 200, 40, "Tan", getFontRenderer()));
        addWidget(new GuiButton(4, 0, 240, 200, 40, "Inverse", getFontRenderer()));
        addWidget(new GuiButton(5, 0, 300, 200, 40, "Square root", getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        super.actionPerformed(widget);
        if(widget.getID() == 0)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.CONSTANT);
        }
        else if(widget.getID() == 1)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.SIN);
        }
        else if(widget.getID() == 2)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.COS);
        }
        else if(widget.getID() == 3)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.TAN);
        }
        else if(widget.getID() == 4)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.INVERSE);
        }
        else if(widget.getID() == 5)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.SQRT);
        }
        toPlot.clear();
    }

    public void render(int mx, int my, RenderEngine engine)
    {
        Gui.drawColoredRect(engine, 0, 0, getWidth(), getHeight(), 0x70000000);
        super.render(mx, my, engine);
        getFontRenderer().setScale(1.5f);
        String title = I18n.format("gui.electricPowerSourceModifier.title");
        getFontRenderer().drawShadowedString(title, 0xFFFFFFFF, (int) (getWidth() / 2 - getFontRenderer().getTextWidth(title) / 2), getHeight() / 9, engine);
        getFontRenderer().setScale(1f);

        int repereWidth = 300;
        int repereHeight = 300;
        int originX = getWidth() / 2 - repereWidth / 2;
        int originY = getHeight() / 2 - repereHeight / 2;
        int coordX = (int) (x * repereWidth);
        int coordY = (int) (y * repereHeight);
        Gui.drawColoredRect(engine, originX + 0, originY + 0, repereWidth, repereHeight, 0xC0FFFFFF);
        Gui.drawColoredRect(engine, originX + coordX, originY + coordY, 4, 4, 0xFF000000);
        Vector2 previous = null;
        for(Vector2 point : toPlot)
        {
            if(previous != null)
            {
                if(previous.getX() < point.getX())
                {
                    int xi = (int) Math.floor(point.getX() * repereWidth);
                    int yi = (int) Math.floor(point.getY() * repereHeight);
                    int xi2 = (int) Math.floor(previous.getX() * repereWidth);
                    int yi2 = (int) Math.floor(previous.getY() * repereHeight);
                    Gui.drawColoredLine(engine, originX + xi, originY + yi, originX + xi2, originY + yi2, 0xFF0000FF, 4f);
                }
            }
            previous = point;
        }
    }

    public void update()
    {
        super.update();
        EnumPowerSourceMode mode = (EnumPowerSourceMode) world.getBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode);
        x = ((float) world.getTick() % 60f) / 60f;
        y = (float) (15 - mode.function().apply(world.getTick()).powerValue()) / 15f;
        if(world.getTick() % 60 == 0 || x == 0f)
        {
            toPlot.clear();
        }
        toPlot.add(Vector2.get(x, y));
    }

    public boolean keyReleased(int id, char c)
    {
        if(id == Keyboard.KEY_ESCAPE)
        {
            oc.openMenu(new GuiIngame(oc));
            return true;
        }
        return super.keyReleased(id, c);
    }

}
