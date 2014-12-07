package org.craft.client.gui.widgets;

import org.craft.client.*;
import org.craft.client.render.fonts.*;
import org.lwjgl.input.*;

public class GuiSpinner extends GuiPanel
{

    private GuiTextField textInput;
    private GuiButton    upButton;
    private GuiButton    downButton;
    private float        value;
    private float        step;

    public GuiSpinner(int id, int x, int y, int w, int h, FontRenderer font)
    {
        super(id, x, y, w, h, OurCraft.getOurCraft(), font);
        this.textInput = new GuiTextField(0, 0, 0, w - 20, h, font);
        textInput.setText("0");
        upButton = new GuiButton(1, w - 20, 0, 20, h / 2, "^", font);
        downButton = new GuiButton(2, w - 20, h / 2, 20, h / 2, "V", font);

        addWidget(textInput);
        addWidget(upButton);
        addWidget(downButton);
        step = 1f;
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 1)
        {
            value += step;
            updateDisplay();
        }
        else if(widget.getID() == 2)
        {
            value -= step;
            updateDisplay();
        }
    }

    public void setValue(float value)
    {
        this.value = value;
        updateDisplay();
    }

    private void updateDisplay()
    {
        this.textInput.setText("" + value);
    }

    public float getValue()
    {
        return value;
    }

    public void setStep(float step)
    {
        this.step = step;
    }

    public float getStep()
    {
        return step;
    }

    @Override
    public boolean keyReleased(int id, char c)
    {
        if(textInput.isFocused())
            if(GuiTextField.isLetter(c) && c < '0' || c > '9')
                return false;
            else if(id == Keyboard.KEY_UP)
            {
                value += step;
                updateDisplay();
            }
            else if(id == Keyboard.KEY_DOWN)
            {
                value -= step;
                updateDisplay();
            }
        return super.keyReleased(id, c);
    }

    @Override
    public void update()
    {
        String txt = textInput.getText();
        if(txt.length() > 0)
        {
            try
            {
                float newValue = Float.parseFloat(txt);
                value = newValue;
            }
            catch(Exception e)
            {
                textInput.setText("" + value);
            }
        }
    }
}
