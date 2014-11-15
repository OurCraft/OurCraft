package org.craft.client;

import java.io.*;
import java.util.*;

import org.lwjgl.input.*;

public class GameSettings
{

    public GameOption jumpKey;
    public GameOption forwardKey;
    public GameOption backwardsKey;
    public GameOption leftKey;
    public GameOption rightKey;
    public GameOption lang;
    public GameOption font;
    public GameOption sensitivity;

    public GameSettings()
    {
        jumpKey = new GameOption("jumpKey", GameOptionType.INPUT);
        jumpKey.setValue(Keyboard.KEY_SPACE + "");

        forwardKey = new GameOption("forwardKey", GameOptionType.INPUT);
        forwardKey.setValue(Keyboard.KEY_W + "");

        backwardsKey = new GameOption("backwardsKey", GameOptionType.INPUT);
        backwardsKey.setValue(Keyboard.KEY_S + "");

        leftKey = new GameOption("leftKey", GameOptionType.INPUT);
        leftKey.setValue(Keyboard.KEY_A + "");

        rightKey = new GameOption("rightKey", GameOptionType.INPUT);
        rightKey.setValue(Keyboard.KEY_D + "");

        lang = new GameOption("lang", GameOptionType.PLAIN_TEXT);
        lang.setValue("en_US");

        font = new GameOption("font", GameOptionType.PLAIN_TEXT);
        font.setValue("default");

        sensitivity = new GameOption("sensitivity", GameOptionType.RANGE);
        sensitivity.setValue("1");
    }

    public void loadFrom(File file) throws IOException
    {
        if(!file.exists())
            return;
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(file);
        props.load(in);
        in.close();
        load(jumpKey, props);
        load(leftKey, props);
        load(rightKey, props);
        load(forwardKey, props);
        load(backwardsKey, props);
        load(lang, props);
        load(font, props);
        load(sensitivity, props);
    }

    public void saveTo(File file) throws IOException
    {
        if(!file.exists())
            file.createNewFile();
        Properties props = new Properties();
        props.put(jumpKey.getID(), jumpKey.getValue());
        props.put(leftKey.getID(), leftKey.getValue());
        props.put(rightKey.getID(), rightKey.getValue());
        props.put(forwardKey.getID(), forwardKey.getValue());
        props.put(backwardsKey.getID(), backwardsKey.getValue());
        props.put(lang.getID(), lang.getValue());
        props.put(font.getID(), font.getValue());
        props.put(sensitivity.getID(), sensitivity.getValue());
        FileOutputStream out = new FileOutputStream(file);
        props.store(out, "OurCraft properties");
        out.close();
    }

    private void load(GameOption option, Properties props)
    {
        if(props.containsKey(option.getID()))
            option.setValue(props.getProperty(option.getID()));
    }
}
