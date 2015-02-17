package org.craft.client.gui.widgets;

import org.craft.client.*;
import org.craft.client.render.fonts.*;
import org.craft.resources.*;

public class GuiComboBox extends GuiPanel
{

    private GuiTextField               textField;
    private GuiIconButton              dropDownButton;
    private String[]                   values;
    private boolean                    customInput;
    private GuiList<GuiSimpleTextSlot> list;

    public GuiComboBox(int id, int x, int y, int w, int h, OurCraft oc, FontRenderer fontRenderer)
    {
        super(id, x, y, w, h, oc, fontRenderer);
        values = new String[0];
        forceDrawAll = true;

        this.textField = new GuiTextField(0, x, y, w, h, fontRenderer);
        textField.setEditable(false);
        addWidget(textField);

        this.dropDownButton = new GuiIconButton(1, x + w - 32, y + 4, new ResourceLocation("dzqdq"));
        addWidget(dropDownButton);

        this.list = new GuiList<>(2, 4, h - h / 4, w - 8, h, 20);
        list.visible = false;
        list.enabled = false;
        addWidget(list);
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 1)
        {
            list.visible = !list.visible;
            list.enabled = list.visible;

            textField.setShowText(!list.visible);
        }
        else if(widget.getID() == 2)
        {
            if(list.getSelectedIndex() != -1)
            {
                GuiSimpleTextSlot text = list.getSelected();
                textField.setText(text.getMainLabel().getText());
            }
            list.visible = false;
            list.enabled = false;
            textField.setShowText(true);
        }
    }

    public boolean allowsCustomInput()
    {
        return customInput;
    }

    public void allowCustomInput(boolean custom)
    {
        this.customInput = custom;
        textField.setEditable(custom);
        if(!custom)
        {
            for(String s : values)
            {
                if(s.equals(textField.getText()))
                {
                    return;
                }
            }
            textField.setText(values[0]);
        }
    }

    public void setValues(String[] values)
    {
        this.values = values;
        list.clear();
        for(String val : values)
            list.addSlot(new GuiSimpleTextSlot(new GuiLabel(-1, 0, 0, val, fontRenderer)));
    }

    public String[] getValues()
    {
        return values;
    }

    public String getSelected()
    {
        return textField.getText();
    }

    public void setSelected(String value)
    {
        textField.setText(value);
    }
}
