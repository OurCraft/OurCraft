package org.craft.sound;

import java.net.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class Sounds
{

    public static HashMap<String, List<SoundInfo>> sounds = Maps.newHashMap();

    public static void init()
    {
        registerMultipleSounds("explode", new ResourceLocation("minecraft", "sound/explode"), EnumSoundFormats.VORBIS);
    }

    public static List<SoundInfo> get(String id)
    {
        return sounds.get(id);
    }

    public static void registerSound(String id)
    {
        registerSound(id, new SoundInfo(id, EnumSoundFormats.UNKNOWN, null));
    }

    public static void registerMultipleSounds(String id, ResourceLocation loc, EnumSoundFormats format)
    {
        AssetLoader loader = CommonHandler.getCurrentInstance().getAssetsLoader();
        for(int index = 1;; index++ )
        {
            ResourceLocation subSoundLoc = new ResourceLocation(loc.getFullPath() + index + "." + format.id());
            if(loader.doesResourceExists(subSoundLoc))
            {
                registerSound(id, subSoundLoc);
            }
            else
            {
                Log.error("Sound file " + subSoundLoc.getFullPath() + " doesn't exist, stopping to search for sub sounds of " + loc.getFullPath());
                break;
            }
        }
    }

    public static void registerSound(String id, ResourceLocation loc)
    {
        AssetLoader loader = CommonHandler.getCurrentInstance().getAssetsLoader();
        try
        {
            URL url = loader.getResource(loc).getURL();
            EnumSoundFormats format = EnumSoundFormats.fromID(loc.getExtension());
            SoundInfo info = new SoundInfo(id, format, url);
            registerSound(id, info);
        }
        catch(Exception e)
        {
            Log.error("Error while registering sound", e);
        }
    }

    public static void registerSound(String id, SoundInfo sound)
    {
        List<SoundInfo> infos = sounds.get(id);
        if(infos == null)
        {
            infos = Lists.newArrayList();
        }
        infos.add(sound);
        if(Dev.debug())
        {
            Log.message("Registred sound " + sound.toString());
        }
        sounds.put(id, infos);
    }

    public static Collection<List<SoundInfo>> getAllInfos()
    {
        return sounds.values();
    }

    public static SoundInfo getRandom(String id)
    {
        List<SoundInfo> infos = get(id);
        if(infos != null)
            return infos.get((int) (infos.size() * Math.random()));
        else
            return null;
    }
}
