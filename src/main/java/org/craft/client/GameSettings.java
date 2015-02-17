package org.craft.client;

import java.io.*;

import org.craft.client.render.*;
import org.craft.modding.*;
import org.lwjgl.input.*;

public class GameSettings
{

    public GameOption<String>             palette;
    public GameOption<Integer>            jumpKey;
    public GameOption<Integer>            forwardKey;
    public GameOption<Integer>            backwardsKey;
    public GameOption<Integer>            leftKey;
    public GameOption<Integer>            rightKey;
    public GameOption<String>             lang;
    public GameOption<String>             font;
    public GameOption<Float>              sensitivity;
    public GameOption<Float>              musicVolume;
    public GameOption<Float>              soundVolume;
    public GameOption<Float>              masterVolume;
    public GameOption<EnumFullscreenType> fullscreenType;
    private Configuration                 configuration;

    public GameSettings()
    {
        jumpKey = new GameOption<Integer>("jumpKey", GameOptionType.INPUT);
        forwardKey = new GameOption<Integer>("forwardKey", GameOptionType.INPUT);
        backwardsKey = new GameOption<Integer>("backwardsKey", GameOptionType.INPUT);
        leftKey = new GameOption<Integer>("leftKey", GameOptionType.INPUT);
        rightKey = new GameOption<Integer>("rightKey", GameOptionType.INPUT);
        lang = new GameOption<String>("lang", GameOptionType.PLAIN_TEXT);
        font = new GameOption<String>("font", GameOptionType.PLAIN_TEXT);
        sensitivity = new GameOption<Float>("sensitivity", GameOptionType.RANGE);
        musicVolume = new GameOption<Float>("musicVolume", GameOptionType.RANGE);
        soundVolume = new GameOption<Float>("soundVolume", GameOptionType.RANGE);
        masterVolume = new GameOption<Float>("masterVolume", GameOptionType.RANGE);

        fullscreenType = new GameOption<EnumFullscreenType>("fullscreenType", GameOptionType.PLAIN_TEXT);

        palette = new GameOption<>("palette", GameOptionType.CHOICE);

        String[] values = new String[ColorPalette.getPalettes().size() + 1];
        for(int i = 0; i < ColorPalette.getPalettes().size(); i++ )
        {
            values[i + 1] = ColorPalette.getPalettes().get(i).getName();
        }
        values[0] = "None";
        palette.setPossibleValues(values);
        palette.registerCallback(new Runnable()
        {

            @Override
            public void run()
            {
                ShaderBatch postWorldBatch = OurCraft.getOurCraft().getRenderEngine().getPostWorldRenderBatch();
                postWorldBatch.clear();
                ColorPalette colorPalette = ColorPalette.fromName(palette.getValue());
                if(colorPalette == null)
                    postWorldBatch.add(OurCraft.getOurCraft().getRenderEngine().getBlitShader());
                else
                    postWorldBatch.add(colorPalette.getShader());
            }
        });

    }

    public void loadFrom(File file) throws IOException
    {
        configuration = new Configuration(file);
        jumpKey.setValue(configuration.getInt(jumpKey.getID(), Keyboard.KEY_SPACE));
        leftKey.setValue(configuration.getInt(leftKey.getID(), Keyboard.KEY_A));
        rightKey.setValue(configuration.getInt(rightKey.getID(), Keyboard.KEY_D));
        forwardKey.setValue(configuration.getInt(forwardKey.getID(), Keyboard.KEY_W));
        backwardsKey.setValue(configuration.getInt(backwardsKey.getID(), Keyboard.KEY_S));
        lang.setValue(configuration.get(lang.getID(), "en_US"));
        font.setValue(configuration.get(font.getID(), "default"));
        sensitivity.setValue(configuration.getFloat(sensitivity.getID(), 1f));
        musicVolume.setValue(configuration.getFloat(musicVolume.getID(), 1f));
        soundVolume.setValue(configuration.getFloat(soundVolume.getID(), 1f));
        masterVolume.setValue(configuration.getFloat(masterVolume.getID(), 1f));

        fullscreenType.setValue(EnumFullscreenType.valueOf(configuration.get(fullscreenType.getID(), EnumFullscreenType.NATIVE.name())));

        palette.setValue(configuration.get(palette.getID(), "none"));

        ShaderBatch postWorldBatch = OurCraft.getOurCraft().getRenderEngine().getPostWorldRenderBatch();
        postWorldBatch.clear();
        ColorPalette colorPalette = ColorPalette.fromName(palette.getValue());
        if(colorPalette == null)
            postWorldBatch.add(OurCraft.getOurCraft().getRenderEngine().getBlitShader());
        else
            postWorldBatch.add(colorPalette.getShader());
    }

    public void saveTo(File file) throws IOException
    {
        if(configuration != null)
        {
            configuration.setInt(jumpKey.getID(), jumpKey.getValue());
            configuration.setInt(leftKey.getID(), leftKey.getValue());
            configuration.setInt(rightKey.getID(), rightKey.getValue());
            configuration.setInt(forwardKey.getID(), forwardKey.getValue());
            configuration.setInt(backwardsKey.getID(), backwardsKey.getValue());
            configuration.set(lang.getID(), lang.getValue());
            configuration.set(font.getID(), font.getValue());
            configuration.setFloat(sensitivity.getID(), sensitivity.getValue());
            configuration.setFloat(musicVolume.getID(), musicVolume.getValue());
            configuration.setFloat(soundVolume.getID(), soundVolume.getValue());
            configuration.setFloat(masterVolume.getID(), masterVolume.getValue());

            configuration.set(fullscreenType.getID(), fullscreenType.getValue().name());

            configuration.set(palette.getID(), palette.getValue());
            configuration.save();
        }
    }

}
