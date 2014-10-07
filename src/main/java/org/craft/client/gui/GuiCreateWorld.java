package org.craft.client.gui;

import java.io.*;

import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiCreateWorld extends Gui
{

    private GuiTextField worldNameField;

    public GuiCreateWorld(FontRenderer font, File saveFolder)
    {
        super(font);
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        worldNameField = new GuiTextField(0, 10, 10, 200, 40, getFontRenderer());
        addWidget(worldNameField);
    }

    @Override
    public void update()
    {
        worldNameField.updateCursorCounter();
    }

    public void draw(int mx, int my, RenderEngine engine)
    {
        drawBackground(mx, my, engine);
        super.draw(mx, my, engine);
    }
}
