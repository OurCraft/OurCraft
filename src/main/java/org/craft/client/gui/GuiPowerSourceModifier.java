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
    private boolean       strictRepresentation;

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

        addWidget(new GuiButton(6, 0, 360, 200, 40, "Pulses", getFontRenderer()));
        addWidget(new GuiButton(7, 0, 420, 200, 40, "Random", getFontRenderer()));

        addWidget(new GuiButton(8, 0, 480, 200, 40, "Noise", getFontRenderer()));
        addWidget(new GuiButton(9, 0, 540, 200, 40, "Square", getFontRenderer()));

        addWidget(new GuiButton(15, getWidth() - 200, getHeight() - 40, 200, 40, "Strict representation", getFontRenderer()));
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
        else if(widget.getID() == 6)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.PULSES);
        }
        else if(widget.getID() == 7)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.RANDOM);
        }
        else if(widget.getID() == 8)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.NOISE);
        }
        else if(widget.getID() == 9)
        {
            world.setBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode, EnumPowerSourceMode.SQUARE);
        }

        else if(widget.getID() == 15)
        {
            strictRepresentation = !strictRepresentation;
            if(strictRepresentation)
                ((GuiButton) widget).setText("Non-Strict representation");
            else
                ((GuiButton) widget).setText("Strict representation");
        }

        if(widget.getID() <= 14)
            clearPoints();
    }

    public void clearPoints()
    {
        for(Vector2 p : toPlot)
        {
            p.setDisposable(true);
            p.dispose();
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

        int spaceWidth = 300;
        int spaceHeight = 300;
        int originX = getWidth() / 2 - spaceWidth / 2;
        int originY = getHeight() / 2 - spaceHeight / 2;
        int xInSpace = (int) (x * spaceWidth);
        int yInSpace = (int) (y * spaceHeight);
        Gui.drawColoredRect(engine, originX + 0, originY + 0, spaceWidth, spaceHeight, 0xC0FFFFFF);
        Gui.drawColoredRect(engine, originX + xInSpace, originY + yInSpace, 4, 4, 0xFF000000);
        Vector2 previous = null;
        EnumPowerSourceMode mode = (EnumPowerSourceMode) world.getBlockState(this.coordX, this.coordY, this.coordZ, BlockStates.powerSourceMode);
        if(mode != null)
            if(mode.usesBezier() && !strictRepresentation)
            {
                Vector2[] points = new Vector2[toPlot.size()];
                for(int i = 0; i < points.length; i++ )
                {
                    points[i] = toPlot.get(i).mul(spaceWidth, spaceHeight).add(originX, originY);
                    points[i].setDisposable(false);
                }
                Gui.drawBezierCurve(engine, 0xFF0000FF, 10, 4f, points);
            }
            else
            {
                for(Vector2 point : toPlot)
                {
                    if(previous != null)
                    {
                        if(previous.getX() < point.getX())
                        {
                            int xi = (int) Math.floor(point.getX() * spaceWidth);
                            int yi = (int) Math.floor(point.getY() * spaceHeight);
                            int xi2 = (int) Math.floor(previous.getX() * spaceWidth);
                            int yi2 = (int) Math.floor(previous.getY() * spaceHeight);
                            Gui.drawColoredLine(engine, originX + xi, originY + yi, originX + xi2, originY + yi2, 0xFF0000FF, 4f);
                        }
                    }
                    previous = point;
                }
            }
    }

    public void update()
    {
        super.update();
        EnumPowerSourceMode mode = (EnumPowerSourceMode) world.getBlockState(coordX, coordY, coordZ, BlockStates.powerSourceMode);
        if(mode != null)
        {
            x = ((float) world.getTick() % 60f) / 60f;
            y = (float) (15 - mode.function().apply(world.getTick()).powerValue()) / 15f;
            if(world.getTick() % 60 == 0 || x == 0f)
            {
                clearPoints();
            }
            Vector2 v = Vector2.get(x, y);
            v.setDisposable(false);
            toPlot.add(v);
        }
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
